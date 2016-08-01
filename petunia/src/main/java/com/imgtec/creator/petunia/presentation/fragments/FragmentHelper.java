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

package com.imgtec.creator.petunia.presentation.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.Transition;

import com.imgtec.creator.petunia.R;


public class FragmentHelper {

  public static void replaceFragment(FragmentManager fm, Fragment fragment) {
    fm.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commitAllowingStateLoss();
  }

  public static void replaceFragment(FragmentManager fm, Fragment fragment, Transition enterTransition) {
    fragment.setEnterTransition(enterTransition);
    replaceFragment(fm, fragment);
  }

  public static void replaceFragment(FragmentManager fm, Fragment fragment, Transition enterTransition,
                                     boolean allowEnterTransitionOverlap) {
    fragment.setAllowEnterTransitionOverlap(allowEnterTransitionOverlap);
    replaceFragment(fm, fragment, enterTransition);
  }

  public static void replaceFragmentAndClearBackStack(FragmentManager fm, Fragment fragment) {
    final android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    transaction.replace(R.id.fragment_container, fragment);
    transaction.commitAllowingStateLoss();
  }

  public static void replaceFragmentAndClearBackStack(FragmentManager fm, Fragment fragment, Transition enterTransition) {
    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    fragment.setEnterTransition(enterTransition);
    replaceFragmentAndClearBackStack(fm, fragment);
  }
}