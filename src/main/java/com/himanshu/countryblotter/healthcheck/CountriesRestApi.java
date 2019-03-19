package com.himanshu.countryblotter.healthcheck;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class CountriesRestApi implements HealthIndicator {
  private String url;

  public CountriesRestApi(String url) {
    this.url = url;
  }

  @Override
  public Health health() {
    String testUrl = url+"/all";
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity(testUrl, String.class, new HashMap<>());
    Health.Builder healthBuilder = new Health.Builder();
    return response.getStatusCode() == HttpStatus.OK ? Health.up().build() : Health.down().build();
  }
}
