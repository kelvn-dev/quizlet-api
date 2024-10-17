/**
 * Copyright 2019 (C) VinBrain
 */

package net.vinbrain.vbmda.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Default repository factory bean.
 *
 * @author Nguyen Minh Man
 */
public class VbmdaRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>
        extends JpaRepositoryFactoryBean<T, S, ID> {

    /**
     * @param repositoryInterface
     */
    public VbmdaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new VbmdaRepositoryFactory(entityManager);
    }
}
