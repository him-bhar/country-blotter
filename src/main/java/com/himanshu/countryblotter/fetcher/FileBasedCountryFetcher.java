package com.himanshu.countryblotter.fetcher;

import com.google.common.collect.Lists;
import com.himanshu.countryblotter.domain.Country;

import java.util.List;

public class FileBasedCountryFetcher implements ICountryFetcher<Country> {
  private CountryLoader countryLoader;

  public FileBasedCountryFetcher(CountryLoader countryLoader) {
    this.countryLoader = countryLoader;
  }

  @Override
  public List<Country> fetchAllCountries() {
    try {
      return Lists.newArrayList(countryLoader.loadAll ().values());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Country fetchCountry(String countryCode) {
    try {
      return countryLoader.load(countryCode);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
