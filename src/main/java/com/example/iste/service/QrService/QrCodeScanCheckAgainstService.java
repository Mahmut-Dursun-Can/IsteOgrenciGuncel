package com.example.iste.service.QrService;

import com.example.iste.entity.QR.QrCode;
import com.example.iste.entity.QR.QrScan;
import com.example.iste.repository.QrRepository;
import com.example.iste.repository.QrScanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class QrCodeScanCheckAgainstService {
    private final QrRepository qrRepository;
    private final QrScanRepository qrScanRepository;

    public QrCodeScanCheckAgainstService(QrRepository qrRepository,
                                         QrScanRepository qrScanRepository) {
        this.qrRepository = qrRepository;
        this.qrScanRepository = qrScanRepository;
    }


    @Transactional(readOnly = true)
    public boolean isLatestQrScanned() {
        // 1. En yeni, süresi henüz dolmamış QR kodunu al
        Optional<QrCode> optionalQrCode = qrRepository
                .findFirstByExpiresAtAfterOrderByCreatedAtDesc(LocalDateTime.now());

        if (optionalQrCode.isEmpty()) {
            // Geçerli QR kodu yok
            System.out.println("Geçerli QR kodu bulunamadı for user: " );
            return false;
        }

        QrCode qrCode = optionalQrCode.get();
        String token = qrCode.getToken();
        LocalDateTime expiresAt = qrCode.getExpiresAt();

        System.out.println("Found QR token: " + token + ", expires at: " + expiresAt);

        // 2. Bu token ile en son taranma kaydını al
        Optional<QrScan> optionalQrScan = qrScanRepository
                .findFirstByTokenOrderByScannedAtDesc(token);

        if (optionalQrScan.isEmpty()) {
            // Hiç taranmamış
            System.out.println("QR kodu hiç taranmadı: " + token);
            return false;
        }

        QrScan qrScan = optionalQrScan.get();
        LocalDateTime scannedAt = qrScan.getScannedAt();

        System.out.println("QR token scanned at: " + scannedAt);

        // 3. Taranma zamanı ile son geçerlilik zamanını karşılaştır
        boolean valid = scannedAt.isBefore(expiresAt);
        System.out.println("QR kodu geçerli mi? " + valid);

        return valid;
    }
}
