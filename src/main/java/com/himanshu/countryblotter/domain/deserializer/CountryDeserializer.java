package com.himanshu.countryblotter.domain.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.himanshu.countryblotter.domain.Country;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CountryDeserializer extends StdDeserializer<Country> {

  public CountryDeserializer() {
    super(Country.class);
  }

  public CountryDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Country deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    String countryName = node.get("name").asText();
    String capital = node.get("capital").asText();
    List<String> domains = extractFromArrayNodeWithKey(node, "topLevelDomain");
    List<String> callingCodes = extractFromArrayNodeWithKey(node, "callingCodes");
    List<String> borders = extractFromArrayNodeWithKey(node, "borders");
    return new Country(countryName,
          domains.toArray(new String[domains.size()]),
          callingCodes.toArray(new String[callingCodes.size()]),
          capital,
          borders.toArray(new String[borders.size()]));
  }

  private List<String> extractFromArrayNodeWithKey(JsonNode node, String nodeKey) {
    List<String> domains = new ArrayList<>();
    if (node.get(nodeKey).isArray()) {
      ArrayNode arrayNode = (ArrayNode) node.get(nodeKey);
      for (int i=0; i<arrayNode.size(); i++) {
        domains.add(arrayNode.get(i).asText());
      }
    }
    return domains;
  }
}
