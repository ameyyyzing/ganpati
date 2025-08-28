package com.puneganpatidarshan.ganpati_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig {

  // Comma-separated list, e.g.
  // "http://localhost:5173,https://*.vercel.app,https://*.onrender.com"
  @Value("${app.cors.allowed-origins:http://localhost:5173,http://localhost:*http://127.0.0.1:*,http://192.168.*.*}")
  private String allowedOrigins;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    // split + trim
    final String[] patterns = Arrays.stream(allowedOrigins.split(","))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .toArray(String[]::new);

    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            // Use patterns so we can allow wildcards and HTTPS domains
            .allowedOriginPatterns(patterns)
            .allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Location")
            .allowCredentials(true)
            .maxAge(3600);
      }
    };
  }
}
