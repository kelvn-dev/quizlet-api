package com.quizlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "topic")
@Data
@DynamicUpdate
public class Topic extends BaseModel {

  @Column(name = "name")
  private String name;

  @ManyToMany(mappedBy = "topics")
  private Set<Folder> folders = new HashSet<>();
}
