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

package com.imgtec.creator.petunia.data.api.oauth;

import com.imgtec.di.PerApp;

import dagger.Module;
import dagger.Provides;

@Module
public class OauthModule {

  @Provides @PerApp
  OauthTokenWrapper provideOauthTokenWrapper() {
    return new OauthTokenWrapper();
  }

  @Provides @PerApp
  OauthInterceptor provideOauthInterceptor(OauthTokenWrapper tokenWrapper) {
    return new OauthInterceptor(tokenWrapper);
  }

  //TODO: implement
}
