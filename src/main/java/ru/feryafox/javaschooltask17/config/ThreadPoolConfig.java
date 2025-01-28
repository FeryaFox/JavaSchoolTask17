package ru.feryafox.javaschooltask17.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService downloadExecutorService(DownloadConfig downloadConfig) {
        return Executors.newFixedThreadPool(downloadConfig.getDownloadThreads());
    }
}

