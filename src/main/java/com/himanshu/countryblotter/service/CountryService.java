package com.himanshu.countryblotter.service;

import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.fetcher.ICountryFetcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryService {
  private final ICountryFetcher<Country> countryFetcher;

  public CountryService(@Qualifier("fileBasedCountryFetcher") ICountryFetcher<Country> countryFetcher) {
    this.countryFetcher = countryFetcher;
  }

  public List<Country> getAll() {
    return countryFetcher.fetchAllCountries();
  }

  public Country getByCode(String code) {
    return countryFetcher.fetchCountry(code);
  }
}
