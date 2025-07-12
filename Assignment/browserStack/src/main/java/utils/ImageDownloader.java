package utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageDownloader {

    private static final String DOWNLOAD_DIR = "src/main/resources/";

    public static void downloadImage(String imageUrl, String fileName) {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.createDirectories(Paths.get(DOWNLOAD_DIR));
            Files.copy(in, Paths.get(DOWNLOAD_DIR + fileName));
            System.out.println("✅ Image saved to " + DOWNLOAD_DIR + fileName);
        } catch (IOException e) {
            System.out.println("❌ Failed to download image: " + DOWNLOAD_DIR + fileName);
        }
    }
}
