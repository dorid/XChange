package org.knowm.xchange.gateio.service;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.gateio.GateioAdapters;
import org.knowm.xchange.gateio.dto.account.GateioKlines;
import org.knowm.xchange.gateio.dto.marketdata.GateioDepth;
import org.knowm.xchange.gateio.dto.marketdata.GateioMarketInfoWrapper;
import org.knowm.xchange.gateio.dto.marketdata.GateioTicker;
import org.knowm.xchange.gateio.dto.marketdata.GateioTradeHistory;

import javax.ws.rs.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GateioKlineDataServiceRaw extends GateioBaseService {

    /**
     * Constructor
     *
     * @param exchange
     */
    public GateioKlineDataServiceRaw(Exchange exchange) {

        super(exchange);
    }


    public GateioKlines getKline(CurrencyPair currencyPair, Integer groupSec, Integer rangHour) throws IOException {

        GateioKlines gateioTicker = bter.getKline(currencyPair.base.getCurrencyCode(), currencyPair.counter.getCurrencyCode(), groupSec, rangHour);

        return handleResponse(gateioTicker);
    }

}
