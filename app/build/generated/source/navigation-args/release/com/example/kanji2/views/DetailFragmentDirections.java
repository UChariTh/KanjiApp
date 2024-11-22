package com.example.kanji2.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import com.example.kanji2.R;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class DetailFragmentDirections {
  private DetailFragmentDirections() {
  }

  @NonNull
  public static ActionDetailFragmentToQuizragment actionDetailFragmentToQuizragment() {
    return new ActionDetailFragmentToQuizragment();
  }

  public static class ActionDetailFragmentToQuizragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionDetailFragmentToQuizragment() {
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionDetailFragmentToQuizragment setQuizId(@NonNull String quizId) {
      if (quizId == null) {
        throw new IllegalArgumentException("Argument \"quizId\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("quizId", quizId);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionDetailFragmentToQuizragment setTotalQueCount(long totalQueCount) {
      this.arguments.put("totalQueCount", totalQueCount);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("quizId")) {
        String quizId = (String) arguments.get("quizId");
        __result.putString("quizId", quizId);
      } else {
        __result.putString("quizId", "null");
      }
      if (arguments.containsKey("totalQueCount")) {
        long totalQueCount = (long) arguments.get("totalQueCount");
        __result.putLong("totalQueCount", totalQueCount);
      } else {
        __result.putLong("totalQueCount", 0L);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_detailFragment_to_quizragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getQuizId() {
      return (String) arguments.get("quizId");
    }

    @SuppressWarnings("unchecked")
    public long getTotalQueCount() {
      return (long) arguments.get("totalQueCount");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionDetailFragmentToQuizragment that = (ActionDetailFragmentToQuizragment) object;
      if (arguments.containsKey("quizId") != that.arguments.containsKey("quizId")) {
        return false;
      }
      if (getQuizId() != null ? !getQuizId().equals(that.getQuizId()) : that.getQuizId() != null) {
        return false;
      }
      if (arguments.containsKey("totalQueCount") != that.arguments.containsKey("totalQueCount")) {
        return false;
      }
      if (getTotalQueCount() != that.getTotalQueCount()) {
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
      result = 31 * result + (getQuizId() != null ? getQuizId().hashCode() : 0);
      result = 31 * result + (int)(getTotalQueCount() ^ (getTotalQueCount() >>> 32));
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionDetailFragmentToQuizragment(actionId=" + getActionId() + "){"
          + "quizId=" + getQuizId()
          + ", totalQueCount=" + getTotalQueCount()
          + "}";
    }
  }
}
