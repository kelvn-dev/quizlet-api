package com.quizlet.model;

import com.quizlet.model.compositekey.UserScoreId;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "user_score")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@IdClass(UserScoreId.class)
public class UserScore {
  @Id
  @Column(name = "user_id", columnDefinition = "uuid")
  private UUID userId;

  @Id
  @Column(name = "topic_id", columnDefinition = "uuid")
  private UUID topicId;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "topic_id", insertable = false, updatable = false)
  private Topic topic;

  @Column(name = "score")
  private int score;
}
