package com.quizlet.controller;

import com.quizlet.dto.request.FolderReqDto;
import com.quizlet.mapping.FolderMapper;
import com.quizlet.model.Folder;
import com.quizlet.model.FolderEntityGraph;
import com.quizlet.service.FolderService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/folders")
@RequiredArgsConstructor
public class FolderController implements SecuredRestController {

  private final FolderService folderService;
  private final FolderMapper folderMapper;

  @PostMapping()
  public ResponseEntity<?> create(
      JwtAuthenticationToken token, @Valid @RequestBody FolderReqDto dto) {
    Folder folder = folderService.create(token, dto);
    return ResponseEntity.ok(folderMapper.model2Dto(folder));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(JwtAuthenticationToken token, @PathVariable UUID id) {
    FolderEntityGraph entityGraph = FolderEntityGraph.____().topics().____.____();
    Folder folder = folderService.getById(token, id, entityGraph, false);
    return ResponseEntity.ok(folderMapper.model2ExtendDto(folder));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateById(
      JwtAuthenticationToken token, @PathVariable UUID id, @Valid @RequestBody FolderReqDto dto) {
    Folder folder = folderService.updateById(token, id, dto);
    return ResponseEntity.ok(folderMapper.model2Dto(folder));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(JwtAuthenticationToken token, @PathVariable UUID id) {
    folderService.deleteById(token, id);
    return ResponseEntity.ok(null);
  }

  @GetMapping
  public ResponseEntity<?> getList(
      JwtAuthenticationToken token,
      @PageableDefault(
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          @ParameterObject
          Pageable pageable,
      @RequestParam(required = false, defaultValue = "") List<String> filter) {
    Page<Folder> folders = folderService.getList(token, filter, pageable);
    return ResponseEntity.ok(folderMapper.model2Dto(folders));
  }
}
