package com.example.kanji2.views;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.kanji2.R;

public class SplashFragmentDirections {
  private SplashFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionSplashFragmentToListFragment() {
    return new ActionOnlyNavDirections(R.id.action_splashFragment_to_listFragment);
  }
}
