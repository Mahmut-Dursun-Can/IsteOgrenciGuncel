package com.example.iste.controller.QrController;

import com.example.iste.entity.User;
import com.example.iste.service.QrService.JwtService;
import com.example.iste.service.QrService.QrCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;
    private final JwtService jwtService;

    public QrCodeController(QrCodeService qrCodeService, JwtService jwtService) {
        this.qrCodeService = qrCodeService;
        this.jwtService = jwtService;
    }

    // QR kod üretme endpoint'i
    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody User user) {
        try {
            String base64QR = qrCodeService.generateQRCodeForToken(user);
            return ResponseEntity.ok(base64QR);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("QR kod üretilemedi: " + e.getMessage());
        }
    }
    // Token doğrulama endpoint'i
    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyToken(@PathVariable String token) {
        boolean isValid = jwtService.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok("Token geçerli.");
        } else {
            return ResponseEntity.badRequest().body("Token geçersiz ya da süresi dolmuş.");
        }
    }
}
