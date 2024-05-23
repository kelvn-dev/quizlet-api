package com.quizlet.model;

import com.quizlet.model.compositekey.WordFactorId;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "word_factor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@IdClass(WordFactorId.class)
public class WordFactor implements Serializable {
  @Id
  @Column(name = "user_id")
  private String userId;

  @Id
  @Column(name = "word_id", columnDefinition = "uuid")
  private UUID wordId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "word_id", insertable = false, updatable = false)
  private Word word;

  @Column(name = "learning_count")
  private int learningCount;

  @Column(name = "is_marked")
  private boolean isMarked;

  @Column(name = "created_at", updatable = false)
  private long createdAt;

  @Column(name = "updated_at")
  private long updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now().getEpochSecond();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now().getEpochSecond();
  }
}
