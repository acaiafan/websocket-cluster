package com.cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author:xiasc
 * @Date:2018/10/31
 * @Time:14:57
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
public class WebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class);
    }

    @GetMapping("/info")
    public String info(){
        return "ws-info";
    }
}
