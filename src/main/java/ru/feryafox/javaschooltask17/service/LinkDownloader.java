package ru.feryafox.javaschooltask17.service;

import java.nio.file.Path;

public interface LinkDownloader {
    void download(String url, Path path);
}
