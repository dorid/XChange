package info.bitrich.xchangestream.binance;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/** Created by Lukas Zaoralek on 15.11.17. */
public class MyBinanceManualExample {
  private static final Logger LOG = LoggerFactory.getLogger(BinanceManualExample.class);

  private static volatile BigDecimal btcPrePrice = null;
  private static volatile BigDecimal bnbPrePrice = null;


  public static void main(String[] args) throws InterruptedException {
    // Far safer than temporarily adding these to code that might get committed to VCS
    String apiKey = System.getProperty("binance-api-key");
    String apiSecret = System.getProperty("binance-api-secret");

    ExchangeSpecification spec =
            StreamingExchangeFactory.INSTANCE
                    .createExchange(BinanceStreamingExchange.class)
                    .getDefaultExchangeSpecification();
    spec.setApiKey(apiKey);
    spec.setSecretKey(apiSecret);
    BinanceStreamingExchange exchange =
            (BinanceStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(spec);

    ProductSubscription subscription =
            ProductSubscription.create()
                    .addTicker(CurrencyPair.BTC_USDT)
                    .addTicker(new CurrencyPair(Currency.BNB, Currency.USDT))
                    .build();

    exchange.connect(subscription).blockingAwait();

    LOG.info("Subscribing public channels");

    Disposable tickers =
            exchange
                    .getStreamingMarketDataService()
                    .getTicker(CurrencyPair.BTC_USDT)
                    .subscribe(
                            ticker -> {
//                              LOG.info("BTC-USDT price : {}", ticker.getLast());
                              if (btcPrePrice == null) {
                                btcPrePrice = ticker.getLast();
                              } else {
                                if (btcPrePrice.compareTo(ticker.getLast()) > 0) {
                                  LOG.info("[price] BTC 0,  {}", btcPrePrice .subtract(ticker.getLast()));
                                } else if (btcPrePrice.compareTo(ticker.getLast()) < 0) {
                                  LOG.info("[price] BTC 1,  {}", btcPrePrice .subtract(ticker.getLast()));
                                }
                                btcPrePrice = ticker.getLast();
                              }
                            },
                            throwable -> LOG.error("ERROR in getting ticker: ", throwable));

    Disposable bnb =
            exchange
                    .getStreamingMarketDataService()
                    .getTicker(new CurrencyPair(Currency.BNB, Currency.USDT))
                    .subscribe(
                            ticker -> {
//                              LOG.info("BNB-USDT price : {}", ticker.getLast());
                              if (bnbPrePrice == null) {
                                bnbPrePrice = ticker.getLast();
                              } else {
                                if (bnbPrePrice.compareTo(ticker.getLast()) > 0) {
                                  LOG.info("[price] BNB 0,  {}", bnbPrePrice .subtract(ticker.getLast()));
                                } else if (bnbPrePrice.compareTo(ticker.getLast()) < 0) {
                                  LOG.info("[price] BNB 1,  {}", bnbPrePrice .subtract(ticker.getLast()));
                                }
                                bnbPrePrice = ticker.getLast();
                              }
                            },
                            throwable -> LOG.error("ERROR in getting ticker: ", throwable));

    Thread.sleep(1000000);

    tickers.dispose();


    exchange.disconnect().blockingAwait();
  }

  private static Disposable orderbooks(StreamingExchange exchange, String identifier) {
    return exchange
            .getStreamingMarketDataService()
            .getOrderBook(CurrencyPair.LTC_BTC)
            .subscribe(
                    orderBook -> {
                      LOG.info(
                              "Order Book ({}): askDepth={} ask={} askSize={} bidDepth={}. bid={}, bidSize={}",
                              identifier,
                              orderBook.getAsks().size(),
                              orderBook.getAsks().get(0).getLimitPrice(),
                              orderBook.getAsks().get(0).getRemainingAmount(),
                              orderBook.getBids().size(),
                              orderBook.getBids().get(0).getLimitPrice(),
                              orderBook.getBids().get(0).getRemainingAmount());
                    },
                    throwable -> LOG.error("ERROR in getting order book: ", throwable));
  }

  private static Disposable orderbooksIncremental(
          BinanceStreamingExchange exchange, String identifier) {
    return exchange
            .getStreamingMarketDataService()
            .getOrderBookUpdates(CurrencyPair.LTC_BTC)
            .subscribe(
                    level -> LOG.info("Order Book Level update({}): {}", identifier, level),
                    throwable -> LOG.error("ERROR in getting order book: ", throwable));
  }
}
