package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.ProductRequest;
import org.ptithcm2021.fashionshop.dto.request.ProductUpdateRequest;
import org.ptithcm2021.fashionshop.dto.request.ProductVariantRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.ProductResponse;
import org.ptithcm2021.fashionshop.dto.response.ProductVariantResponse;
import org.ptithcm2021.fashionshop.service.ProductService;
import org.ptithcm2021.fashionshop.service.ProductVariantService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    public final ProductVariantService productVariantService;
    private final ProductService productService;

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable int id) {
        return ApiResponse.<ProductResponse>builder().data(productService.getProductById(id)).build();
    }


    @PostMapping(value = "/create")
    public ApiResponse<ProductResponse> creatProductVariant(
            @RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .data(productService.addProduct(request))
                .build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable int id, @RequestBody @Valid ProductUpdateRequest request) {
        return ApiResponse.<ProductResponse>builder().data(productService.updateProduct(request, id)).build();
    }

    @GetMapping()
    public ApiResponse<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products = productService.getAllProducts();

        if (products.isEmpty()) { return ApiResponse.<List<ProductResponse>>builder().message("No matching products found").build(); }

        return ApiResponse.<List<ProductResponse>>builder().data(products).build();
    }

//    @GetMapping(value = "/filter", params = "brandId")
//    public ApiResponse<List<ProductResponse>> getAllProductsByBrand(@RequestParam(name = "brandId") int brandId) {
//        List<ProductResponse> products = productService.getProductsByBrand(brandId);
//
//        if(products.isEmpty()) {
//            return ApiResponse.<List<ProductResponse>>builder().message("No matching products found").build();
//        }
//        return ApiResponse.<List<ProductResponse>>builder().data(products).build();
//    }
//
//    @GetMapping(value = "/filter", params = "categoryId")
//    public ApiResponse<List<ProductResponse>> getAllProductsByCategory(@RequestParam(name = "categoryId") int categoryId) {
//        List<ProductResponse> products = productService.getProductsByCategory(categoryId);
//        if(products.isEmpty()) {
//            return ApiResponse.<List<ProductResponse>>builder().message("No matching products found").build();
//        }
//
//        return ApiResponse.<List<ProductResponse>>builder().data(products).build();
//    }
//
//    @GetMapping(value = "/filter", params = {"brandId", "categoryId"})
//    public ApiResponse<List<ProductResponse>> getAllProductByBrandAndCategory(@RequestParam(name ="brandId") int brandId, @RequestParam(name = "categoryId") int categoryId) {
//        List<ProductResponse> products = productService.getProductsByBrandAndCategory(brandId, categoryId);
//        if(products.isEmpty()) {
//            return ApiResponse.<List<ProductResponse>>builder().message("No matching products found").build();
//        }
//
//        return ApiResponse.<List<ProductResponse>>builder().data(products).build();
//    }

    @GetMapping("/filter")
    public ApiResponse<List<ProductResponse>> getAllProducts(
            @RequestParam(name = "brandId", required = false) Integer brandId,
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {

        List<ProductResponse> products;

        if (brandId != null && categoryId != null) {
            products = productService.getProductsByBrandAndCategory(brandId, categoryId);
        } else if (brandId != null) {
            products = productService.getProductsByBrand(brandId);
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
        } else {
            products = productService.getAllProducts();
        }

        if (products.isEmpty()) {
            return ApiResponse.<List<ProductResponse>>builder().message("No matching products found").build();
        }

        return ApiResponse.<List<ProductResponse>>builder().data(products).build();
    }


    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProduct(@PathVariable int id) {
        return ApiResponse.<String>builder().message(productService.deleteProduct(id)).build();
    }

    @GetMapping("/search")
    public ApiResponse<Map<Integer, String>> searchProducts(@RequestParam String keyword) {
        String data = '"'+keyword+'"';
        return ApiResponse.<Map<Integer, String>>builder()
                .data(productService.searchProduct(data)).build();
    }

}
