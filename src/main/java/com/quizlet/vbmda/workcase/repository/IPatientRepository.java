/** Copyright 2019 (C) VinBrain */
package com.quizlet.vbmda.workcase.repository;

import com.quizlet.vbmda.common.repository.IVbmdaRepository;
import com.quizlet.vbmda.workcase.dom.Patient;
import org.springframework.stereotype.Repository;

/**
 * @author Nguyen Minh Man
 */
@Repository
public interface IPatientRepository extends IVbmdaRepository<Patient, Long> {}
