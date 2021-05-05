package info.bitrich.xchangestream.binance;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class test the Live Subscription/Unsubscription feature of the Binance Api. See
 * https://github.com/binance/binance-spot-api-docs/blob/master/web-socket-streams.md#live-subscribingunsubscribing-to-streams
 *
 * <p>Before this addon, the subscription of the currency pairs required to be at the connection
 * time, so if we wanted to add new currencies to the stream, it was required to disconnect from the
 * stream and reconnect with the new ProductSubscription instance that contains all currency pairs.
 * With the new addon, we can subscribe to new currencies live without disconnecting the stream.
 */
public class MyLiveSubscriptionExample {

  private static final Logger LOG = LoggerFactory.getLogger(MyLiveSubscriptionExample.class);

  public static void main(String[] args) throws InterruptedException {

    ExchangeSpecification spec =
        StreamingExchangeFactory.INSTANCE
            .createExchange(BinanceStreamingExchange.class)
            .getDefaultExchangeSpecification();
    BinanceStreamingExchange exchange =
        (BinanceStreamingExchange) StreamingExchangeFactory.INSTANCE.createExchange(spec);

    // First, we subscribe only for one currency pair at connection time (minimum requirement)
    ProductSubscription subscription =
        ProductSubscription.create()
            .addTrades(CurrencyPair.BTC_USDT)
            .addOrderbook(CurrencyPair.BTC_USDT)
            .build();
    // Note: at connection time, the live subscription is disabled
    exchange.connect(subscription).blockingAwait();



      Disposable subscribe = exchange
              .getStreamingTradeService()
              .getUserTrades()
              .subscribe(trade -> LOG.info("User trade: {}", trade));


    LOG.info(
        "Now all symbols are live unsubscribed (BTC, ETH, LTC & XRP). We will live subscribe to XML/USDT and EOS/BTC...");
    Thread.sleep(5000);
//
//    Disposable xlmDisposable =
//        exchange
//            .getStreamingMarketDataService()
//            .getTrades(CurrencyPair.XLM_USDT)
//            .doOnDispose(
//                () ->
//                    exchange
//                        .getStreamingMarketDataService()
//                        .unsubscribe(CurrencyPair.XLM_USDT, BinanceSubscriptionType.TRADE))
//            .subscribe(trade -> {});
//    Disposable eosDisposable =
//        exchange
//            .getStreamingMarketDataService()
//            .getTrades(CurrencyPair.EOS_BTC)
//            .doOnDispose(
//                () ->
//                    exchange
//                        .getStreamingMarketDataService()
//                        .unsubscribe(CurrencyPair.EOS_BTC, BinanceSubscriptionType.TRADE))
//            .subscribe(trade -> {});
//
//    Thread.sleep(5000);
//    LOG.info("Test finished, we unsubscribe XML/USDT and EOS/BTC from the streams.");
//
//    xlmDisposable.dispose();
//    eosDisposable.dispose();

    exchange.disconnect().blockingAwait();
  }
}
