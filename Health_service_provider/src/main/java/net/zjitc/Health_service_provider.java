package net.zjitc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class Health_service_provider {

    public static void main(String[] args) {
        SpringApplication.run(Health_service_provider.class, args);
    }

}
