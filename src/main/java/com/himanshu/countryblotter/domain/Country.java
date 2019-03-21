package com.himanshu.countryblotter.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.himanshu.countryblotter.domain.deserializer.CountryDeserializer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@JsonDeserialize(using = CountryDeserializer.class)
public class Country implements Serializable {
  private String name;
  private String[] topLevelDomain;
  private String[] callingCodes;
  private String capital;
  private String[] borders;

  public Country(String name, String[] topLevelDomain, String[] callingCodes, String capital, String[] borders) {
    this.name = name;
    this.topLevelDomain = topLevelDomain;
    this.callingCodes = callingCodes;
    this.capital = capital;
    this.borders = borders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Country country = (Country) o;
    return Objects.equals(name, country.name) &&
          Arrays.equals(topLevelDomain, country.topLevelDomain) &&
          Arrays.equals(callingCodes, country.callingCodes) &&
          Objects.equals(capital, country.capital) &&
          Arrays.equals(borders, country.borders);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(name, capital);
    result = 31 * result + Arrays.hashCode(topLevelDomain);
    result = 31 * result + Arrays.hashCode(callingCodes);
    result = 31 * result + Arrays.hashCode(borders);
    return result;
  }

  @Override
  public String toString() {
    return "Country{" +
          "name='" + name + '\'' +
          ", topLevelDomain=" + Arrays.toString(topLevelDomain) +
          ", callingCodes=" + Arrays.toString(callingCodes) +
          ", capital='" + capital + '\'' +
          ", borders=" + Arrays.toString(borders) +
          '}';
  }

  public String getName() {
    return name;
  }

  public String[] getTopLevelDomain() {
    return topLevelDomain;
  }

  public String[] getCallingCodes() {
    return callingCodes;
  }

  public String getCapital() {
    return capital;
  }

  public String[] getBorders() {
    return borders;
  }
}
