/*
 * <b>Copyright 2016 by Imagination Technologies Limited
 * and/or its affiliated group companies.</b>
 *
 * All rights reserved.  No part of this software, either
 * material or conceptual may be copied or distributed,
 * transmitted, transcribed, stored in a retrieval system
 * or translated into any human or computer language in any
 * form by any means, electronic, mechanical, manual or
 * other-wise, or disclosed to the third parties without the
 * express written permission of Imagination Technologies
 * Limited, Home Park Estate, Kings Langley, Hertfordshire,
 * WD4 8LZ, U.K.
 */

package com.imgtec.creator.petunia.data.api;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.imgtec.creator.petunia.data.api.pojo.Client;
import com.imgtec.creator.petunia.data.api.pojo.Clients;
import com.imgtec.creator.petunia.data.api.pojo.Measurements;
import com.imgtec.creator.petunia.data.api.requests.GetRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 *
 */
public class ApiServiceImpl implements ApiService {

  final Context appContext;
  final HttpUrl url;
  final OkHttpClient client;
  final ExecutorService executorService;

  @Inject
  ApiServiceImpl(final Context appContext, final HttpUrl url, final OkHttpClient client,
                 final ExecutorService executorService) {
    super();
    this.appContext = appContext;
    this.url = url;
    this.client = client;
    this.executorService = executorService;
  }

  @Override
  public Clients getClients(Filter<Client> filter) throws IOException {

    final String urlString = url.toString()+"clients";  //FIXME: should be taken from API via 'rel = clients'

    final Clients clients = new GetRequest<Clients>(urlString).execute(client, Clients.class);
    if (filter != null) {
      List<Client> clientList = new ArrayList<>();
      for (Client c: clients.getItems()) {
        if (filter.accept(c)) {
          clientList.add(c);
        }
      }
      clients.setItems(clientList);
    }
    return clients;
  }

  @Override
  public Measurements getMeasurements(final String clientId, Date from, Date to) throws IOException {

    Clients clients = getClients(new Filter<Client>() {
      @Override
      public boolean accept(Client client) {
        return client.getData().get(0).getId().equals(clientId);
      }
    });

    final Client client = clients.getItems().get(0);

    //FIXME: dummy implementation
    Measurements measurements = new GsonBuilder()
        .create()
        .fromJson(MEASUREMENTS, Measurements.class);

    return measurements;
  }

  static final String MEASUREMENTS = "{\n" +
      "   \"PageInfo\":{\n" +
      "      \"TotalCount\":1000,\n" +
      "      \"ItemsCount\":100,\n" +
      "      \"StartIndex\":0\n" +
      "   },\n" +
      "   \"Items\":[\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-15T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-15T09:27:21.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-15T09:28:22.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-15T09:29:23.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-15T09:30:24.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-15T09:31:25.907Z\"\n" +
      "      },\n" +

      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-16T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-17T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-18T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-19T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-20T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-21T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-22T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-23T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-24T09:26:20.907Z\"\n" +
      "      },\n" +
      "      {\n" +
      "         \"Links\":[\n" +
      "            {\t\"rel\":\"self\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"update\", \"href\":\"/clients/{clientId}/data/{id}\" },\n" +
      "            {   \"rel\":\"remove\", \"href\":\"/clients/{clientId}/data/{id}\" }\n" +
      "         ],\n" +
      "         \"id\":\"577d083a0b073b294d34d546\",\n" +
      "         \"value\":\"20.5\",\n" +
      "         \"timestamp\":\"2016-07-25T09:26:20.907Z\"\n" +
      "      }\n" +
      "   ]\n" +
      "}";
}
