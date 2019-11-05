package com.david.interview.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TransferApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TransferApplication.class);
        application.run(args);
    }
}
