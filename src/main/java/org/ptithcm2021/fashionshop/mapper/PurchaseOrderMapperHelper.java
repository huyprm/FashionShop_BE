//package org.ptithcm2021.fashionshop.mapper;
//
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.Helper;
//import org.ptithcm2021.fashionshop.exception.AppException;
//import org.ptithcm2021.fashionshop.exception.ErrorCode;
//import org.ptithcm2021.fashionshop.model.Product;
//import org.ptithcm2021.fashionshop.model.Supplier;
//import org.ptithcm2021.fashionshop.model.User;
//import org.ptithcm2021.fashionshop.repository.ProductRepository;
//import org.ptithcm2021.fashionshop.repository.SupplierRepository;
//import org.ptithcm2021.fashionshop.repository.UserRepository;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class PurchaseOrderMapperHelper {
//    private final UserRepository userRepository;
//    private final SupplierRepository supplierRepository;
//    private final ProductRepository productRepository;
//
//    public Supplier findSupplierById(int id) {
//        return supplierRepository.findById(id).orElseThrow(() ->
//                new RuntimeException("Supplier not found with id: " + id));
//    }
//
//    public Product findProductById(int id) {
//        return productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
//    }
//
//    public User findUserById(String id) {
//        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//    }
//}
