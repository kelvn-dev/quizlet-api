package com.quizlet.service;

import com.quizlet.exception.NotFoundException;
import com.quizlet.model.BaseModel;
import com.quizlet.repository.BaseRepository;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

  public void deleteById(UUID id) {
    M model = this.getById(id, false);
    repository.delete(model);
  }

  //  public ApiPageableResponse getList(String[] filter, Pageable pageable) {
  //    List<SearchCriteria> criteria = HelperUtils.formatSearchCriteria(filter);
  //    BooleanExpression expression = PredicateUtils.getBooleanExpression(criteria, modelClass);
  //    Page<M> pagingModel = repository.findAll(expression, pageable);
  //    return formatPagingResponse(pagingModel);
  //  }
  //
  //  public ApiPageableResponse formatPagingResponse(Page<M> page) {
  //    return ApiPageableResponse.builder()
  //        .currentPage(page.getNumber() + 1)
  //        .pageSize(page.getSize())
  //        .totalPages(page.getTotalPages())
  //        .totalElements(page.getTotalElements())
  //        .isFirst(page.isFirst())
  //        .isLast(page.isLast())
  //        .data(mappingUtils.mapListToDTO(page.getContent(), responseDtoClass))
  //        .build();
  //  }
}
