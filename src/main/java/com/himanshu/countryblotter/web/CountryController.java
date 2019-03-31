package com.himanshu.countryblotter.web;

import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.fetcher.ICountryFetcher;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "Querying countries REST API")
@RestController
@RequestMapping("/countries")
public class CountryController {

  private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

  @Autowired
  private ICountryFetcher<Country> countryFetcher;

  @ApiOperation(value = "List all countries", httpMethod = "GET", produces = "application/json", response = List.class
        /*, authorizations = {
              @Authorization(value = "basic")
        }*/
  )
  @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
  })
  @RequestMapping(path = {"/all"}, method = {RequestMethod.GET})
  public ResponseEntity<List<Country>> getAllCountries() {
    return new ResponseEntity(countryFetcher.fetchAllCountries(), HttpStatus.OK);
  }

  @ApiOperation(value = "Get a country by country-code", httpMethod = "GET", produces = "application/json", response = Country.class)
  @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved country"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
  })
  @RequestMapping(path = {"/{country-code}"}, method = {RequestMethod.GET})
  public ResponseEntity<List<Country>> getCountryByCode(@ApiParam(value = "Country code", required = true) @PathVariable("country-code") String countryCode) {
    logger.info("Looking for country: "+countryCode);
    Country country = countryFetcher.fetchCountry(countryCode);
    if (country != null) {
      return new ResponseEntity(countryFetcher.fetchCountry(countryCode), HttpStatus.OK);
    } else {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}
