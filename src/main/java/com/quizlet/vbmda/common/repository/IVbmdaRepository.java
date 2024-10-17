/** Copyright 2019 (C) VinBrain */
package com.quizlet.vbmda.common.repository;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Nguyen Minh Man
 */
@NoRepositoryBean
public interface IVbmdaRepository<T, ID extends Serializable>
    extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {

  /**
   * Find all entities by JPA query.
   *
   * @param query is JPA query
   * @return Result list.
   */
  List<T> findAll(JPAQuery<T> query);

  /**
   * Find all entities by JPA query with projection.
   *
   * @param query is JPA query
   * @param factoryExpression is projection
   * @return Result list.
   */
  <DTO> List<DTO> findAll(JPAQuery query, FactoryExpression<DTO> factoryExpression);

  /**
   * Find one entity by JPA query.
   *
   * @param query The input query.
   * @return The single object matching input query.
   */
  T findOne(JPAQuery<T> query);

  /**
   * Count number of entities by JPA query.
   *
   * @param query Is the JPA query.
   * @return number of entities.
   */
  long count(JPAQuery<T> query);
}
