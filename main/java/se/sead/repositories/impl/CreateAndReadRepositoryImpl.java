package se.sead.repositories.impl;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import se.sead.repositories.CreateAndReadRepository;
import se.sead.sead.model.LoggableEntity;

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
    public T saveOrUpdate(T entity) {
        if(entity instanceof LoggableEntity && ((LoggableEntity)entity).isMarkedForDeletion()){
            Object entityToDelete = entityManager.find(entity.getClass(), ((LoggableEntity) entity).getId());
            entityManager.remove(entityToDelete);
            return null;
        } else {
            return entityManager.merge(entity);
        }
    }
}
