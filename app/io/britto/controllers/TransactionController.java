package io.britto.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.britto.dto.Transaction;
import io.britto.persistence.TransactionPersistence;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by tfulton on 7/4/16.
 */
public class TransactionController
        extends Controller {

    @Inject
    private TransactionPersistence transactionPersistence;

    protected Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @BodyParser.Of(BodyParser.Json.class)
    public Result createTransaction(String merchantId) {

        JsonNode json = request().body().asJson();
        Transaction data = gson.fromJson(json.toString(), Transaction.class);
        transactionPersistence.save(data.getKey(), data);
        return ok();
    }
}
