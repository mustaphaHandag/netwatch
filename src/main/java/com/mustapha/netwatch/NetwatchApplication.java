package com.mustapha.netwatch;

import com.mustapha.netwatch.config.NotificationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(NotificationProperties.class)
public class NetwatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetwatchApplication.class, args);
    }
}
