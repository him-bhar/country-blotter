package com.himanshu.countryblotter.configurer;

import com.himanshu.countryblotter.healthcheck.CountriesRestApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthConfigurer {
  @Value("${countries.rest.api.url}")
  private String countriesRestApiUrl;

  @Bean
  public CountriesRestApi countriesRestApi() {
    return new CountriesRestApi(countriesRestApiUrl);
  }

}
