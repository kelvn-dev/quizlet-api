/*
 * Copyright 2020 (C) VinBrain
 */

package net.vinbrain.vbmda.workcase.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.vinbrain.vbmda.common.aop.TenantAware;
import net.vinbrain.vbmda.common.security.SecurityUtils;
import net.vinbrain.vbmda.common.utils.NestedProjections;
import net.vinbrain.vbmda.workcase.dom.Patient;
import net.vinbrain.vbmda.workcase.dom.QPatient;
import net.vinbrain.vbmda.workcase.repository.IPatientRepository;
import net.vinbrain.vbmda.workcase.service.IPatientService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author GiapDQ
 */
@Service
@TenantAware
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class PatientService implements IPatientService {

    @PersistenceContext
    private EntityManager entityManager;

    private final IPatientRepository patientRepository;

    @Override
    public Pair<Long, List<Patient>> findAllWithProjections(final BooleanBuilder condition, final int limit, final int offset) {
        if (Objects.isNull(condition)) {
            return Pair.of(0L, Collections.emptyList());
        }

        final long count = new JPAQuery<>(entityManager)
                .select(QPatient.patient.pid.countDistinct())
                .from(QPatient.patient)
                .where(condition)
                .fetch()
                .stream()
                .findFirst()
                .orElse(0L);
        if (count == 0L) {
            return Pair.of(0L, Collections.emptyList());
        }

        return Pair.of(count, getPatientInternally(condition, limit, offset));
    }

    @Override
    public Patient findOneByPid(final String pid) {
        if (StringUtils.isBlank(pid)) {
            return null;
        }

        return patientRepository.findOne(new JPAQuery<Patient>()
                .from(QPatient.patient)
                .where(QPatient.patient.pid.eq(pid)
                        // Apply the tenant condition to handle the case when an user who belongs to VB tenant
                        // create a workcase because we don't apply the tenant filter on VB users.
                        .and(QPatient.patient.tenantCode.eq(SecurityUtils.currentUser().getTenantCode()))));
    }

    @Override
    public List<Patient> findAllByPid(final String pid) {
        if (StringUtils.isBlank(pid)) {
            return Collections.emptyList();
        }

        return patientRepository.findAll(new JPAQuery<Patient>()
                .from(QPatient.patient)
                .where(QPatient.patient.pid.equalsIgnoreCase(pid)));
    }

    @Override
    @TenantAware(disabled = true)
    public Patient findByPidIgnoreTenant(final String pid) {
        return patientRepository.findAll(new JPAQuery<Patient>()
                .from(QPatient.patient)
                .where(QPatient.patient.pid.eq(pid))
                .limit(1)
            )
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public String getNextIncrementalSuffixPid(final String pid) {
        final QPatient patient = QPatient.patient;
        final String prefix = pid + "_";
        final JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        final NumberExpression<Long> lastSuffix = Expressions.numberTemplate(Long.class,
                "coalesce(max(cast(nullif(substring({0}, {1}, length({0}) + 1), '') as long)), 0)",
                patient.pid, prefix.length() + 1);

        return queryFactory
                .select(Expressions.stringTemplate("concat({0}, {1} + 1)", prefix, lastSuffix))
                .from(patient)
                .where(patient.pid.like(pid+"%"))
                .fetchOne();
    }

    @Override
    public void save(final Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public void saveAll(final List<Patient> patients) {
        if (CollectionUtils.isNotEmpty(patients)) {
            patientRepository.saveAll(patients);
        }
    }

    private List<Patient> getPatientInternally(final BooleanBuilder condition, final int limit, final int offset) {
        return patientRepository.findAll(
                new JPAQuery<Patient>()
                        .from(QPatient.patient)
                        .where(condition)
                        .limit(limit)
                        .offset(offset)
                        .orderBy(QPatient.patient.updatedAt.desc()),
                NestedProjections.of(
                        Patient.class,
                        QPatient.patient.id,
                        QPatient.patient.pid,
                        QPatient.patient.firstName,
                        QPatient.patient.gender,
                        QPatient.patient.dateOfBirth,
                        QPatient.patient.location,
                        QPatient.patient.tenantCode
                ));
    }

}
