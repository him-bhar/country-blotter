package com.himanshu.countryblotter.dao;

import com.himanshu.countryblotter.domain.Country;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@MapperScan
public interface ICountryDao {
  List<Country> selectAll();
  Country getCountryByName(String countryName);
  void save(Country country);
  List<Map> selectAllAsMap();
  void removeAllCountries();
}
