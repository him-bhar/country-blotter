package com.himanshu.countryblotter.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class CountryControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testGetAllCountries() throws Exception {
    mockMvc
          .perform(
                MockMvcRequestBuilders.get("/countries/in").contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(MockMvcResultHandlers.print())
          .andExpect(
                MockMvcResultMatchers.jsonPath("name", Matchers.equalTo("India"))
          ).andExpect(
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)
          );
  }
}
