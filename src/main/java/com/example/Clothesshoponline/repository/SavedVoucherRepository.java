package com.example.Clothesshoponline.repository;

import com.example.Clothesshoponline.model.SavedVoucher;
import com.example.Clothesshoponline.model.User;
import com.example.Clothesshoponline.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedVoucherRepository extends JpaRepository<SavedVoucher, Long> {

    boolean existsByUserAndVoucher(User user, Voucher voucher);

    List<SavedVoucher> findByUser(User user);

    Optional<SavedVoucher> findByUserAndVoucher(User user, Voucher voucher);
}
