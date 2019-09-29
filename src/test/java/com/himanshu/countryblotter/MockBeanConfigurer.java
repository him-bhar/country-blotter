package com.himanshu.countryblotter;

import com.himanshu.countryblotter.dao.ICountryDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

public class MockBeanConfigurer {
  @Bean
  public ICountryDao countryDao() {
    return Mockito.mock(ICountryDao.class);
  }
}
