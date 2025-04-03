package com.recipesocial.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

		try {
			String url = "http://localhost:8080/swagger-ui/index.html";
			System.setProperty("java.awt.headless", "false"); // not recommended for prod
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(new URI(url));
				System.out.println("✅ Swagger UI launched in browser: " + url);
			} else {
				System.out.println("Swagger UI available at: " + url);
			}
		} catch (Exception e) {
			System.err.println("Failed to open Swagger UI: " + e.getMessage());
		}
	}

}
