package com.himanshu.countryblotter.web;

import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.service.CountryService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(value = "Querying countries REST API")
@RestController
@RequestMapping("/api/countries")
public class CountryController {

  private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

  private CountryService countryService;

  public CountryController(@Autowired CountryService countryService) {
    this.countryService = countryService;
  }

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
    //return new ResponseEntity(countryFetcher.fetchAllCountries(), HttpStatus.OK);
    //return new ResponseEntity(countryDao.selectAll(), HttpStatus.OK);
    return new ResponseEntity(countryService.getAllCountries(), HttpStatus.OK);
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
    Country country = countryService.getCountryByCode(countryCode);
    if (country != null) {
      return new ResponseEntity(country, HttpStatus.OK);
    } else {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }

  @ApiOperation(value = "Add a new country", httpMethod = "POST", produces = "application/json", response = Boolean.class)
  @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved country"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
  })
  @RequestMapping(method = {RequestMethod.POST})
  public ResponseEntity<Boolean> addCountry(@RequestBody Country country) {
    logger.info("Adding a new country: {}", country);
    countryService.saveCountry(country);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  @ApiOperation(value = "List all countries as map", httpMethod = "GET", produces = "application/json", response = Country.class)
  @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved country"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
  })
  @RequestMapping(value = "/map", method = {RequestMethod.GET})
  public ResponseEntity<List<Map>> listCountriesAsMap() {
    return new ResponseEntity<>(countryService.getAllCountriesAsMap(), HttpStatus.OK);
  }
}
