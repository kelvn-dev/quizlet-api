package com.quizlet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Topic extends BaseModel {

  @Column(name = "name")
  private String name;

  @Column(name = "owner_id", updatable = false)
  private UUID ownerId;

  @ManyToMany(mappedBy = "topics", fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Folder> folders;

  @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, orphanRemoval = true)
  @JsonManagedReference
  private Set<Word> words;

  @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<UserScore> userScores;

  @PreRemove
  private void removeAssociations() {
    for (Folder folder : this.folders) {
      folder.getTopics().remove(this);
    }
  }
}
