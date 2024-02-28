package com.quizlet.service;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.quizlet.exception.NotFoundException;
import com.quizlet.model.BaseModel;
import com.quizlet.repository.BaseRepository;
import com.quizlet.utils.HelperUtils;
import com.quizlet.utils.PredicateUtils;
import com.quizlet.utils.SearchCriteria;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class BaseService<M extends BaseModel, R extends BaseRepository<M, UUID>> {

  protected final Class<M> modelClass =
      (Class<M>)
          ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

  protected final R repository;

  public M getById(UUID id, boolean noException) {
    M model = repository.findById(id).orElse(null);
    if (Objects.isNull(model) && !noException) {
      throw new NotFoundException(modelClass, "id", id.toString());
    }
    return model;
  }

  public M getById(UUID id, EntityGraph entityGraph, boolean noException) {
    M model = repository.findById(id, entityGraph).orElse(null);
    if (Objects.isNull(model) && !noException) {
      throw new NotFoundException(modelClass, "id", id.toString());
    }
    return model;
  }

  public void deleteById(UUID id) {
    M model = this.getById(id, false);
    repository.delete(model);
  }

  public Page<M> getList(List<String> filter, Pageable pageable) {
    List<SearchCriteria> criteria = HelperUtils.formatSearchCriteria(filter);
    BooleanExpression expression = PredicateUtils.getBooleanExpression(criteria, modelClass);
    return repository.findAll(expression, pageable);
  }
}
