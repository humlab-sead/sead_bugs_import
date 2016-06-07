package se.sead.repositories.impl;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import se.sead.repositories.CreateAndReadRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class CreateAndReadRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CreateAndReadRepository<T, ID> {

    private JpaEntityInformation<T, ID> entityInformation;
    private EntityManager entityManager;

    public CreateAndReadRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public T saveUsingMerge(T entity) {
        return entityManager.merge(entity);
    }
}
