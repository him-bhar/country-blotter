package com.himanshu.countryblotter.web;

import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.fetcher.ICountryFetcher;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser(username = "test")
public class CountryControllerTest {
  @Autowired
  private MockMvc mockMvc;

  /*@Autowired
  private WebApplicationContext wac;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(SecurityMockMvcConfigurers.springSecurity())
          .build();
  }*/

  @MockBean
  @Qualifier("fileBasedCountryFetcher")
  private ICountryFetcher<Country> countryFetcher;

  @Test
  public void testGetCountryByCode() throws Exception {
    //setupCredentialsAndAuthority("ROLE_TEST");
    Mockito.when(countryFetcher.fetchCountry("IN"))
          .thenReturn(new Country("India", "IN", new String[]{".in"}, new String[]{"91"}, "delhi", new String[]{"pak", "ban"}));
    mockMvc
          .perform(
                MockMvcRequestBuilders.get("/api/countries/IN").contentType(MediaType.APPLICATION_JSON)
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
                MockMvcRequestBuilders.get("/api/countries/zz").contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void testGetAllCountries() throws Exception {
    List<Country> countries = new ArrayList<>();
    countries.add(new Country("India", "IN", new String[]{".in"}, new String[]{"91"}, "delhi", new String[]{"pak", "ban"}));
    countries.add(new Country("United States of America", "US", new String[]{".us"}, new String[]{"1"}, "washington", new String[]{"mexico", "canada"}));
    Mockito.when(countryFetcher.fetchAllCountries()).thenReturn(countries);
    mockMvc
          .perform(
                MockMvcRequestBuilders.get("/api/countries/all").contentType(MediaType.APPLICATION_JSON)
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

  @Ignore
  @Test
  @DisplayName("Throw error when remote service has issues")
  public void testGetAllCountriesWithError() throws Exception {
    Mockito.when(countryFetcher.fetchAllCountries()).thenThrow(new NullPointerException("Blah blah blah!"));
    mockMvc
          .perform(
                MockMvcRequestBuilders.get("/api/countries/all").contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(MockMvcResultHandlers.print())
          .andExpect(MockMvcResultMatchers.status().isInternalServerError())
          .andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)
          );
  }

  private void setupCredentialsAndAuthority(String role) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role));
    Authentication auth = new UsernamePasswordAuthenticationToken("Mr. Big", "password", authorities);

    SecurityContextImpl securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(auth);
    SecurityContextHolder.setContext(securityContext);
  }
}
