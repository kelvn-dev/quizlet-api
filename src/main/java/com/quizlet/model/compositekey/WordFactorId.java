package com.quizlet.model.compositekey;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordFactorId implements Serializable {
  private String userId;

  private UUID wordId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WordFactorId that = (WordFactorId) o;
    return Objects.equals(userId, that.userId) && Objects.equals(wordId, that.wordId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, wordId);
  }
}
