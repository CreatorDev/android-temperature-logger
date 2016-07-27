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