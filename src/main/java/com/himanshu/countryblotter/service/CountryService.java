package com.himanshu.countryblotter.service;

import com.himanshu.countryblotter.dao.ICountryDao;
import com.himanshu.countryblotter.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class CountryService {
  private final CacheManager cacheManager;
  private final ICountryDao countryDao;

  public CountryService(@Autowired CacheManager cacheManager, @Autowired ICountryDao countryDao) {
    this.cacheManager = cacheManager;
    this.countryDao = countryDao;
  }

  @Transactional
  public boolean saveCountry(Country country) {
    countryDao.save(country);
    return true;
  }

  @Transactional(readOnly = true)
  public List<Country> getAllCountries() {
    return this.countryDao.selectAll();
  }

  @Transactional(readOnly = true)
  public List<Map> getAllCountriesAsMap() {
    return this.countryDao.selectAllAsMap();
  }

  @Transactional(readOnly = true)
  public Country getCountryByCode(String code) {
    return this.countryDao.getCountryByCode(code);
  }
}
