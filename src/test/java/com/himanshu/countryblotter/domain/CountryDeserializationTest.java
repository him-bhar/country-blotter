package com.himanshu.countryblotter.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.himanshu.countryblotter.domain.deserializer.CountryDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

public class CountryDeserializationTest {

  private static ObjectMapper objectMapper;

  @BeforeAll
  public static void setup() {
    objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addDeserializer(Country.class, new CountryDeserializer(Country.class));
    objectMapper.registerModule(simpleModule);
  }

  @Test
  public void testDesrialization() throws IOException {
    String path = CountryDeserializationTest.class.getResource("/").getFile().concat("/CountryJson.json");
    File f = new File(path);
    String line;
    /*try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
      while((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }*/
    Country country = objectMapper.readValue(f, Country.class);
    Assertions.assertAll(() -> Assertions.assertTrue(country.getName().equals("Colombia")),
          () -> Assertions.assertTrue(country.getCapital().equals("Bogotá")),
          () -> Assertions.assertAll(
                () -> Assertions.assertTrue(country.getTopLevelDomain().length == 2),
                () -> Assertions.assertTrue(country.getTopLevelDomain()[0].equals(".co") || country.getTopLevelDomain()[1].equals(".co")),
                () -> Assertions.assertTrue(country.getTopLevelDomain()[0].equals(".dummy") || country.getTopLevelDomain()[1].equals(".dummy"))
          ),
          () -> Assertions.assertAll(
                () -> Assertions.assertTrue(country.getCallingCodes().length == 1),
                () -> Assertions.assertTrue(country.getCallingCodes()[0].equals("57"))
          ),
          () -> Assertions.assertAll(
                () -> Assertions.assertTrue(country.getBorders().length == 5)
          )
    );
  }
}