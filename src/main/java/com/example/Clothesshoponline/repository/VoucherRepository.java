package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Optional<Voucher> findByCode(String code);

    List<Voucher> findByActiveAndStartDateBeforeAndEndDateAfter(
            boolean active,
            LocalDateTime startDate,
            LocalDateTime endDate);
}
