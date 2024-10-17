/**
 * Copyright 2019 (C) VinBrain
 */

package net.vinbrain.vbmda.common.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;

/**
 * Default repo factory.
 *
 * @author Nguyen Minh Man
 */
public class VbmdaRepositoryFactory extends JpaRepositoryFactory {

    /** Entity manager. */
    private final EntityManager em;

    /**
     * Constructor.
     *
     * @param entityManager
     *         Is the entity manager
     */
    public VbmdaRepositoryFactory(final EntityManager entityManager) {
        super(entityManager);
        em = entityManager;
    }

    @Override
    protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
        return VbmdaRepository.class;
    }
}
