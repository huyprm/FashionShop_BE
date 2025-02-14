package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.BrandRequest;
import org.ptithcm2021.fashionshop.dto.response.BrandResponse;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.model.Brand;
import org.ptithcm2021.fashionshop.repository.BrandRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")    public BrandResponse crateBrand(BrandRequest brandRequest) {
        Brand newBrand = Brand.builder()
                .name(brandRequest.getName())
                .description(brandRequest.getDescription())
                .build();
        Brand brand = brandRepository.save(newBrand);

        BrandResponse brandResponse = BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .build();
        return brandResponse;
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")    public BrandResponse updateBrand(BrandRequest brandRequest, int id) {
        Brand newBrand = brandRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        newBrand.setName(brandRequest.getName());
        newBrand.setDescription(brandRequest.getDescription());

        brandRepository.save(newBrand);

        BrandResponse brandResponse = BrandResponse.builder()
                .id(newBrand.getId())
                .name(newBrand.getName())
                .description(newBrand.getDescription())
                .build();

        return brandResponse;
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")
    public String deleteBrand(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        if (!brand.getProducts().isEmpty()) throw new AppException(ErrorCode.FORBIDDEN);
        brandRepository.deleteById(id);
        return "Deleted brand";
    }

    public List<BrandResponse> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();

        List<BrandResponse> brandResponses = new ArrayList<>();

        brands.forEach(brand -> {
            brandResponses.add(BrandResponse.builder()
                    .id(brand.getId())
                    .description(brand.getDescription())
                    .name(brand.getName()).build());
        });

        return brandResponses;
    }

    public BrandResponse getBrandById(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        BrandResponse brandResponse = BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .build();
        return brandResponse;
    }

}