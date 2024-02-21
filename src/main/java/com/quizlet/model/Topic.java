package com.quizlet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "topic")
@Data
@DynamicUpdate
public class Topic extends BaseModel {

  @Column(name = "name")
  private String name;

  //  @ManyToMany(mappedBy = "topics")
  //  private Set<Folder> folders = new HashSet<>();
}
