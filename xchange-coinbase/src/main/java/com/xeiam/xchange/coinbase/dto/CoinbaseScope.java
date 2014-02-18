package com.xeiam.xchange.coinbase.dto;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xeiam.xchange.coinbase.dto.CoinbaseScope.CoinbaseScopeDeserializer;

@JsonDeserialize(using = CoinbaseScopeDeserializer.class)
@JsonSerialize(using = EnumLowercaseJsonSerializer.class)
public enum CoinbaseScope {

  ALL, MERCHANT, BALANCE, USER, ADDRESSES, BUTTONS, BUY, SELL, CONTACTS, ORDERS, TRANSACTIONS, SEND, REQUEST, TRANSFERS, RECURRING_PAYMENTS;

  static class CoinbaseScopeDeserializer extends JsonDeserializer<CoinbaseScope> {

    private static final EnumFromStringHelper<CoinbaseScope> FROM_STRING_HELPER = new EnumFromStringHelper<CoinbaseScope>(CoinbaseScope.class);

    @Override
    public CoinbaseScope deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {

      ObjectCodec oc = jsonParser.getCodec();
      JsonNode node = oc.readTree(jsonParser);
      String jsonString = node.textValue();
      return FROM_STRING_HELPER.fromJsonString(jsonString);
    }
  }
}