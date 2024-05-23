package com.quizlet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "word")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Word extends BaseModel {

  @Column(name = "name")
  private String name;

  @Column(name = "definition")
  private String definition;

  @Column(name = "topic_id", columnDefinition = "uuid")
  private UUID topicId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topic_id", insertable = false, updatable = false)
  @JsonBackReference
  private Topic topic;

  @OneToMany(mappedBy = "word", fetch = FetchType.LAZY, orphanRemoval = true)
  private Set<WordFactor> wordFactors;
}
