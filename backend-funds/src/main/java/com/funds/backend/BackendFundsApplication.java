package com.funds.backend;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendFundsApplication {

	public static void main(String[] args) {
		// Fase 0: zona horaria del sistema America/New_York (ET) para cálculo y presentación.
		TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
		SpringApplication.run(BackendFundsApplication.class, args);
	}

}
