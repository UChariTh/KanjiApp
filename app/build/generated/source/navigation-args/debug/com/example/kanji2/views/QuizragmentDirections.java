package com.example.kanji2.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.kanji2.R;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class QuizragmentDirections {
  private QuizragmentDirections() {
  }

  @NonNull
  public static ActionQuizragmentToResultFragment actionQuizragmentToResultFragment() {
    return new ActionQuizragmentToResultFragment();
  }

  @NonNull
  public static NavDirections actionQuizragmentToListFragment() {
    return new ActionOnlyNavDirections(R.id.action_quizragment_to_listFragment);
  }

  public static class ActionQuizragmentToResultFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionQuizragmentToResultFragment() {
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionQuizragmentToResultFragment setQuizId(@NonNull String quizId) {
      if (quizId == null) {
        throw new IllegalArgumentException("Argument \"quizId\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("quizId", quizId);
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
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_quizragment_to_resultFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getQuizId() {
      return (String) arguments.get("quizId");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionQuizragmentToResultFragment that = (ActionQuizragmentToResultFragment) object;
      if (arguments.containsKey("quizId") != that.arguments.containsKey("quizId")) {
        return false;
      }
      if (getQuizId() != null ? !getQuizId().equals(that.getQuizId()) : that.getQuizId() != null) {
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
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionQuizragmentToResultFragment(actionId=" + getActionId() + "){"
          + "quizId=" + getQuizId()
          + "}";
    }
  }
}
