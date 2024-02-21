package com.quizlet.controller;

import com.quizlet.dto.request.FolderReqDto;
import com.quizlet.mapping.FolderMapper;
import com.quizlet.model.Folder;
import com.quizlet.model.FolderEntityGraph;
import com.quizlet.service.FolderService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController implements SecuredRestController {

  private final FolderService folderService;
  private final FolderMapper folderMapper;

  @PostMapping()
  public ResponseEntity<?> create(@Valid @RequestBody FolderReqDto dto) {
    Folder folder = folderService.create(dto);
    return ResponseEntity.ok(folderMapper.model2Dto(folder));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable UUID id) {
    FolderEntityGraph entityGraph = FolderEntityGraph.____().topics().____.____();
    Folder folder = folderService.getById(id, entityGraph, false);
    return ResponseEntity.ok(folderMapper.model2ExtendDto(folder));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateById(@PathVariable UUID id, @Valid @RequestBody FolderReqDto dto) {
    Folder folder = folderService.updateById(id, dto);
    return ResponseEntity.ok(folderMapper.model2Dto(folder));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable UUID id) {
    folderService.deleteById(id);
    return ResponseEntity.ok(null);
  }

  @GetMapping
  public ResponseEntity<?> getList(
      @PageableDefault(
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          @ParameterObject
          Pageable pageable,
      @RequestParam(required = false) String[] filter) {
    Page<Folder> folders = folderService.getList(filter, pageable);
    return ResponseEntity.ok(folderMapper.model2Dto(folders));
  }
}
