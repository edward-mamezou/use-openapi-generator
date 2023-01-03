package com.mamezou_tech.example.infrastructure.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public record BarcodeGenerator(int width, int height) {

    public byte[] generate(final String url) throws IOException, WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return out.toByteArray();
        }
    }
}
