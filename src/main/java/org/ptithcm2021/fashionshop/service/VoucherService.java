package org.ptithcm2021.fashionshop.service;

import lombok.RequiredArgsConstructor;
import org.ptithcm2021.fashionshop.dto.request.VoucherRequest;
import org.ptithcm2021.fashionshop.dto.response.VoucherResponse;
import org.ptithcm2021.fashionshop.mapper.VoucherMapper;
import org.ptithcm2021.fashionshop.model.Voucher;
import org.ptithcm2021.fashionshop.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;

    public VoucherResponse createVoucher(VoucherRequest voucherRequest) {
        if(voucherRepository.findByCode(voucherRequest.getCode()).isPresent()) throw new RuntimeException("Voucher codes really do exist");

        return voucherMapper.toVoucherResponse(voucherRepository.save(voucherMapper.toVoucher(voucherRequest)));
    }

    public String deleteVoucher(int code) {
        Optional<Voucher> voucher = voucherRepository.findById(code);
        if (voucher.isEmpty()) {
            throw new RuntimeException("Voucher not found");
        }
        voucherRepository.delete(voucher.get());
        return "Voucher deleted";
    }

    public VoucherResponse getVoucherById(int code) {
        Optional<Voucher> voucher = voucherRepository.findById(code);
        if (voucher.isEmpty()) {
            throw new RuntimeException("Voucher not found");
        }
        return voucherMapper.toVoucherResponse(voucher.get());
    }

    public List<VoucherResponse> getAllVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        return vouchers.stream().map(voucher -> voucherMapper.toVoucherResponse(voucher)).toList();
    }

    public VoucherResponse updateVoucher(int code, VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        voucherMapper.updateVoucher(voucher, voucherRequest);
        voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponse(voucher);
    }
}
