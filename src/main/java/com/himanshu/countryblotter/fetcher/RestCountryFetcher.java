package com.himanshu.countryblotter.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.himanshu.countryblotter.domain.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class RestCountryFetcher implements ICountryFetcher<Country> {
  private final String baseCountryApiUri;
  private final String allCountriesApiSubUri;
  private final String getCountryApiSubUri;
  private final ObjectMapper mapper;
  private final RestTemplate restTemplate;
  private static final Logger LOG = LoggerFactory.getLogger(RestCountryFetcher.class);

  public RestCountryFetcher(String baseCountryApiUri, String allCountriesApiSubUri, String getCountryApiSubUri,
                            ObjectMapper objectMapper, RestTemplate restTemplate) {
    this.baseCountryApiUri = baseCountryApiUri;
    this.allCountriesApiSubUri = allCountriesApiSubUri;
    this.getCountryApiSubUri = getCountryApiSubUri;
    this.mapper = objectMapper;
    this.restTemplate = restTemplate;
  }

  @Override
  public List<Country> fetchAllCountries() {
    ResponseEntity<String> response = restTemplate.getForEntity(this.baseCountryApiUri.concat(this.allCountriesApiSubUri), String.class, new HashMap<>());
    try {
      return this.mapper.readValue(response.getBody(), mapper.getTypeFactory().constructCollectionType(List.class, Country.class));
    } catch (IOException e) {
      throw new RuntimeException("Error while parsing response for all countries from rest api.", e);
    }
  }

  //unless is a negative condition, as below says if result is null. Don't cache it.
  @Override
  public Country fetchCountry(String countryCode) {
    LOG.info("Fetching country : [{}] from REST API", countryCode);
    ResponseEntity<String> response = restTemplate.getForEntity(this.baseCountryApiUri.concat(this.getCountryApiSubUri.replace("%(country-code)", countryCode)), String.class, new HashMap<>());
    try {
      return this.mapper.readValue(response.getBody(), Country.class);
    } catch (IOException e) {
      throw new RuntimeException("Error while parsing response for fetching "+countryCode+" from rest api.", e);
    }
  }
}
