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

import com.imgtec.creator.petunia.data.api.pojo.Client;
import com.imgtec.creator.petunia.data.api.pojo.Clients;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import okhttp3.HttpUrl;

/**
 *
 */
public class ApiServiceImpl implements ApiService {

  final Context appContext;
  final HttpUrl url;
  final ExecutorService executorService;

  @Inject
  ApiServiceImpl(final Context appContext, final HttpUrl url, final ExecutorService executorService) {
    super();
    this.appContext = appContext;
    this.url = url;
    this.executorService = executorService;
  }

  @Override
  public Clients getClients(Filter<Client> filter) {
    return null;
  }
}
