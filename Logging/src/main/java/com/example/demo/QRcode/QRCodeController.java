package com.example.demo.QRcode;

import com.example.demo.user.User;
import com.example.demo.user.UserRepo;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/v1/qrcodes")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/{userId}")
    public ResponseEntity<byte[]> getQRCodeById(@PathVariable String userId) {
        // Fetch QR code data from the database based on the provided ID

        Optional<User> user = userRepo.findById(UUID.fromString(userId));
        int id = user.get().getQrCode().getId();
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


    @PostMapping("/scanQR")
    public String scanQRCode(@RequestBody String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bis);

            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(image)));
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            return result.getText();
        } catch (NotFoundException | IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadQRCodeById(@PathVariable String id) {
        return qrCodeService.downloadQRCodeImageById(id);
    }
}

