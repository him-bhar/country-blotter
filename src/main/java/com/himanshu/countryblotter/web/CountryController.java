package com.himanshu.countryblotter.web;

import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.fetcher.ICountryFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

  @Autowired
  private ICountryFetcher<Country> countryFetcher;

  @RequestMapping(path = {"/all"}, method = {RequestMethod.GET})
  public ResponseEntity<List<Country>> getAllCountries() {
    return new ResponseEntity(countryFetcher.fetchAllCountries(), HttpStatus.OK);
  }

  @RequestMapping(path = {"/{country-code}"}, method = {RequestMethod.GET})
  public ResponseEntity<List<Country>> getCountryByCode(@PathVariable("country-code") String countryCode) {
    System.out.println("Looking for country: "+countryCode);
    Country country = countryFetcher.fetchCountry(countryCode);
    if (country != null) {
      return new ResponseEntity(countryFetcher.fetchCountry(countryCode), HttpStatus.OK);
    } else {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
