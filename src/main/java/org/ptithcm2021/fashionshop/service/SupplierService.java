package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.SupplierRequest;
import org.ptithcm2021.fashionshop.dto.response.SupplierResponse;
import org.ptithcm2021.fashionshop.enums.AccountStatusEnum;
import org.ptithcm2021.fashionshop.mapper.SupplierMapper;
import org.ptithcm2021.fashionshop.model.Supplier;
import org.ptithcm2021.fashionshop.repository.SupplierRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAuthority({'ADMIN','STAFF_WAREHOUSE'})")
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    public SupplierResponse getSupplierById(int id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found."));

        return supplierMapper.toSupplierResponse(supplier);
    }


    public List<SupplierResponse> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();

        return suppliers.stream().map(supplierMapper::toSupplierResponse).toList();
    }

    public SupplierResponse addSupplier(SupplierRequest request) {
        if(supplierRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Supplier already exists.");
        }

        Supplier supplier = supplierMapper.toSupplier(request);
        supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(supplier);
    }

    public SupplierResponse updateSupplier(SupplierRequest request) {
        Supplier supplier = supplierRepository.findByName(request.getName()).orElseThrow(() -> new RuntimeException("Supplier not found."));

        supplierMapper.toUpdateSupplier(supplier, request);

        supplierRepository.save(supplier);

        return supplierMapper.toSupplierResponse(supplier);
    }

    public String deleteSupplier(int id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found."));
        supplier.setStatus(AccountStatusEnum.CANCELED);
        supplierRepository.save(supplier);
        return "Supplier deleted successfully";
    }
}
