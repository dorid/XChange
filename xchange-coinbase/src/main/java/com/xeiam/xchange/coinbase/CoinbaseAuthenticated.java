package com.xeiam.xchange.coinbase;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import si.mazi.rescu.ParamsDigest;

import com.xeiam.xchange.coinbase.dto.CoinbaseBaseResponse;
import com.xeiam.xchange.coinbase.dto.CoinbaseUser;
import com.xeiam.xchange.coinbase.dto.CoinbaseUsers;
import com.xeiam.xchange.coinbase.dto.account.CoinbaseAccountChanges;
import com.xeiam.xchange.coinbase.dto.account.CoinbaseAddress;
import com.xeiam.xchange.coinbase.dto.account.CoinbaseAddressCallback;
import com.xeiam.xchange.coinbase.dto.account.CoinbaseAddresses;
import com.xeiam.xchange.coinbase.dto.account.CoinbaseContacts;
import com.xeiam.xchange.coinbase.dto.marketdata.CoinbaseAmount;
import com.xeiam.xchange.coinbase.dto.merchant.CoinbaseButton;
import com.xeiam.xchange.coinbase.dto.merchant.CoinbaseOrder;
import com.xeiam.xchange.coinbase.dto.merchant.CoinbaseOrders;
import com.xeiam.xchange.coinbase.dto.trade.CoinbaseTransaction;
import com.xeiam.xchange.coinbase.dto.trade.CoinbaseTransactions;
import com.xeiam.xchange.coinbase.dto.trade.CoinbaseTransfers;

/**
 * @author jamespedwards42
 */
@Path("api/v1")
public interface CoinbaseAuthenticated extends Coinbase {

  @GET
  @Path("users")
  CoinbaseUsers getUsers(@HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer, @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("users/{userId}")
  CoinbaseUser updateUser(@PathParam("userId") String userId, CoinbaseUser user, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer, @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @POST
  @Path("tokens/redeem")
  CoinbaseBaseResponse redeemToken(@QueryParam("token_id") String tokenId, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @GET
  @Path("account/balance")
  CoinbaseAmount getBalance(@HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer, @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @GET
  @Path("account/receive_address")
  CoinbaseAddress getReceiveAddress(@HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer, @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("account/generate_receive_address")
  CoinbaseAddress generateReceiveAddress(CoinbaseAddressCallback callbackUrl, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @GET
  @Path("account_changes")
  CoinbaseAccountChanges getAccountChanges(@QueryParam("page") Integer page, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @GET
  @Path("addresses")
  CoinbaseAddresses getAddresses(@QueryParam("page") Integer page, @QueryParam("limit") Integer limit, @QueryParam("query") String query, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @GET
  @Path("contacts") 
  CoinbaseContacts getContacts(@QueryParam("page") Integer page, @QueryParam("num_pages") Integer limit, @QueryParam("query") String query, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  
  
  @GET
  @Path("transfers") 
  CoinbaseTransfers getTransfers(@QueryParam("page") Integer page, @QueryParam("limit") Integer limit, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @GET
  @Path("transactions") 
  CoinbaseTransactions getTransactions(@QueryParam("page") Integer page, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @GET
  @Path("transactions/{transactionId}") 
  CoinbaseTransaction getTransactionDetails(@PathParam("transactionId") String transactionId, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("transactions/request_money")
  CoinbaseTransaction requestMoney(CoinbaseTransaction transactionRequest, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("transactions/send_money")
  CoinbaseTransaction sendMoney(CoinbaseTransaction transactionRequest, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @PUT
  @Path("transactions/{transactionId}/resend_request")
  CoinbaseBaseResponse resendRequest(@PathParam("transactionId") String transactionId, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @PUT
  @Path("transactions/{transactionId}/complete_request")
  CoinbaseTransaction completeRequest(@PathParam("transactionId") String transactionId, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @DELETE
  @Path("transactions/{transactionId}/cancel_request")
  CoinbaseBaseResponse cancelRequest(@PathParam("transactionId") String transactionId, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("buttons")
  CoinbaseButton createButton(CoinbaseButton button, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @GET
  @Path("orders") 
  CoinbaseOrders getOrders(@QueryParam("page") Integer page, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @GET
  @Path("orders/{orderId}") 
  CoinbaseOrder getOrder(@PathParam("orderId") String orderId, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;
  
  @POST
  @Path("buttons/{code}/create_order")
  CoinbaseOrder createOrder(@PathParam("code") String code, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("orders")
  CoinbaseOrder createOrder(CoinbaseButton button, @HeaderParam("ACCESS_KEY") String apiKey, @HeaderParam("ACCESS_SIGNATURE") ParamsDigest signer,
      @HeaderParam("ACCESS_NONCE") long nonce) throws IOException;

}
