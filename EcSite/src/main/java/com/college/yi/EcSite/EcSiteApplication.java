package com.college.yi.EcSite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.college.yi.EcSite.admin.repository")
public class EcSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcSiteApplication.class, args);
	}

}