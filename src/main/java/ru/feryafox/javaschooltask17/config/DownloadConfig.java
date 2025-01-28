package ru.feryafox.javaschooltask17.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "download")
@Getter
@Setter
public class DownloadConfig {
    private Long kilobytesPerSecond;
    private Integer downloadThreads;
}
