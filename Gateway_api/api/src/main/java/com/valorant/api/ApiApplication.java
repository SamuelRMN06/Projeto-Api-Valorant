package com.valorant.api;

import com.valorant.api.controller.GatewayController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

//	ApiService apiService = new ApiService();
//
//		try{
//		System.out.println(apiService.getSkin());
//	} catch (Exception e) {
//		throw new RuntimeException(e);
//	}
}
