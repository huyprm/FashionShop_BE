package org.ptithcm2021.fashionshop.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.CartRequest;
import org.ptithcm2021.fashionshop.dto.request.CartUpdateRequest;
import org.ptithcm2021.fashionshop.dto.response.CartResponse;
import org.ptithcm2021.fashionshop.enums.DiscountTypeEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.CartMapper;
import org.ptithcm2021.fashionshop.model.*;
import org.ptithcm2021.fashionshop.repository.*;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final BundleDiscountRepository bundleDiscountRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CartMapper cartMapper;

    @Transactional
    public CartResponse createCart(CartRequest cartRequest) {

        User user = userRepository.findById(cartRequest.getUser_id()).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));

        ProductVariant productVariant = productVariantRepository
                .findById(cartRequest.getProductVariant_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Optional<Cart> carted =cartRepository.
                findByUserIdAndProductVariantId(cartRequest.getUser_id(), cartRequest.getProductVariant_id());

        if(carted.isPresent()) {
            return cartMapper.toCartResponse(checkCarted(carted.get(), cartRequest, productVariant));
        }

        Cart cart = new Cart();

        DiscountDetail discountDetails = createCartDiscount(cartRequest.getCartDiscountDetails(), cart, productVariant);

        double totalPrice = discountDetails.totalPrice + cartRequest.getQuantity() * productVariant.getPrice();

        cart.setUser(user);
        cart.setProductVariant(productVariant);
        cart.setQuantity(cartRequest.getQuantity());
        cart.setCartDiscountDetails(discountDetails.cartDiscountDetails);
        cart.setTotalPrice(totalPrice);

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    public CartResponse updateCart(CartUpdateRequest request) {
        Cart cart = cartRepository.findById(request.getCartId()).orElseThrow(() -> new RuntimeException("Cart not found"));

        double totalPrice = 0;
        if (request.getProductDiscount()!=null) {
            for(var productDiscount: cart.getCartDiscountDetails()){
                Integer newQuantity = request.getProductDiscount().get(productDiscount.getId());
                if(newQuantity != null){
                    int temp = productDiscount.getQuantity();

                    productDiscount.setQuantity(newQuantity);

                    productDiscount.setPrice(productDiscount.getPrice()/temp *productDiscount.getQuantity());
                    totalPrice += productDiscount.getPrice();
                }
            }
        }

        cart.setQuantity(request.getQuantity());
        cart.setTotalPrice(totalPrice + cart.getQuantity() * cart.getProductVariant().getPrice());

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    public List<CartResponse> getCartByUserId(String userId) {

        List<Cart> carts = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        return carts.stream().map(cart -> cartMapper.toCartResponse(cart)).collect(Collectors.toList());
    }


    @Transactional
    public String deleteCart(int id, String type) {
        if(type.equals("main")){
            cartRepository.deleteById(id);
        } else {
            cartDetailRepository.deleteById(id);
        }
        return "Cart deleted";
    }
    private DiscountDetail createCartDiscount(List<CartRequest.DiscountDetailRequest> discountDetailRequests, Cart cart, ProductVariant productVariant) {

        AtomicReference<Double> totalPrice = new AtomicReference<>((double) 0);

        List<CartDiscountDetail> cartDiscountDetails =
                Optional.ofNullable(discountDetailRequests).orElse(Collections.emptyList())
                        .stream().map(request->{
                            CartDiscountDetail cartDiscountDetail = new CartDiscountDetail();
                            cartDiscountDetail.setQuantity(request.getQuantity());

                            ProductVariant productDiscount = productVariantRepository
                                    .findById(request.getProductVariant_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                            BundleDiscount bundleDiscount = bundleDiscountRepository
                                    .findById(request.getBundleDiscount_id()).orElseThrow(() -> new RuntimeException("Bundle discount not found"));

                            int x;
                            if((x = checkDiscount(bundleDiscount, productDiscount, productVariant)) != 0) {
                                switch (x){
                                    case 1:{
                                        throw new RuntimeException("Discount expired.");
                                    }
                                    case 2:{
                                        throw new RuntimeException("Product variant is not the same as the product variant in the bundle discount");
                                    }
                                }
                            }
                            double price = priceCalculation(bundleDiscount.getDiscountType(), bundleDiscount.getDiscountValue(), productDiscount.getPrice(), productDiscount.getQuantity());


                            totalPrice.set(totalPrice.get() + price);

                            cartDiscountDetail.setPrice(price);
                            cartDiscountDetail.setBundleDiscount(bundleDiscount);
                            cartDiscountDetail.setProductVariant(productDiscount);
                            cartDiscountDetail.setCart(cart);

                            return cartDiscountDetail;
                        }).toList();

        return new DiscountDetail(cartDiscountDetails, totalPrice.get());
    }

    private record DiscountDetail(List<CartDiscountDetail> cartDiscountDetails, double totalPrice){}

    private double priceCalculation(DiscountTypeEnum discountType, double discountValue, double price, int quantity) {
        double discount_price = discountType == DiscountTypeEnum.PERCENTAGE ?
                price * (1 - discountValue / 100) :
                price - discountValue;

        return discount_price * quantity;

    }

    private int checkDiscount(BundleDiscount bundleDiscount, ProductVariant bundleProduct, ProductVariant mainProduct) {
        if(bundleDiscount.getStartDate().isAfter(LocalDateTime.now()) || bundleDiscount.getEndDate().isBefore(LocalDateTime.now())) {
            log.info(LocalDateTime.now().toString());
            return 1;
        }

        if(mainProduct.getProduct().getId() == bundleDiscount.getMainProduct().getId()){

            if(bundleDiscount.getBundledProduct().getId() != bundleProduct.getProduct().getId())
                return 2;
        }
        else{
           return 2;
        }
        return 0;
    }

    private Cart checkCarted(Cart cart, CartRequest cartRequest, ProductVariant productVariant) {
        Map<Integer, CartDiscountDetail> disCountMap = Optional.ofNullable(cart.getCartDiscountDetails()).orElse(Collections.emptyList())
                .stream().collect(Collectors.toMap(temp -> temp.getProductVariant().getId(), temp -> temp));

        double totalPrice = 0;
        List<CartRequest.DiscountDetailRequest> discountDetailRequests = new ArrayList<>();

        if(cartRequest.getCartDiscountDetails() != null) {
            for (var request : cartRequest.getCartDiscountDetails()) {
                CartDiscountDetail existCart = disCountMap.get(request.getProductVariant_id());

                if (existCart != null) {
                    existCart.setQuantity(existCart.getQuantity() + request.getQuantity());

                    existCart.setPrice(priceCalculation(existCart.getBundleDiscount().getDiscountType(),
                            existCart.getBundleDiscount().getDiscountValue(),
                            existCart.getProductVariant().getPrice(),
                            existCart.getQuantity()));
                    totalPrice += existCart.getPrice();
                } else {
                    discountDetailRequests.add(request);
                }
            }
            if (!discountDetailRequests.isEmpty()) {
                DiscountDetail discountDetails = createCartDiscount(discountDetailRequests, cart, productVariant);

                totalPrice =  discountDetails.totalPrice + cartRequest.getQuantity() * productVariant.getPrice();
                log.info(cart.getCartDiscountDetails().toString());
                cart.getCartDiscountDetails().addAll(discountDetails.cartDiscountDetails);
            }
        }
        cart.setQuantity(cartRequest.getQuantity()+cart.getQuantity());

        cart.setTotalPrice(totalPrice + cart.getTotalPrice());

        return cartRepository.save(cart);
    }

}

