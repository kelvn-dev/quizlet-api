package com.quizlet.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "folder")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Folder extends BaseModel {

  @Column(name = "name")
  private String name;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "folder_topic",
      joinColumns = @JoinColumn(name = "folder_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"))
  private Set<Topic> topics;
}
