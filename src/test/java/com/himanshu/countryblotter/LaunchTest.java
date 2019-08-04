package com.himanshu.countryblotter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
public class LaunchTest {
  @DisplayName("Test Spring @Autowired Integration")
  @Test
  void testGet() {
    Assertions.assertTrue(true);
  }
}
