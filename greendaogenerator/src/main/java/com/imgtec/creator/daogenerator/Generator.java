/*
 * <b>Copyright (c) 2015. by Imagination Technologies Limited
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
 *
 */

package com.imgtec.creator.daogenerator;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class Generator {


  private static final String PACKAGE = "com.imgtec.creator.petunia.data";
  private static final int DB_VERSION = 1;


  public static void main(String args[]) throws Exception {

    Schema schema = new Schema(DB_VERSION, PACKAGE);

    createDatabase(schema);
    new DaoGenerator().generateAll(schema, args[0]);
  }

  private static void createDatabase(final Schema schema) {
    //TODO: implement if needed

  }
}
