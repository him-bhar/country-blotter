package com.himanshu.countryblotter.web;

import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.fetcher.ICountryFetcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CountryControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ICountryFetcher<Country> countryFetcher;

  @Test
  public void testGetCountryByCode() throws Exception {
    Mockito.when(countryFetcher.fetchCountry("in"))
          .thenReturn(new Country("India", new String[]{".in"}, new String[]{"91"}, "delhi", new String[]{"pak", "ban"}));
    mockMvc
          .perform(
                MockMvcRequestBuilders.get("/countries/in").contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)
          )
          .andExpect(
                MockMvcResultMatchers.jsonPath("name", Matchers.equalTo("India"))
          );
  }

  @Test
  public void testGetCountryByInvalidCode() throws Exception {
    Mockito.when(countryFetcher.fetchCountry("zz")).thenReturn(null);
    mockMvc
          .perform(
                MockMvcRequestBuilders.get("/countries/zz").contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testGetAllCountries() throws Exception {
    List<Country> countries = new ArrayList<>();
    countries.add(new Country("India", new String[]{".in"}, new String[]{"91"}, "delhi", new String[]{"pak", "ban"}));
    countries.add(new Country("United States of America", new String[]{".us"}, new String[]{"1"}, "washington", new String[]{"mexico", "canada"}));
    Mockito.when(countryFetcher.fetchAllCountries()).thenReturn(countries);
    mockMvc
          .perform(
                MockMvcRequestBuilders.get("/countries/all").contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)
          )
          .andExpect(
                MockMvcResultMatchers.jsonPath("$.[?(@.name!='')].name", Matchers.containsInAnyOrder("India", "United States of America"))
          );
  }

  @Test
  public void testGetAllCountriesWithError() throws Exception {
    Mockito.when(countryFetcher.fetchAllCountries()).thenThrow(new NullPointerException("Blah blah blah!"));
    mockMvc
          .perform(
                MockMvcRequestBuilders.get("/countries/all").contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isInternalServerError())
          .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)
          );
  }
}
