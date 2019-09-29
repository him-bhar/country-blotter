package com.himanshu.countryblotter.dao;

import com.himanshu.countryblotter.Main;
import com.himanshu.countryblotter.TestDBConfigurer;
import com.himanshu.countryblotter.domain.Country;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = {Main.class}, properties = {"spring.main.allow-bean-definition-overriding=true"})
@ContextConfiguration(classes = {TestDBConfigurer.class})
public class CountryDaoTest {
  private static Logger logger = LoggerFactory.getLogger(CountryDaoTest.class);
  @Autowired
  private ICountryDao countryDao;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  public void testSelectAll() {
    List<Country> countries = countryDao.selectAll();
    Assertions.assertAll(
          () -> Assertions.assertNotNull(countries),
          () -> Assertions.assertTrue(countries.size() == 0)
    );
  }

  @Test
  public void testSave() {
    Country country = Country.builder().name("Test-Country").capital("test-capital").build();
    countryDao.save(country);
    List<Country> countries = countryDao.selectAll();
    Assertions.assertAll(
          () -> Assertions.assertNotNull(countries),
          () -> Assertions.assertTrue(countries.size() == 1)
    );
  }

  @BeforeEach
  public void cleanupDB() {
    logger.info("Deleting all records");
    countryDao.removeAllCountries();
  }

}

