/**
 * Copyright 2019 (C) VinBrain
 */

package net.vinbrain.vbmda.workcase.repository;

import net.vinbrain.vbmda.common.repository.IVbmdaRepository;
import net.vinbrain.vbmda.workcase.dom.Patient;
import org.springframework.stereotype.Repository;

/**
 * @author Nguyen Minh Man
 */
@Repository
public interface IPatientRepository extends IVbmdaRepository<Patient, Long> {

}
