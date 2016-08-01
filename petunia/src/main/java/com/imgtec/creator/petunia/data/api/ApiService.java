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

import com.imgtec.creator.petunia.data.api.pojo.Client;
import com.imgtec.creator.petunia.data.api.pojo.Clients;
import com.imgtec.creator.petunia.data.api.pojo.Delta;
import com.imgtec.creator.petunia.data.api.pojo.Measurements;

import java.io.IOException;
import java.util.Date;

/**
 *
 */
public interface ApiService {

  interface Filter<T> {
    boolean accept(final Client client);
  }

  Clients getClients(Filter<Client> filter) throws IOException;

  Measurements getMeasurements(String clientId, Date from, Date to) throws IOException;

  Delta getDelta(String clientId) throws IOException;

  void setDelta(String clientId, double delta) throws IOException;
}
