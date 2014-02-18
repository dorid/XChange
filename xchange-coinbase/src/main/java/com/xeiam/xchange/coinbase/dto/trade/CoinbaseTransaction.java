package com.xeiam.xchange.coinbase.dto.trade;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.joda.money.BigMoney;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.xeiam.xchange.coinbase.dto.CoinbaseBaseResponse;
import com.xeiam.xchange.coinbase.dto.CoinbaseUser;
import com.xeiam.xchange.coinbase.dto.CoinbaseUser.CoinbaseUserInfo;
import com.xeiam.xchange.coinbase.dto.EnumFromStringHelper;
import com.xeiam.xchange.coinbase.dto.marketdata.CoinbaseAmount;
import com.xeiam.xchange.coinbase.dto.trade.CoinbaseTransaction.CoinbaseTransactionStatus.CoinbaseTransactionStatusDeserializer;
import com.xeiam.xchange.currency.MoneyUtils;
import com.xeiam.xchange.utils.jackson.ISO8601DateDeserializer;

public class CoinbaseTransaction extends CoinbaseBaseResponse implements CoinbaseTransactionInfo {

  @JsonProperty("transaction")
  private final CoinbaseTransactionInfo transaction;

  private CoinbaseTransaction(@JsonProperty("transaction") final CoinbaseTransactionInfoResult transaction, @JsonProperty("success") final boolean success,
      @JsonProperty("errors") final List<String> errors) {

    super(success, errors);
    this.transaction = transaction;
  }

  public CoinbaseTransaction(final CoinbaseTransactionInfo transaction) {

    super(true, null);
    this.transaction = transaction;
  }

  @Override
  public String getId() {

    return transaction.getId();
  }

  @Override
  public Date getCreatedAt() {

    return transaction.getCreatedAt();
  }

  @Override
  public CoinbaseAmount getAmount() {

    return transaction.getAmount();
  }

  @Override
  public boolean isRequest() {

    return transaction.isRequest();
  }

  @Override
  public CoinbaseTransactionStatus getStatus() {

    return transaction.getStatus();
  }

  @Override
  public CoinbaseUser getSender() {

    return transaction.getSender();
  }

  @Override
  public CoinbaseUser getRecipient() {

    return transaction.getRecipient();
  }

  @Override
  public String getRecipientAddress() {

    return transaction.getRecipientAddress();
  }

  public String getNotes() {

    return transaction.getNotes();
  }

  public String getTransactionHash() {

    return transaction.getTransactionHash();
  }

  public String getIdempotencyKey() {

    return transaction.getIdempotencyKey();
  }

  @Override
  public String toString() {

    return "CoinbaseTransaction [transaction=" + transaction + "]";
  }

  @JsonDeserialize(using = CoinbaseTransactionStatusDeserializer.class)
  public enum CoinbaseTransactionStatus {

    PENDING, COMPLETE;

    static class CoinbaseTransactionStatusDeserializer extends JsonDeserializer<CoinbaseTransactionStatus> {

      private static final EnumFromStringHelper<CoinbaseTransactionStatus> FROM_STRING_HELPER = new EnumFromStringHelper<CoinbaseTransactionStatus>(CoinbaseTransactionStatus.class);

      @Override
      public CoinbaseTransactionStatus deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        String jsonString = node.textValue();
        return FROM_STRING_HELPER.fromJsonString(jsonString);
      }
    }
  }

  private abstract static class CoinbaseTransactionRequest implements CoinbaseTransactionInfo {

    @JsonProperty("amount_string")
    private final String amountString;
    @JsonProperty("amount_currency_iso")
    private final String currencyIso;
    @JsonProperty("notes")
    protected String notes;

    private CoinbaseTransactionRequest(final String currency, final String amountString) {

      this.amountString = amountString;
      this.currencyIso = currency;
    }

    @Override
    public String getId() {

      return null;
    }

    @Override
    public Date getCreatedAt() {

      return null;
    }

    @Override
    public CoinbaseAmount getAmount() {

      return new CoinbaseAmount(MoneyUtils.parse(currencyIso + " " + amountString));
    }

    @Override
    public boolean isRequest() {

      return true;
    }

    @Override
    public CoinbaseTransactionStatus getStatus() {

      return null;
    }

    @Override
    public CoinbaseUser getSender() {

      return null;
    }

    @Override
    public CoinbaseUser getRecipient() {

      return null;
    }
    
    @Override
    public String getRecipientAddress() {

      return null;
    }

    @Override
    public String getNotes() {

      return notes;
    }
    
    public abstract CoinbaseTransactionRequest withNotes(final String notes);

    @Override
    public String getTransactionHash() {

      return null;
    }

    @Override
    public String getIdempotencyKey() {

      return null;
    }
  }

  public static class CoinbaseRequestMoneyRequest extends CoinbaseTransactionRequest {

    @JsonProperty("from")
    private final String from;

    public CoinbaseRequestMoneyRequest(final String from, String currency, String amountString) {

      super(currency, amountString);
      this.from = from;
    }

    public CoinbaseRequestMoneyRequest(final String from, final BigMoney amount) {

      this(from, amount.getCurrencyUnit().getCurrencyCode(), amount.getAmount().toPlainString());
    }
    
    public String getFrom() {

      return from;
    }

    @Override
    public CoinbaseRequestMoneyRequest withNotes(String notes) {

      this.notes = notes;
      return this;
    }
  }

  public static class CoinbaseSendMoneyRequest extends CoinbaseTransactionRequest {

    @JsonProperty("to")
    private final String to;
    @JsonProperty("user_fee")
    private String userFee;
    @JsonProperty("referrer_id")
    private String referrerId;
    @JsonProperty("idem")
    private String idempotencyKey;
    @JsonProperty("instant_buy")
    private boolean instantBuy;
    
    public CoinbaseSendMoneyRequest(final String to, String currency, String amountString) {

      super(currency, amountString);
      this.to = to;
    }

    public CoinbaseSendMoneyRequest(final String to, final BigMoney amount) {

      this(to, amount.getCurrencyUnit().getCurrencyCode(), amount.getAmount().toPlainString());
    }

    public String getTo() {

      return to;
    }
    
    @Override
    public CoinbaseSendMoneyRequest withNotes(String notes) {

      this.notes = notes;
      return this;
    }
    
    public String getUserFee() {
      
      return userFee;
    }

    public CoinbaseSendMoneyRequest withUserFee(final String userFee) {
      
      this.userFee = userFee;
      return this;
    }
    
    public String getReferrerId() {
    
      return referrerId;
    }

    public CoinbaseSendMoneyRequest withReferrerId(final String referrerId) {
      
      this.referrerId = referrerId;
      return this;
    }
    
    public String getIdempotencyKey() {
    
      return idempotencyKey;
    }

    public CoinbaseSendMoneyRequest withIdempotencyKey(final String idempotencyKey) {
      
      this.idempotencyKey = idempotencyKey;
      return this;
    }
    
    public boolean isInstantBuy() {
    
      return instantBuy;
    }
    
    public CoinbaseSendMoneyRequest withInstantBuy(final boolean instantBuy) {
      
      this.instantBuy = instantBuy;
      return this;
    }

    @Override
    public String toString() {

      return "CoinbaseSendMoneyRequest [to=" + to + ", userFee=" + userFee + ", referrerId=" + referrerId + ", idempotencyKey=" + idempotencyKey + ", instantBuy=" + instantBuy + "]";
    }
  }

  private static class CoinbaseTransactionInfoResult implements CoinbaseTransactionInfo {

    private final String id;
    private final Date createdAt;
    private final CoinbaseAmount amount;
    private final boolean request;
    private final CoinbaseTransactionStatus status;
    private final CoinbaseUser sender;
    private final CoinbaseUser recipient;
    private final String recipientAddress;
    private final String notes;
    private final String transactionHash;
    private final String idempotencyKey;

    private CoinbaseTransactionInfoResult(@JsonProperty("id") final String id, @JsonProperty("created_at") @JsonDeserialize(using=ISO8601DateDeserializer.class) final Date createdAt, @JsonProperty("amount") final CoinbaseAmount amount,
        @JsonProperty("request") final boolean request, @JsonProperty("status") final CoinbaseTransactionStatus status, @JsonProperty("sender") final CoinbaseUserInfo sender,
        @JsonProperty("recipient") final CoinbaseUserInfo recipient, @JsonProperty("recipient_address") final String recipientAddress, @JsonProperty("notes") final String notes,
        @JsonProperty("hsh") final String transactionHash, @JsonProperty("idem") final String idempotencyKey) {

      this.id = id;
      this.createdAt = createdAt;
      this.amount = amount;
      this.request = request;
      this.status = status;
      this.sender = new CoinbaseUser(sender);
      this.recipient = new CoinbaseUser(recipient);
      this.recipientAddress = recipientAddress;
      this.notes = notes;
      this.transactionHash = transactionHash;
      this.idempotencyKey = idempotencyKey;
    }

    @Override
    public String getId() {

      return id;
    }

    @Override
    public Date getCreatedAt() {

      return createdAt;
    }

    @Override
    public CoinbaseAmount getAmount() {

      return amount;
    }

    @Override
    public boolean isRequest() {

      return request;
    }

    @Override
    public CoinbaseTransactionStatus getStatus() {

      return status;
    }

    @Override
    public CoinbaseUser getSender() {

      return sender;
    }

    @Override
    public CoinbaseUser getRecipient() {

      return recipient;
    }

    @Override
    public String getRecipientAddress() {

      return recipientAddress;
    }

    @Override
    public String getNotes() {

      return notes;
    }

    @Override
    public String getTransactionHash() {

      return transactionHash;
    }

    @Override
    public String getIdempotencyKey() {

      return idempotencyKey;
    }

    @Override
    public String toString() {

      return "CoinbaseTransactionInfoResult [id=" + id + ", createdAt=" + createdAt + ", amount=" + amount + ", request=" + request + ", status=" + status + ", sender=" + sender + ", recipient="
          + recipient + ", recipientAddress=" + recipientAddress + ", notes=" + notes + ", transactionHash=" + transactionHash + ", idempotencyKey=" + idempotencyKey + "]";
    }
  }
}
