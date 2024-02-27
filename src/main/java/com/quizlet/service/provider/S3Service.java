package com.quizlet.service.provider;

import com.quizlet.enums.ContentDisposition;
import com.quizlet.utils.HelperUtils;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

  @Value("${aws.s3.bucket-name}")
  private String s3BucketName;

  private final S3Presigner s3Presigner;

  /** For uploading */
  public PresignedPutObjectRequest getPresignedUrl(
      String contentType, String extension, ObjectCannedACL acl) {
    try {
      String randomString = HelperUtils.getRandomString();
      String key = String.format("%s.%s", randomString, extension);

      PutObjectRequest objectRequest =
          PutObjectRequest.builder()
              .bucket(s3BucketName)
              .key(key)
              .contentType(contentType)
              .acl(acl)
              .build();

      PutObjectPresignRequest presignRequest =
          PutObjectPresignRequest.builder()
              .signatureDuration(Duration.ofMinutes(10))
              .putObjectRequest(objectRequest)
              .build();

      PresignedPutObjectRequest request = s3Presigner.presignPutObject(presignRequest);
      return request;
    } catch (S3Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /** For retrieving */
  //  @SneakyThrows(RuntimeException.class)
  public PresignedGetObjectRequest getPresignedUrl(
      String key, ContentDisposition contentDisposition) {
    try {
      GetObjectRequest getObjectRequest =
          GetObjectRequest.builder()
              .bucket(s3BucketName)
              .key(key)
              .responseContentDisposition(
                  ContentDisposition.ATTACHMENT.equals(contentDisposition)
                      ? "attachment; filename=\"" + key.concat(".png") + "\""
                      : ContentDisposition.INLINE.toString())
              .build();

      GetObjectPresignRequest getObjectPresignRequest =
          GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofMinutes(10))
              .getObjectRequest(getObjectRequest)
              .build();

      PresignedGetObjectRequest request = s3Presigner.presignGetObject(getObjectPresignRequest);
      return request;
    } catch (S3Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
