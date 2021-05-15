package org.knowm.xchange.gateio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GateioBaseResponse {

  private boolean result;
  private String message;

  protected GateioBaseResponse(
      @JsonProperty("result") final boolean result, @JsonProperty("msg") final String message) {

    this.result = result;
    this.message = message;
  }

  public boolean isResult() {

    return result;
  }

  public String getMessage() {

    return message;
  }
}
