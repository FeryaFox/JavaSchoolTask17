package ru.feryafox.javaschooltask17.service.impl;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;
import ru.feryafox.javaschooltask17.service.LinkDownloader;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

@Service
public class LinkDownloaderImpl implements LinkDownloader {

    private final Bucket bucket;

    public LinkDownloaderImpl(Bucket globalBucket) {
        this.bucket = globalBucket;
    }

    @Override
    public void download(String urlStr, Path destination) {
        System.out.println("Start downloading: " + urlStr);

        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);

            try (InputStream is = new BufferedInputStream(connection.getInputStream());
                 FileOutputStream fos = new FileOutputStream(destination.toFile())) {

                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = is.read(buffer)) != -1) {

                    bucket.asBlocking().consume(bytesRead);

                    fos.write(buffer, 0, bytesRead);
                }
            } finally {
                connection.disconnect();
            }

            System.out.println("Finished downloading: " + urlStr);
        } catch (Exception e) {
            System.err.println("Error downloading " + urlStr + ": " + e.getMessage());
        }
    }
}
