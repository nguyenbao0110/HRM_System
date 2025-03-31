package com.example.qrcodecheckin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//thêm annotation @EnableCaching để bật cache
@SpringBootApplication
@EnableCaching
public class QrCodeCheckInApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrCodeCheckInApplication.class, args);
    }

}
