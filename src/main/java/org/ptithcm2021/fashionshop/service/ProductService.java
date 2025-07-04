package org.ptithcm2021.fashionshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ptithcm2021.fashionshop.dto.request.ProductRequest;
import org.ptithcm2021.fashionshop.dto.request.ProductUpdateRequest;
import org.ptithcm2021.fashionshop.dto.request.ProductVariantRequest;
import org.ptithcm2021.fashionshop.dto.response.BrandResponse;
import org.ptithcm2021.fashionshop.dto.response.CategoryResponse;
import org.ptithcm2021.fashionshop.dto.response.ProductResponse;
import org.ptithcm2021.fashionshop.dto.response.ProductVariantResponse;
import org.ptithcm2021.fashionshop.enums.ProductStatusEnum;
import org.ptithcm2021.fashionshop.exception.AppException;
import org.ptithcm2021.fashionshop.exception.ErrorCode;
import org.ptithcm2021.fashionshop.mapper.ProductMapper;
import org.ptithcm2021.fashionshop.model.Brand;
import org.ptithcm2021.fashionshop.model.Category;
import org.ptithcm2021.fashionshop.model.Product;
import org.ptithcm2021.fashionshop.model.ProductVariant;
import org.ptithcm2021.fashionshop.repository.BrandRepository;
import org.ptithcm2021.fashionshop.repository.CategoryRepository;
import org.ptithcm2021.fashionshop.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductVariantService productVariantService;
    private final FileService fileService;

    public ProductResponse getProductById(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductResponse productResponse = productMapper.toProductResponse(product);

        productResponse.setBrand(BrandResponse.builder()
                .id(product.getBrand().getId())
                .name(product.getBrand().getName())
                .build());

        productResponse.setCategory(CategoryResponse.builder()
                .name(product.getCategory().getName())
                .id(product.getCategory().getId())
                .build());
        return productResponse;
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")
    public ProductResponse addProduct(ProductRequest productRequest) {
        Brand brand = brandRepository.findById(productRequest.getBrand_id()).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        Category category = categoryRepository.findById(productRequest.getCategory_id()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = new Product();

        product.setImages(productRequest.getImages());
        product.setThumbnail(productRequest.getThumbnail());
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setBrand(brand);
        product.setCategory(category);
        product.setDescription(productRequest.getDescription());

        List<ProductVariant> list = productRequest.getProductVariantList().stream().map(productVariantRequest ->{
            ProductVariant product_variant = ProductVariant.builder()
                    .color(productVariantRequest.getColor())
                    .image(productVariantRequest.getImage())
                    .price(productVariantRequest.getPrice())
                    .quantity(productVariantRequest.getQuantity())
                    .size( productVariantRequest.getSize())
                    .product(product)
            .build();
            return product_variant;
        } ).toList();

        int tempQuantity = productRequest.getProductVariantList().stream().mapToInt(ProductVariantRequest::getQuantity).sum();

        product.setStock_quantity(tempQuantity != 0 ? tempQuantity : productRequest.getStock_quantity());
        product.setProductVariantList(list);
        return productMapper.toProductResponse(productRepository.save(product));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")
    @Transactional
    public ProductResponse updateProduct(ProductUpdateRequest productRequest, int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setImages(productRequest.getImages_deleted());
        fileService.deleteFiles("product", productRequest.getImages_deleted());

        product.setThumbnail(productRequest.getThumbnail());
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());

        if(product.getBrand().getId()!= productRequest.getBrand_id()){
            Brand brand = brandRepository.findById(productRequest.getBrand_id()).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

            product.setBrand(brand);
        }
       if(product.getCategory().getId()!= productRequest.getCategory_id()){
           Category category = categoryRepository.findById(productRequest.getCategory_id()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

           product.setCategory(category);
       }
        product.setStatus(productRequest.getStatus());
        product.setDescription(productRequest.getDescription());

        var tempQuantity = productVariantService.updateProductVariant(productRequest.getProductVariantList())
                .stream().mapToInt(ProductVariantResponse::getQuantity).sum();

        product.setStock_quantity(tempQuantity != 0 ? tempQuantity : productRequest.getStock_quantity());

        return productMapper.toProductResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(product -> {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponse.setProductVariantList(null);
            return productResponse;
        }).collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByBrand(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        return brand.getProducts().stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByCategory(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return category.getProducts().stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByBrandAndCategory(int idBrand, int idCategory) {
        Brand brand = brandRepository.findById(idBrand).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        Category category = categoryRepository.findById(idCategory).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        List<Product> products = productRepository.findByBrandAndCategory(brand, category).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return products.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN','SCOPE_STAFF_WAREHOUSE')")
    public String deleteProduct(int id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setStatus(ProductStatusEnum.DISCONTINUED);
        return "Product deleted";
    }

    public Map<Integer, String> searchProduct(String keyword){
        return productRepository.searchProduct(keyword);
    }
}
