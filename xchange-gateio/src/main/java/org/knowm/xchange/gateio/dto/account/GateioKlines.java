package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.gateio.dto.GateioBaseResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GateioKlines extends GateioBaseResponse {

  private final List<List<BigDecimal>> data;

  /**
   * Constructor
   *
   */
  public GateioKlines(
      @JsonProperty("data") List<List<BigDecimal>> data,
      @JsonProperty("result") boolean result,
      @JsonProperty("message") final String message) {

    super(result, message);

    this.data = data == null ? new ArrayList<>() : data;
  }


  @Override
  public String toString() {

    return "BTERAccountInfoReturn [data=" + data + "]";
  }
}
