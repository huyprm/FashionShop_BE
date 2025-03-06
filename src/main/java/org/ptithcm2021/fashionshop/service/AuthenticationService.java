package org.ptithcm2021.fashionshop.service;

import com.nimbusds.jose.*;

import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.EmailRequest;
import org.ptithcm2021.fashionshop.dto.request.LoginRequest;
import org.ptithcm2021.fashionshop.dto.response.AuthenticationResponse;
import org.ptithcm2021.fashionshop.enums.AccountStatusEnum;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.model.User;
import org.ptithcm2021.fashionshop.repository.UserRepository;
import org.ptithcm2021.fashionshop.util.MailUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${jwt.signerKey}")
    private String sign;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailUtils mailUtils;

    public AuthenticationResponse login (LoginRequest loginRequest) throws Exception {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        User user = new User();

        int check = loginRequest.getUsername().matches(emailRegex) ? 1 : 2 ;
        if(check == 1){
            user = userRepository.findByEmail(loginRequest.getUsername()).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        } else {
            user = userRepository.findByPhone(loginRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        }

        boolean checkpassword = passwordEncoder.matches(loginRequest.getPassword(),user.getPassword());
        if(!checkpassword) throw new AppException(ErrorCode.WRONG_PASSWORD);

        if(user.getStatus().equals(AccountStatusEnum.PENDING)) throw new AppException((ErrorCode.ACCOUNT_LOCKED));

        String refreshToken = generateRefreshToken(user);
        String accessToken = generateAccessToken(user);

        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .cookie(createCookie(refreshToken, 86400))
                .build();
    }


    public String generateAccessToken(User user) throws Exception {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS512).type(JOSEObjectType.JWT).build();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("fashionshop.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(3600, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getRole().getRole())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try{
            jwsObject.sign(new MACSigner(sign.getBytes()));
        } catch (KeyLengthException e) {

            throw new Exception(e);
        } catch (JOSEException e) {

            throw new Exception(e);
        }
        return jwsObject.serialize();
    }

    public String generateRefreshToken(User user) throws Exception {
        JWSHeader  jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS512).type(JOSEObjectType.JWT).build();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("fashionshop.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(86400, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try{
            jwsObject.sign(new MACSigner(sign.getBytes()));
            //into blacklist
        } catch (KeyLengthException e) {
            throw new Exception(e);
        } catch (JOSEException e) {
            throw new Exception(e);
        }
        return jwsObject.serialize();
    }

    private Boolean verifyToken(String token ) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(sign.getBytes());

        SignedJWT jwt = SignedJWT.parse(token);

        var verifiedToken = jwt.verify(verifier);

        if(!(verifiedToken && jwt.getJWTClaimsSet().getIssueTime().before(new Date()))){
            throw new AppException(ErrorCode.INVALID_JWT);
        }
        return true;
    }


    public Cookie logout(String id) {
        User user = userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));

        user.setRefreshToken(null);
        userRepository.save(user);

        return createCookie(null, 0);
    }

    public void sendVerificationEmail(String email){
        EmailRequest emailRequest = new EmailRequest();
        User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        String verificationUrl = "http://localhost:8080/api/auth/verifyEmail?token=" + user.getRefreshToken();

        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject(user.getEmail());
        emailRequest.setBody("Click this link to verify your email: " + verificationUrl);

        mailUtils.sendEmail(emailRequest);
    }

    public void verificationEmail(String token){
        User user = userRepository.findByRefreshToken(token).orElseThrow(()-> new AppException(ErrorCode.INVALID_JWT));
        if(user.getRefreshToken().isEmpty()) throw new AppException(ErrorCode.INVALID_JWT);
        user.setRefreshToken(null);
        user.setStatus(AccountStatusEnum.ACTIVE);
        userRepository.save(user);
    }

    private Cookie createCookie(String refreshToken, int expireTime){
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(expireTime);

        return cookie;
    }

    public AuthenticationResponse refreshToken(String refreshToken, String userId) throws Exception {
        if(refreshToken == null)  throw new RuntimeException("Refresh token not found");

        if (!verifyToken(refreshToken))
            throw new AppException(ErrorCode.INVALID_JWT);

        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //check token revoked
        if(!user.getRefreshToken().equals(refreshToken)) throw new AppException(ErrorCode.INVALID_JWT);

        String refresh = generateRefreshToken(user);
        user.setRefreshToken(refresh);
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .accessToken(generateAccessToken(user))
                .cookie(createCookie(refresh, 86400))
                .build();
    }
}
