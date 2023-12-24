package com.example.demo.QRcode;

import com.example.demo.user.User;
import com.example.demo.user.UserRepo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class QRCodeService {

    @Autowired
    private QRCodeRepository qrCodeRepository;
    @Autowired
    private final UserRepo userRepo;

    public QRCodeService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void saveQRCode(String email) throws WriterException, IOException {
        Optional<User> user = userRepo.findByEmail(email);
        UUID userId = user.get().getId();

        User updateUser = user.get();

        if(user.get().getEmail().equals(email)){
        String base64QRCode = generateQRCode(email, userId);

        QRCode qrCode = new QRCode();
        qrCode.setEmail(email);
        qrCode.setUserId(userId);
        qrCode.setQRCodeData(base64QRCode);

        updateUser.setQrCode(qrCode);
        qrCodeRepository.save(qrCode);
        userRepo.save(updateUser);
        }
    }

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    public static String generateQRCode(String email, UUID userId) throws WriterException, IOException {

        String qrCodeData = "eamil :" +email + "\n"+"userId: "+ userId;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                qrCodeData, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();

        // Convert byte array to base64 for storage in the database

        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }

    public Optional<QRCode> getQRCodeById(Integer id) {
        return qrCodeRepository.findById(id);
    }

}
