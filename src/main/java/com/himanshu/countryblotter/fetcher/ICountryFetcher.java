package com.himanshu.countryblotter.fetcher;

import java.util.List;

public interface ICountryFetcher<T> {
  List<T> fetchAllCountries();
  T fetchCountry(String countryCode);
}
