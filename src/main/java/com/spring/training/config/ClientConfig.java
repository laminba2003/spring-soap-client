package com.spring.training.config;

import lombok.Data;

import java.util.Map;

@Data
public class ClientConfig {
    String location;
    Map<String, Object> security;
}
