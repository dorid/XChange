package org.knowm.xchange.gateio.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knowm.xchange.gateio.dto.GateioBaseResponse;

import java.util.List;

@Data
@NoArgsConstructor
public class GateioKlines extends GateioBaseResponse {

  private  List<GateioKline> data;
  private  String elapsed;

  /**
   * Constructor
   */
  private GateioKlines(
          @JsonProperty("data") List<GateioKline> data,
          @JsonProperty("result") boolean result,
          @JsonProperty("elapsed") String elapsed) {

    super(result, null);
    this.data = data;
    this.elapsed = elapsed;
  }
}
