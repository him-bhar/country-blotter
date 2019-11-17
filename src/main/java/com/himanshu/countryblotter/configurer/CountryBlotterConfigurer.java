package com.himanshu.countryblotter.configurer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.fetcher.CountryLoader;
import com.himanshu.countryblotter.fetcher.FileBasedCountryFetcher;
import com.himanshu.countryblotter.fetcher.ICountryFetcher;
import com.himanshu.countryblotter.fetcher.RestCountryFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;

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

  @Bean(name = "fileBasedCountryFetcher")
  @Autowired
  public ICountryFetcher<Country> fileBasedCountryFetcher(CountryLoader countryLoader) {
    return new FileBasedCountryFetcher(countryLoader);
  }

  @Bean
  public CountryLoader countryLoader(
        @Value("${countries.file.path}") String countriesFile,
        ObjectMapper objectMapper) throws FileNotFoundException {
    return new CountryLoader(ResourceUtils.getFile(countriesFile), objectMapper);
  }

}
