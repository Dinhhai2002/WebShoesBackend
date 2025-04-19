package com.shoes.webshoes.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class BarcodeUtil {
    public static byte[] generateEAN13BarcodeImage(String barcode) throws Exception {
        int width = 300;
        int height = 100;
        BitMatrix bitMatrix = new MultiFormatWriter().encode(barcode, BarcodeFormat.EAN_13, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
