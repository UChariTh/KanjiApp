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

public class QuizragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private QuizragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private QuizragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static QuizragmentArgs fromBundle(@NonNull Bundle bundle) {
    QuizragmentArgs __result = new QuizragmentArgs();
    bundle.setClassLoader(QuizragmentArgs.class.getClassLoader());
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
    if (bundle.containsKey("totalQueCount")) {
      long totalQueCount;
      totalQueCount = bundle.getLong("totalQueCount");
      __result.arguments.put("totalQueCount", totalQueCount);
    } else {
      __result.arguments.put("totalQueCount", 0L);
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static QuizragmentArgs fromSavedStateHandle(@NonNull SavedStateHandle savedStateHandle) {
    QuizragmentArgs __result = new QuizragmentArgs();
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
    if (savedStateHandle.contains("totalQueCount")) {
      long totalQueCount;
      totalQueCount = savedStateHandle.get("totalQueCount");
      __result.arguments.put("totalQueCount", totalQueCount);
    } else {
      __result.arguments.put("totalQueCount", 0L);
    }
    return __result;
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
    if (arguments.containsKey("totalQueCount")) {
      long totalQueCount = (long) arguments.get("totalQueCount");
      __result.putLong("totalQueCount", totalQueCount);
    } else {
      __result.putLong("totalQueCount", 0L);
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
    if (arguments.containsKey("totalQueCount")) {
      long totalQueCount = (long) arguments.get("totalQueCount");
      __result.set("totalQueCount", totalQueCount);
    } else {
      __result.set("totalQueCount", 0L);
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
    QuizragmentArgs that = (QuizragmentArgs) object;
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
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getQuizId() != null ? getQuizId().hashCode() : 0);
    result = 31 * result + (int)(getTotalQueCount() ^ (getTotalQueCount() >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "QuizragmentArgs{"
        + "quizId=" + getQuizId()
        + ", totalQueCount=" + getTotalQueCount()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull QuizragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder() {
    }

    @NonNull
    public QuizragmentArgs build() {
      QuizragmentArgs result = new QuizragmentArgs(arguments);
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

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setTotalQueCount(long totalQueCount) {
      this.arguments.put("totalQueCount", totalQueCount);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getQuizId() {
      return (String) arguments.get("quizId");
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public long getTotalQueCount() {
      return (long) arguments.get("totalQueCount");
    }
  }
}
