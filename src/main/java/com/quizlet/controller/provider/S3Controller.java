package com.quizlet.controller.provider;

import com.quizlet.controller.SecuredRestController;
import com.quizlet.dto.provider.response.PresignedObjectRequestDto;
import com.quizlet.enums.ContentDisposition;
import com.quizlet.mapping.S3Mapper;
import com.quizlet.service.provider.S3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Tag(name = "S3")
@RestController
@RequestMapping("/v1/s3")
@RequiredArgsConstructor
public class S3Controller implements SecuredRestController {

  private final S3Service s3Service;
  private final S3Mapper s3Mapper;

  /**
   * @apiNote postman cannot download, use browser to receive correct behavior
   * @apiNote to add signedHeaders into header request
   */
  @GetMapping("/presigned-request")
  public ResponseEntity<?> getPresignedGetUrl(
      @RequestParam String key, @RequestParam ContentDisposition contentDisposition) {
    PresignedGetObjectRequest request = s3Service.getPresignedUrl(key, contentDisposition);
    PresignedObjectRequestDto dto = s3Mapper.getObjectRequest2Dto(request);
    return ResponseEntity.ok(dto);
  }

  /**
   * @apiNote select binary tab in Body section to upload file while using with postman
   * @apiNote to add signedHeaders into header request
   */
  @PostMapping("/presigned-request")
  public ResponseEntity<?> getPresignedPutObjectRequest(
      @RequestParam String contentType,
      @RequestParam String extension,
      @RequestParam ObjectCannedACL acl) {
    PresignedPutObjectRequest request = s3Service.getPresignedUrl(contentType, extension, acl);
    PresignedObjectRequestDto dto = s3Mapper.putObjectRequest2Dto(request);
    return ResponseEntity.ok(dto);
  }
}
