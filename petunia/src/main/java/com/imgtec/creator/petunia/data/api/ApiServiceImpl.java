/*
 * <b>Copyright (c) 2016, Imagination Technologies Limited and/or its affiliated group companies
 *  and/or licensors. </b>
 *
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are permitted
 *  provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *      and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *      conditions and the following disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *
 *  3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *      endorse or promote products derived from this software without specific prior written
 *      permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 *  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 *  WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.imgtec.creator.petunia.data.api;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.imgtec.creator.petunia.data.api.pojo.Client;
import com.imgtec.creator.petunia.data.api.pojo.Clients;
import com.imgtec.creator.petunia.data.api.pojo.Measurements;
import com.imgtec.creator.petunia.data.api.requests.GetData;
import com.imgtec.creator.petunia.data.api.requests.GetRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 *
 */
public class ApiServiceImpl implements ApiService {

  private static final java.text.SimpleDateFormat dateFormatter =
      new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  final Context appContext;
  final HttpUrl url;
  final OkHttpClient client;
  final ExecutorService executorService;

  static {
    dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

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
        return client.getData().getId().equals(clientId);
      }
    });

    final Client c = clients.getItems().get(0);
    Measurements measurements;

//    String url = "http://kiwano.herokuapp.com" + c.getLinkByRel("data").getHref();
//    Measurements measurements = new GetData(url,
//        dateFormatter.format(from), dateFormatter.format(to)).execute(client, Measurements.class);

    //FIXME: dummy implementation
    measurements = new GsonBuilder()
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
      "   { " +
      "     \"data\": {\n" +
      "            \"_id\": \"577d08840b073b294d34d547\",\n" +
      "            \"data\": \"17\",\n" +
      "            \"timestamp\": \"2016-07-29T08:56:50.509Z\"\n" +
      "        },\n" +
      "        \"links\": [{\n" +
      "            \"rel\": \"self\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }, {\n" +
      "            \"rel\": \"update\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }, {\n" +
      "            \"rel\": \"remove\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }]" +
      "   },\n" +
      "   { " +
      "     \"data\": {\n" +
      "            \"_id\": \"577d08840b073b294d34d547\",\n" +
      "            \"data\": \"17\",\n" +
      "            \"timestamp\": \"2016-07-29T08:57:51.509Z\"\n" +
      "        },\n" +
      "        \"links\": [{\n" +
      "            \"rel\": \"self\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }, {\n" +
      "            \"rel\": \"update\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }, {\n" +
      "            \"rel\": \"remove\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }]" +
      "   },\n" +
      "   { " +
      "     \"data\": {\n" +
      "            \"_id\": \"577d08840b073b294d34d547\",\n" +
      "            \"data\": \"17\",\n" +
      "            \"timestamp\": \"2016-07-29T08:58:52.509Z\"\n" +
      "        },\n" +
      "        \"links\": [{\n" +
      "            \"rel\": \"self\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }, {\n" +
      "            \"rel\": \"update\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }, {\n" +
      "            \"rel\": \"remove\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }]" +
      "   },\n" +
      "   { " +
      "     \"data\": {\n" +
      "            \"_id\": \"577d08840b073b294d34d547\",\n" +
      "            \"data\": \"17\",\n" +
      "            \"timestamp\": \"2016-07-29T08:59:53.509Z\"\n" +
      "        },\n" +
      "        \"links\": [{\n" +
      "            \"rel\": \"self\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }, {\n" +
      "            \"rel\": \"update\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }, {\n" +
      "            \"rel\": \"remove\",\n" +
      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
      "        }]" +
      "   }, \n" +

      "  { " +
          "     \"data\": {\n" +
              "            \"_id\": \"577d08840b073b294d34d547\",\n" +
              "            \"data\": \"17\",\n" +
              "            \"timestamp\": \"2016-07-29T09:00:04.509Z\"\n" +
              "        },\n" +
              "        \"links\": [{\n" +
              "            \"rel\": \"self\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }, {\n" +
              "            \"rel\": \"update\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }, {\n" +
              "            \"rel\": \"remove\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }]" +
              "   },\n" +
              "   { " +
              "     \"data\": {\n" +
              "            \"_id\": \"577d08840b073b294d34d547\",\n" +
              "            \"data\": \"17\",\n" +
              "            \"timestamp\": \"2016-07-29T09:01:05.509Z\"\n" +
              "        },\n" +
              "        \"links\": [{\n" +
              "            \"rel\": \"self\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }, {\n" +
              "            \"rel\": \"update\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }, {\n" +
              "            \"rel\": \"remove\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }]" +
              "   },\n" +
              "   { " +
              "     \"data\": {\n" +
              "            \"_id\": \"577d08840b073b294d34d547\",\n" +
              "            \"data\": \"17\",\n" +
              "            \"timestamp\": \"2016-07-29T09:02:06.509Z\"\n" +
              "        },\n" +
              "        \"links\": [{\n" +
              "            \"rel\": \"self\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }, {\n" +
              "            \"rel\": \"update\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }, {\n" +
              "            \"rel\": \"remove\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }]" +
              "   },\n" +
              "   { " +
              "     \"data\": {\n" +
              "            \"_id\": \"577d08840b073b294d34d547\",\n" +
              "            \"data\": \"17\",\n" +
              "            \"timestamp\": \"2016-07-29T09:03:07.509Z\"\n" +
              "        },\n" +
              "        \"links\": [{\n" +
              "            \"rel\": \"self\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }, {\n" +
              "            \"rel\": \"update\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }, {\n" +
              "            \"rel\": \"remove\",\n" +
              "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
              "        }]" +
              "   }, \n" +

      "        { " +
                  "     \"data\": {\n" +
                      "            \"_id\": \"577d08840b073b294d34d547\",\n" +
                      "            \"data\": \"17\",\n" +
                      "            \"timestamp\": \"2016-07-30T08:56:50.509Z\"\n" +
                      "        },\n" +
                      "        \"links\": [{\n" +
                      "            \"rel\": \"self\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }, {\n" +
                      "            \"rel\": \"update\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }, {\n" +
                      "            \"rel\": \"remove\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }]" +
                      "   },\n" +
                      "   { " +
                      "     \"data\": {\n" +
                      "            \"_id\": \"577d08840b073b294d34d547\",\n" +
                      "            \"data\": \"17\",\n" +
                      "            \"timestamp\": \"2016-07-31T08:56:50.509Z\"\n" +
                      "        },\n" +
                      "        \"links\": [{\n" +
                      "            \"rel\": \"self\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }, {\n" +
                      "            \"rel\": \"update\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }, {\n" +
                      "            \"rel\": \"remove\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }]" +
                      "   },\n" +
                      "   { " +
                      "     \"data\": {\n" +
                      "            \"_id\": \"577d08840b073b294d34d547\",\n" +
                      "            \"data\": \"17\",\n" +
                      "            \"timestamp\": \"2016-08-01T08:56:50.509Z\"\n" +
                      "        },\n" +
                      "        \"links\": [{\n" +
                      "            \"rel\": \"self\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }, {\n" +
                      "            \"rel\": \"update\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }, {\n" +
                      "            \"rel\": \"remove\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }]" +
                      "   },\n" +
                      "   { " +
                      "     \"data\": {\n" +
                      "            \"_id\": \"577d08840b073b294d34d547\",\n" +
                      "            \"data\": \"17\",\n" +
                      "            \"timestamp\": \"2016-08-02T08:56:50.509Z\"\n" +
                      "        },\n" +
                      "        \"links\": [{\n" +
                      "            \"rel\": \"self\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }, {\n" +
                      "            \"rel\": \"update\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }, {\n" +
                      "            \"rel\": \"remove\",\n" +
                      "            \"href\": \"/api/v1/clients/577d08840b073b294d34d547/data\"\n" +
                      "        }]" +
                      "   } \n" +

                      "   ]\n" +
                      "}";
}
