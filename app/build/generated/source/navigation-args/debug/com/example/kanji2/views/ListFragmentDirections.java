package com.example.kanji2.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import com.example.kanji2.R;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class ListFragmentDirections {
  private ListFragmentDirections() {
  }

  @NonNull
  public static ActionListFragmentToDetailFragment actionListFragmentToDetailFragment() {
    return new ActionListFragmentToDetailFragment();
  }

  public static class ActionListFragmentToDetailFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionListFragmentToDetailFragment() {
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionListFragmentToDetailFragment setPosition(int position) {
      this.arguments.put("position", position);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("position")) {
        int position = (int) arguments.get("position");
        __result.putInt("position", position);
      } else {
        __result.putInt("position", 0);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_listFragment_to_detailFragment;
    }

    @SuppressWarnings("unchecked")
    public int getPosition() {
      return (int) arguments.get("position");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionListFragmentToDetailFragment that = (ActionListFragmentToDetailFragment) object;
      if (arguments.containsKey("position") != that.arguments.containsKey("position")) {
        return false;
      }
      if (getPosition() != that.getPosition()) {
        return false;
      }
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + getPosition();
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionListFragmentToDetailFragment(actionId=" + getActionId() + "){"
          + "position=" + getPosition()
          + "}";
    }
  }
}
