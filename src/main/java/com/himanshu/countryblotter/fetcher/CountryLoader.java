package com.himanshu.countryblotter.fetcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.himanshu.countryblotter.domain.Country;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CountryLoader {

  private final File countriesFile;
  private final ObjectMapper objectMapper;

  public CountryLoader(File countriesFile, ObjectMapper objectMapper) {
    super();
    this.countriesFile = countriesFile;
    this.objectMapper = objectMapper;
  }

  public Map<String, Country> loadAll(Iterable<? extends String> keys) throws Exception {
    List<Country> countries = objectMapper.readValue(countriesFile, new TypeReference<List<Country>>() {});
    Map<String, Country> countryMapByCode = countries.stream().collect(Collectors.toMap(country -> country.getCode(), country -> country,
          (oldVal, newVal) -> oldVal));
    Map<String, Country> resultMap = Maps.newHashMap();
    for(String key : keys) {
      if (countryMapByCode.get(key) != null) {
        resultMap.put(key, countryMapByCode.get(key));
      }
    }
    return resultMap;
  }

  public Country load(String s) throws Exception {
    List<Country> countries = objectMapper.readValue(countriesFile, new TypeReference<List<Country>>() {});
    Map<String, Country> countryMapByCode = countries.stream().collect(Collectors.toMap(country -> country.getCode(), country -> country,
          (oldVal, newVal) -> oldVal));
    return countryMapByCode.get(s);
  }

  public Map<String, Country> loadAll() throws Exception {
    List<Country> countries = objectMapper.readValue(countriesFile, new TypeReference<List<Country>>() {});
    return countries.stream().collect(Collectors.toMap(country -> country.getCode(), country -> country,
          (oldVal, newVal) -> oldVal));

  }

}
