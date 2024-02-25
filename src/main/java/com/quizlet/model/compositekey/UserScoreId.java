package com.quizlet.model.compositekey;

import java.io.Serializable;
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
public class UserScoreId implements Serializable {
  private UUID userId;

  private UUID topicId;
}
