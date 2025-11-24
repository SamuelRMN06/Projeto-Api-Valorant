package com.valorant.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valorant.api.dtos.SkinDTO;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ApiService {

    SkinDTO skinDTO = new SkinDTO();

    public SkinDTO getSkin(){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://valorant-api.com/v1/weapons/skinchromas"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            skinDTO = mapper.readValue(response.body(), SkinDTO.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
        return skinDTO;
    }
}
