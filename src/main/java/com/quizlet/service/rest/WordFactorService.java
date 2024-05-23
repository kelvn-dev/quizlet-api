package com.quizlet.service.rest;

import com.quizlet.dto.rest.request.WordFactorReqDto;
import com.quizlet.model.WordFactor;
import com.quizlet.model.WordFactorEntityGraph;
import com.quizlet.repository.WordFactorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WordFactorService {
  private final WordFactorRepository repository;

  public void markWords(JwtAuthenticationToken token, WordFactorReqDto dto) {
    markOrUnmarkWords(token, dto, true);
  }

  public void unmarkWords(JwtAuthenticationToken token, WordFactorReqDto dto) {
    markOrUnmarkWords(token, dto, false);
  }

  private void markOrUnmarkWords(
      JwtAuthenticationToken token, WordFactorReqDto dto, boolean isMark) {
    List<UUID> wordIds = dto.getWordIds();
    String userId = token.getToken().getSubject();
    List<WordFactor> existingWordFactors = repository.findByUserIdAndWordIdIn(userId, wordIds);
    existingWordFactors.forEach(word -> word.setMarked(isMark));
    List<UUID> existingWordIds =
        existingWordFactors.stream().map(WordFactor::getWordId).collect(Collectors.toList());

    wordIds.removeAll(existingWordIds); // Remove existing words to get list new words to create
    List<WordFactor> wordFactors = new ArrayList<>();
    wordIds.forEach(
        wordId -> {
          WordFactor wordFactor = new WordFactor();
          wordFactor.setUserId(userId);
          wordFactor.setWordId(wordId);
          wordFactor.setMarked(isMark);
          wordFactors.add(wordFactor);
        });
    existingWordFactors.addAll(wordFactors);
    repository.saveAll(existingWordFactors);
  }

  public List<WordFactor> getMarkedWords(JwtAuthenticationToken token) {
    String userId = token.getToken().getSubject();
    WordFactorEntityGraph entityGraph = WordFactorEntityGraph.____().word().____.____();
    return repository.findByUserIdAndIsMarkedIsTrue(userId, entityGraph);
  }

  public void increaseLearningCountBy1(JwtAuthenticationToken token, WordFactorReqDto dto) {
    List<UUID> wordIds = dto.getWordIds();
    String userId = token.getToken().getSubject();
    List<WordFactor> existingWordFactors = repository.findByUserIdAndWordIdIn(userId, wordIds);
    existingWordFactors.forEach(word -> word.setLearningCount(word.getLearningCount() + 1));
    List<UUID> existingWordIds =
        existingWordFactors.stream().map(WordFactor::getWordId).collect(Collectors.toList());

    wordIds.removeAll(existingWordIds); // Remove existing words to get list new words to create
    List<WordFactor> wordFactors = new ArrayList<>();
    wordIds.forEach(
        wordId -> {
          WordFactor wordFactor = new WordFactor();
          wordFactor.setUserId(userId);
          wordFactor.setWordId(wordId);
          wordFactor.setLearningCount(1);
          wordFactors.add(wordFactor);
        });
    existingWordFactors.addAll(wordFactors);
    repository.saveAll(existingWordFactors);
  }
}
