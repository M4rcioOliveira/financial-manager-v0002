package com.github.m4rcioliveira.financial_manager_v0002;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinancialManagerV0002Application {

	public static void main(String[] args) {
		SpringApplication.run(FinancialManagerV0002Application.class, args);
	}

}
