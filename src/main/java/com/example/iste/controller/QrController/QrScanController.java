package com.example.iste.controller.QrController;

import com.example.iste.entity.QR.QrScan;
import com.example.iste.repository.QrScanRepository;
import com.example.iste.service.QrService.QrCodeScanCheckAgainstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/qr-scan")
public class QrScanController {

    @Autowired
    private QrScanRepository qrScanRepository;

    @Autowired
    private QrCodeScanCheckAgainstService qrCodeScanCheckAgainstService;

    // QR kodu tarandığında çağrılır
    @PostMapping
    public ResponseEntity<String> saveScan(@RequestBody QrScan qrScan) {

        qrScan.setScannedAt(LocalDateTime.now());
        qrScanRepository.save(qrScan);
        return ResponseEntity.ok("QR verisi kaydedildi.");
    }

    // Admin dashboard: En son QR kod geçerli mi (true/false)
    @GetMapping("/status")
    public ResponseEntity<Boolean> getLatestQrStatus() {
        boolean isValid = qrCodeScanCheckAgainstService.isLatestQrScanned();
        return ResponseEntity.ok(isValid);
    }
}
