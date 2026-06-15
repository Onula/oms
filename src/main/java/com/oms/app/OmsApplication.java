package com.oms.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Modulithic
@EnableTransactionManagement
@EnableScheduling
public class OmsApplication {

    public static void main(String[] args) {

        SpringApplication.run(OmsApplication.class, args);
    }

}
