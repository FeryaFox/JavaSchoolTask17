package ru.feryafox.javaschooltask17.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileReadingService {

    public List<String> readLinks(Path filePath) throws IOException {
        return Files.readAllLines(filePath)
                .stream()
                .filter(line -> !line.isBlank())
                .toList();
    }
}
