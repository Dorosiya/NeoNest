package com.shyu.NeoNest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${spring.portOne.impCode}")
    private String impCode;

    @GetMapping("/impCode")
    public ResponseEntity<Map<String, String>> getImpCode() {
        Map<String, String> response = new HashMap<>();
        response.put("impCode", impCode);
        return ResponseEntity.ok(response);
    }

}
