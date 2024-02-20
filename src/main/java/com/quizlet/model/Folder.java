package com.quizlet.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "folder")
@Data
@DynamicUpdate
public class Folder extends BaseModel {

  @Column(name = "name")
  private String name;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "folder_topic",
      joinColumns = @JoinColumn(name = "folder_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"))
  private Set<Topic> topics = new HashSet<>();
}
