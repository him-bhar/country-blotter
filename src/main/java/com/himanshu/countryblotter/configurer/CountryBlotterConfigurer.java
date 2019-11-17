package com.himanshu.countryblotter.configurer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.fetcher.ICountryFetcher;
import com.himanshu.countryblotter.fetcher.RestCountryFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CountryBlotterConfigurer {
  @Value("${countries.rest.api.url}")
  private String countriesRestApiBaseUri;

  @Value("${countries.rest.api.all}")
  private String countriesAllRestApiUri;

  @Value("${countries.rest.api.code}")
  private String countryByCodeRestApiUri;

  /*@Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }*/

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ICountryFetcher<Country> restCountryFetcher(ObjectMapper objectMapper, RestTemplate restTemplate) {
    return new RestCountryFetcher(countriesRestApiBaseUri, countriesAllRestApiUri, countryByCodeRestApiUri, objectMapper, restTemplate);
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
    return jomb -> jomb.featuresToEnable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
  }

}
