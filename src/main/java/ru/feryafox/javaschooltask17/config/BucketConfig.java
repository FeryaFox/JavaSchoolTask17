package ru.feryafox.javaschooltask17.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BucketConfig {

    @Bean
    public Bucket globalBucket(DownloadConfig downloadConfig) {

        long bytesPerSecond = downloadConfig.getKilobytesPerSecond() * 1024;

        Bandwidth limit = Bandwidth.classic(
                bytesPerSecond,
                Refill.greedy(bytesPerSecond, Duration.ofSeconds(1))
        );
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
