package org.ptithcm2021.fashionshop.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.SupplierRequest;
import org.ptithcm2021.fashionshop.dto.response.ApiResponse;
import org.ptithcm2021.fashionshop.dto.response.SupplierResponse;
import org.ptithcm2021.fashionshop.service.SupplierService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @GetMapping("/{id}")
    public ApiResponse<SupplierResponse> getSupplierById(@PathVariable int id) {

        return ApiResponse.<SupplierResponse>builder().data(supplierService.getSupplierById(id)).build();
    }

    @GetMapping()
    public ApiResponse<List<SupplierResponse>> getAllSuppliers() {

        return ApiResponse.<List<SupplierResponse>>builder().data(supplierService.getAllSuppliers()).build();
    }

    @PostMapping("/add")
    public ApiResponse<SupplierResponse> addSupplier(@RequestBody @Valid SupplierRequest supplierRequest) {
        return ApiResponse.<SupplierResponse>builder().data(supplierService.addSupplier(supplierRequest)).build();
    }

    @PutMapping("/update")
    public ApiResponse<SupplierResponse> updateSupplier(@RequestBody @Valid SupplierRequest supplierRequest) {
        return ApiResponse.<SupplierResponse>builder().data(supplierService.updateSupplier(supplierRequest)).build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteSupplier(@PathVariable int id) {
        return ApiResponse.<String>builder().message(supplierService.deleteSupplier(id)).build();
    }

}
