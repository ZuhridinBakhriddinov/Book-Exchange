package uz.pdp.bookrematch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "uz.pdp.clients")
public class BookRematchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookRematchApplication.class, args);
    }

}
