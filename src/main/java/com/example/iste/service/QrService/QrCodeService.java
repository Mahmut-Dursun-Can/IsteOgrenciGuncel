package com.example.iste.service.QrService;

import com.example.iste.entity.QR.QrCode;
import com.example.iste.entity.User;
import com.example.iste.repository.QrRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;


@Service
public class QrCodeService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private QrRepository qrRepository;

    public String generateQrCode(User user) {
        String username = user.getUsername();
        LocalDateTime now = LocalDateTime.now();

        // 1) Süresi dolmamış QR kodu kontrolü
        Optional<QrCode> existing = qrRepository
                .findFirstByUsernameAndExpiresAtAfterOrderByCreatedAtDesc(username, now);
        if (existing.isPresent()) {
            return existing.get().getToken();
        }


        String token = jwtService.generateToken(user.getUsername());

        QrCode qrcode = new QrCode();
        qrcode.setToken(token);
        qrcode.setCreatedAt(LocalDateTime.now());
        ZoneId zoneId = ZoneId.systemDefault(); // veya "Europe/Istanbul"
        qrcode.setExpiresAt(LocalDateTime.ofInstant(Instant.now().plus(1, ChronoUnit.MINUTES), zoneId));
        qrcode.setUsername(user.getUsername());


        qrRepository.save(qrcode);

        return token;
    }

    public byte[] generateQRCodeImage(String text) throws Exception {
        int width = 250;
        int height = 250;
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public String generateQRCodeForToken(User user) throws Exception {
        String token = generateQrCode(user); // JWT üret + DB'ye kaydet
        String payload = "{\"token\":\"" + token + "\"}";
        byte[] qrImage = generateQRCodeImage(payload);
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrImage);
    }
}
