package com.himanshu.countryblotter.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.himanshu.countryblotter.domain.deserializer.CountryDeserializer;
import lombok.*;

import java.io.Serializable;

@JsonDeserialize(using = CountryDeserializer.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@Getter
public class Country implements Serializable {
  private String name;
  private String[] topLevelDomain;
  private String[] callingCodes;
  private String capital;
  private String[] borders;
}
