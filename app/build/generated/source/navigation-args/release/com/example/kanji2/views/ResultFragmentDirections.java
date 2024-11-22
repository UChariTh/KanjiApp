package com.example.kanji2.views;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.kanji2.R;

public class ResultFragmentDirections {
  private ResultFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionResultFragmentToListFragment() {
    return new ActionOnlyNavDirections(R.id.action_resultFragment_to_listFragment);
  }
}
