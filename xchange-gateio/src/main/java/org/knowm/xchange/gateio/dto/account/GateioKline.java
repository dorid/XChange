package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonDeserialize(using = GateioKline.GateioKlineDeserializer.class)
public class GateioKline {

  private Long time;
  private BigDecimal volume;
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal open;

  private GateioKline(Long time, BigDecimal volume, BigDecimal close, BigDecimal high, BigDecimal low, BigDecimal open) {

    this.time = time;
    this.volume = volume;
    this.close = close;
    this.high = high;
    this.low = low;
    this.open = open;
  }

  static class GateioKlineDeserializer extends JsonDeserializer<GateioKline> {

    @Override
    public GateioKline deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {

      final ObjectCodec oc = jp.getCodec();
      final JsonNode tickerNode = oc.readTree(jp);

      final Long time = Long.valueOf(tickerNode.path(0).asText());
      final BigDecimal volume = new BigDecimal(tickerNode.path(1).asText());
      final BigDecimal close = new BigDecimal(tickerNode.path(2).asText());
      final BigDecimal high = new BigDecimal(tickerNode.path(3).asText());
      final BigDecimal low = new BigDecimal(tickerNode.path(4).asText());
      final BigDecimal open = new BigDecimal(tickerNode.path(5).asText());

      return new GateioKline(time, volume, close, high, low, open);
    }
  }
}
