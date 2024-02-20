package com.quizlet.service;

import com.quizlet.dto.request.FolderReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.mapping.FolderMapper;
import com.quizlet.model.Folder;
import com.quizlet.repository.FolderRepository;
import org.springframework.stereotype.Service;

@Service
public class FolderService extends BaseService<Folder, FolderRepository> {

  private final FolderMapper folderMapper;

  public FolderService(FolderRepository repository, FolderMapper folderMapper) {
    super(repository);
    this.folderMapper = folderMapper;
  }

  public Folder create(FolderReqDto dto) {
    if (repository.findByName(dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Folder folder = folderMapper.dto2Model(dto);
    return repository.save(folder);
  }
}
