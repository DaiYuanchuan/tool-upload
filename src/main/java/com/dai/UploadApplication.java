package com.dai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @program: toolupload
 * @description:
 * @author: Dai Yuanchuan
 * @create: 2019-10-28 16:05
 **/
@SpringBootApplication
@EnableWebMvc
public class UploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }
}
