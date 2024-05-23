package com.quizlet.dto.rest.response;

import java.io.Serializable;
import java.util.List;
import lombok.*;

@Data
public class PageResDto<M> implements Serializable {
  private int pageIndex;
  private int totalPages;
  private long totalItems;
  private boolean last;
  private boolean first;
  private List<M> items;
}
