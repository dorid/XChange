package org.knowm.xchange.gateio.service.marketdata;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.dto.account.GateioKline;
import org.knowm.xchange.gateio.dto.account.GateioKlines;
import org.knowm.xchange.gateio.service.GateioKlineDataServiceRaw;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/** @author timmolter */
public class TickerFetchIntegration {

  @Test
  public void tickerFetchTest() throws Exception {

    Exchange exchange = ExchangeFactory.INSTANCE.createExchange(GateioExchange.class);
    MarketDataService marketDataService = exchange.getMarketDataService();
    Ticker ticker = marketDataService.getTicker(new CurrencyPair("BTC", "USDT"));
    System.out.println(ticker.toString());
    assertThat(ticker).isNotNull();
  }

  @Test
  public void klines() throws Exception {
    Exchange exchange = ExchangeFactory.INSTANCE.createExchange(GateioExchange.class);
    GateioKlineDataServiceRaw klineDataServiceRaw = new GateioKlineDataServiceRaw(exchange);
    //按分钟取最近1小时的K线
    GateioKlines kline = klineDataServiceRaw.getKline(new CurrencyPair("BTC", "USDT"), 60, 1);
    System.out.println("kline size = " + kline.getData().size());
    List<GateioKline> klineList = kline.getData().stream().sorted(Comparator.comparing(GateioKline::getTime).reversed()).collect(Collectors.toList());
    System.out.println("kline = " + klineList);
    //最近10分钟的K线
    List<GateioKline> latest10Lines = klineList.subList(0, 10);

  }
}
