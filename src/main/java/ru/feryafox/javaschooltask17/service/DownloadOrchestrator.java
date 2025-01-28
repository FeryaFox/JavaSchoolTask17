package ru.feryafox.javaschooltask17.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DownloadOrchestrator {

    private final LinkDownloader linkDownloader;
    private final ExecutorService executorService;

    @Autowired
    public DownloadOrchestrator(LinkDownloader linkDownloader,
                                ExecutorService executorService) {
        this.linkDownloader = linkDownloader;
        this.executorService = executorService;
    }

    public void startDownloads(List<String> links, Path downloadDir) {
        downloadDir.toFile().mkdirs();

        for (String link : links) {
            String fileName = extractFileName(link);
            Path destination = downloadDir.resolve(fileName);

            executorService.submit(() -> linkDownloader.download(link, destination));
        }
    }

    public void waitForDownloadsToFinish() {
        executorService.shutdown();
        try {
            // Ждём завершения всех потоков (например, до 10 минут)
            if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
                // Если за 10 минут задачи не завершились, принудительно останавливаем
                executorService.shutdownNow();
                System.out.println("Превышено время ожидания, выполнение прервано.");
            }
        } catch (InterruptedException e) {
            // При прерывании пытаемся остановить всё
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private String extractFileName(String link) {
        int lastSlash = link.lastIndexOf('/');
        if (lastSlash == -1 || lastSlash == link.length() - 1) {
            return "unknown_file";
        }
        return link.substring(lastSlash + 1);
    }
}
