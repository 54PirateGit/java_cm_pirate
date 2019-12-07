package com.chengm.pirate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * program: CmPirate
 * description: 程序入口
 * author: ChengMo
 * create: 2019-11-30 14:19
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.chengm.pirate.dao")
public class CmPirateApp {

    public static void main(String[] args) {
        SpringApplication.run(CmPirateApp.class, args);
    }

}
