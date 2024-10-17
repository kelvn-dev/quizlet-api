package com.quizlet.vbmda.workcase.web.controller;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.quizlet.vbmda.workcase.constant.MedicalConstants;
import com.quizlet.vbmda.workcase.dom.Patient;
import com.quizlet.vbmda.workcase.dom.QPatient;
import com.quizlet.vbmda.workcase.gen.GetPatientRequestV5;
import com.quizlet.vbmda.workcase.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v5/patients")
@RequiredArgsConstructor
public class PatientControllerV5 {

  private final PatientService patientService;

  @GetMapping
  public ResponseEntity<?> getList(final GetPatientRequestV5 criteria) {
    final BooleanBuilder condition = buildConditionToGetPatients(criteria);

    final int limit = ObjectUtils.defaultIfNull(criteria.getLimit(), MedicalConstants.DEFAULT_LIMIT);
    final int offset = ObjectUtils.defaultIfNull(criteria.getOffset(), MedicalConstants.DEFAULT_OFFSET);

//    final boolean showPatientName = VBCommonUtils.getCurrentTenantConfiguration().isShowPatientName();
    final Pair<Long, List<Patient>> patients = patientService.findAllWithProjections(condition, limit, offset);

    final Map<String, List<Patient>> pidToPatient = patients.getValue()
      .stream()
      .collect(Collectors.groupingBy(Patient::getPid, LinkedHashMap::new, Collectors.toList()));

    return ResponseEntity.ok(pidToPatient);
//    return ResponseEntity.ok(new GetPatientResponseV5()
//      .total(patients.getLeft())
//      .data(pidToPatient.entrySet()
//        .stream()
//        .map(item -> new PatientItemV5()
//          .pid(item.getKey())
//          .entities(item.getValue()
//            .stream()
//            .map(patient -> new PatientEntityV5()
//              .id(patient.getId())
//              .name(showPatientName ? patient.getFirstName() : StringUtils.EMPTY)
//              .gender(EnumUtils.getEnum(Gender.class, patient.getGenderSafely()))
//              .birthDate(VBDateUtils.dateToMilliseconds(patient.getDateOfBirth()))
//              .yearOfBirth(VBDateUtils.calculateAge(patient.getDateOfBirth()))
//              .address(patient.getLocation())
//              .tenantCode(patient.getTenantCode()))
//            .collect(Collectors.toList())))
//        .collect(Collectors.toList())));
  }

  private BooleanBuilder buildConditionToGetPatients(final GetPatientRequestV5 criteria) {
    final BooleanBuilder condition = new BooleanBuilder();

    if (StringUtils.isNotBlank(criteria.getPid())) {
      condition.and(ExpressionUtils.anyOf(
        QPatient.patient.pid.containsIgnoreCase(criteria.getPid()),
        QPatient.patient.firstName.containsIgnoreCase(criteria.getPid()))
      );
    }

    if (Objects.nonNull(criteria.getFromDate())) {
      final Date truncatedFromDate = DateUtils.truncate(new Date(criteria.getFromDate()), Calendar.DATE);
      condition.and(QPatient.patient.updatedAt.goe(truncatedFromDate));
    }

    if (Objects.nonNull(criteria.getToDate())) {
      final Date truncatedToDate = DateUtils.truncate(new Date(criteria.getToDate()), Calendar.DATE);
      condition.and(QPatient.patient.updatedAt.lt(DateUtils.addDays(truncatedToDate, 1)));
    }

    if (!CollectionUtils.isEmpty(criteria.getTenantCodes())) {
      condition.and(QPatient.patient.tenantCode.in(criteria.getTenantCodes()));
    }

    return condition;
  }
}
