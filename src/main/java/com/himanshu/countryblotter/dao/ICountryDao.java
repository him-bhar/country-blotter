package com.himanshu.countryblotter.dao;

import com.himanshu.countryblotter.domain.Country;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@MapperScan
public interface ICountryDao {
  //@Cacheable(cacheNames = {"countryCache"})
  List<Country> selectAll();
  Country getCountryByName(String countryName);
  @Cacheable(cacheNames = {"countryCache"}, key = "#code", unless = "#result == null")
  Country getCountryByCode(String code);
  void save(Country country);
  List<Map> selectAllAsMap();
  void removeAllCountries();
}
