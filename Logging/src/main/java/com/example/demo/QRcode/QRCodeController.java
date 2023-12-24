package com.example.demo.QRcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/qrcodes")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getQRCodeById(@PathVariable Integer id) {
        // Fetch QR code data from the database based on the provided ID
        Optional<QRCode> qrCodeEntity = qrCodeService.getQRCodeById(id);

        if (qrCodeEntity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Decode the base64 string back to byte array
        byte[] decodedBytes = Base64.getDecoder().decode(qrCodeEntity.get().getQRCodeData());

        // Set appropriate headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(decodedBytes.length);

        return new ResponseEntity<>(decodedBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateQRCode(@RequestParam("email") String email) {
        try {
            qrCodeService.saveQRCode(email);
            return ResponseEntity.ok("QR Code generated and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate QR Code: " + e.getMessage());
        }
    }
}

