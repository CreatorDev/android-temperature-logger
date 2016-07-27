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

package com.imgtec.creator.petunia.data.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Hateoas {

  @SerializedName("Links")
  @Expose
  private List<Link> links = new ArrayList<Link>();

  private Map<String, Link> linksMap = new HashMap<>();

  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public Link getLinkByRel(String rel) {
    if (links.isEmpty()) {
      return null;
    }
    if (links.isEmpty() == false && linksMap.isEmpty()) {
      for (Link link : links) {
        linksMap.put(link.getRel(), link);
      }
    }
    return linksMap.get(rel);
  }
}
