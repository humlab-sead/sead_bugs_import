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

        if (entity instanceof LoggableEntity) {
            LoggableEntity loggableEntity = (LoggableEntity)entity;
            if(loggableEntity.isMarkedForDeletion()) {
                Object entityToDelete = entityManager.find(entity.getClass(), loggableEntity.getId());
                entityManager.remove(entityToDelete);
                return null;
            } // else if (loggableEntity.isNewItem()) {
            //    entityManager.persist(entity);
            //    return entity;
            //}
        }
        return entityManager.merge(entity);
    }
}
