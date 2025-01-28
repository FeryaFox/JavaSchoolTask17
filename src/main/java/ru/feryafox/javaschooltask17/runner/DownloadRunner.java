package ru.feryafox.javaschooltask17.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.feryafox.javaschooltask17.service.DownloadOrchestrator;
import ru.feryafox.javaschooltask17.service.FileReadingService;

import java.nio.file.Path;
import java.util.List;

@Component
public class DownloadRunner implements CommandLineRunner {

    private final FileReadingService fileReadingService;
    private final DownloadOrchestrator orchestrator;

    @Autowired
    public DownloadRunner(FileReadingService fileReadingService,
                          DownloadOrchestrator orchestrator) {
        this.fileReadingService = fileReadingService;
        this.orchestrator = orchestrator;
    }

    @Override
    public void run(String... args) throws Exception {
        // Файл со ссылками — src/main/resources/links.txt
        Path linksFile = Path.of("src", "main", "resources", "links.txt");

        Path downloadDir = Path.of("downloads");

        List<String> links = fileReadingService.readLinks(linksFile);
        System.out.println("Found " + links.size() + " links");

        orchestrator.startDownloads(links, downloadDir);

        orchestrator.waitForDownloadsToFinish();
        System.out.println("All downloads have finished. Exiting program.");
    }
}
