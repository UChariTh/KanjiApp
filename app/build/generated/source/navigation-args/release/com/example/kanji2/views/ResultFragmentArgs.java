package com.example.kanji2.views;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class ResultFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private ResultFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private ResultFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static ResultFragmentArgs fromBundle(@NonNull Bundle bundle) {
    ResultFragmentArgs __result = new ResultFragmentArgs();
    bundle.setClassLoader(ResultFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("quizId")) {
      String quizId;
      quizId = bundle.getString("quizId");
      if (quizId == null) {
        throw new IllegalArgumentException("Argument \"quizId\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("quizId", quizId);
    } else {
      __result.arguments.put("quizId", "null");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static ResultFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    ResultFragmentArgs __result = new ResultFragmentArgs();
    if (savedStateHandle.contains("quizId")) {
      String quizId;
      quizId = savedStateHandle.get("quizId");
      if (quizId == null) {
        throw new IllegalArgumentException("Argument \"quizId\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("quizId", quizId);
    } else {
      __result.arguments.put("quizId", "null");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getQuizId() {
    return (String) arguments.get("quizId");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("quizId")) {
      String quizId = (String) arguments.get("quizId");
      __result.putString("quizId", quizId);
    } else {
      __result.putString("quizId", "null");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("quizId")) {
      String quizId = (String) arguments.get("quizId");
      __result.set("quizId", quizId);
    } else {
      __result.set("quizId", "null");
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    ResultFragmentArgs that = (ResultFragmentArgs) object;
    if (arguments.containsKey("quizId") != that.arguments.containsKey("quizId")) {
      return false;
    }
    if (getQuizId() != null ? !getQuizId().equals(that.getQuizId()) : that.getQuizId() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getQuizId() != null ? getQuizId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ResultFragmentArgs{"
        + "quizId=" + getQuizId()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull ResultFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder() {
    }

    @NonNull
    public ResultFragmentArgs build() {
      ResultFragmentArgs result = new ResultFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setQuizId(@NonNull String quizId) {
      if (quizId == null) {
        throw new IllegalArgumentException("Argument \"quizId\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("quizId", quizId);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getQuizId() {
      return (String) arguments.get("quizId");
    }
  }
}
