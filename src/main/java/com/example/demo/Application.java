package com.example.demo;

import com.example.demo.service.ApplicationService;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Main Spring Boot application configuration class.
 *
 * @author mramach
 *
 */
@SpringBootApplication
@RestController
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Autowired
	private ApplicationService service;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "hostname")
	public ResponseEntity<Map<String, Object>> hostname() {

		try {

			return ResponseEntity.ok(Collections.singletonMap(
					"hostname", service.getHostname()));

		} catch (UnknownHostException e) {

			LOGGER.error("Failed to fetch hostname.", e);

			return ResponseEntity.ok(Collections.singletonMap(
					"hostname", "unknown"));

		}

	}

	@GetMapping(value = "/machineRequest")
	public ResponseEntity callHost(@RequestParam(name = "machine")String machine) {
		ResponseEntity<Map> response = null;
		try {
			response = restTemplate.getForEntity("http://" + machine + "/hostname", Map.class);
		} catch (Exception e) {
			if (response == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			} else {
				return ResponseEntity.status(response.getStatusCode()).body(e.getMessage());
			}
		}
		return ResponseEntity.ok(response.getBody());
	}

	/**
	 * Root entry point for Spring Boot application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}