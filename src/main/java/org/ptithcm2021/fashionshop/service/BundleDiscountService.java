package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.ptithcm2021.fashionshop.dto.request.BDiscountUpdateRequest;
import org.ptithcm2021.fashionshop.dto.request.BundleDiscountRequest;
import org.ptithcm2021.fashionshop.dto.response.BundleDiscountResponse;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.BundleDiscountMapper;
import org.ptithcm2021.fashionshop.model.BundleDiscount;
import org.ptithcm2021.fashionshop.repository.BundleDiscountRepository;
import org.ptithcm2021.fashionshop.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BundleDiscountService {
    private final BundleDiscountRepository bundleDiscountRepository;
    private final BundleDiscountMapper bundleDiscountMapper;
    private final ProductRepository productRepository;

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_SALES')")
    public BundleDiscountResponse createBundleDiscount(BundleDiscountRequest request) {
        BundleDiscount bundleDiscount = bundleDiscountMapper.toBundleDiscount(request);

        bundleDiscount.setMainProduct(productRepository.findById(
                request.getMainProduct_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));

        bundleDiscount.setBundledProduct(productRepository.findById(
                request.getBundledProduct_id()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND)));

        if(request.getStartDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid time.");
        }

        if(!request.getStartDate().isBefore(request.getEndDate())) {
            throw new RuntimeException("Invalid time.");
        }

        try{
            return bundleDiscountMapper.toBundleDiscountResponse(bundleDiscountRepository.save(bundleDiscount));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException("BundleDiscount already exists for this product and start date!", e);
        }
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_SALES')")
    public BundleDiscountResponse updateBundleDiscount(BDiscountUpdateRequest request, int id) {
        BundleDiscount bundleDiscount = bundleDiscountRepository.findById(id).orElseThrow(() -> new RuntimeException("BundleDiscount not found"));

        if(bundleDiscount.getStartDate().isBefore(LocalDateTime.now())
        && bundleDiscount.getEndDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("The bundle discount is active, no changes allowed.");
        }

        if(bundleDiscount.getEndDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("The bundle discount is expired, no changes allowed.");

        bundleDiscountMapper.toUpdateBundleDiscount(bundleDiscount, request);

        return bundleDiscountMapper.toBundleDiscountResponse(bundleDiscountRepository.save(bundleDiscount));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_SALES')")
    public String deleteBundleDiscount(int id) {
        BundleDiscount bundleDiscount = bundleDiscountRepository.findById(id).orElseThrow(() -> new RuntimeException("BundleDiscount not found"));

        if(bundleDiscount.getStartDate().isBefore(LocalDateTime.now())
                && bundleDiscount.getEndDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("The bundle discount is active, no changes allowed.");
        }

        bundleDiscountRepository.delete(bundleDiscount);
        return "BundleDiscount deleted successfully";
    }

    public BundleDiscountResponse getBundleDiscount(int id) {
        BundleDiscount bundleDiscount = bundleDiscountRepository.findById(id).orElseThrow(() -> new RuntimeException("BundleDiscount not found"));

        return bundleDiscountMapper.toBundleDiscountResponse(bundleDiscount);
    }

    public List<BundleDiscountResponse> getAllBundleDiscount() {
        List<BundleDiscount> bundleDiscounts = bundleDiscountRepository.findAll();
        return bundleDiscounts.stream().map(bundleDiscountMapper::toBundleDiscountResponse).toList();
    }
}
