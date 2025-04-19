package com.example.iste.repository;

import com.example.iste.entity.QR.QrScan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrScanRepository extends JpaRepository<QrScan, Long> {
    Optional<QrScan> findFirstByTokenOrderByScannedAtDesc(String token);
}
