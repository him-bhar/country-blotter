package com.himanshu.countryblotter.dao;

import com.himanshu.countryblotter.domain.Country;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@MapperScan
public interface ICountryDao {
  List<Country> selectAll();
  void save(Country country);
  List<Map> selectAllAsMap();
}
