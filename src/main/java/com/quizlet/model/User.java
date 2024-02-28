package com.quizlet.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(
    name = "app_user",
    indexes = {@Index(name = "idx_auth0_user_id", columnList = "auth0_user_id")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class User extends BaseModel {

  @Column(name = "nickname", nullable = false, unique = true, updatable = false)
  private String nickname;

  @Column(name = "email", nullable = false, unique = true, updatable = false)
  private String email;

  @Column(name = "auth0_user_id", nullable = false, unique = true, updatable = false)
  private String auth0UserId;

  @Column(name = "avatar")
  private String avatar;

  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private Set<UserScore> userScores;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
  @JoinTable(
      name = "user_score",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "topic_id"))
  private Set<Topic> topics;
}
