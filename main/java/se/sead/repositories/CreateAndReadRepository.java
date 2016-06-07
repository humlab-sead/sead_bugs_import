package se.sead.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Created by erer0001 on 2016-04-22.
 */
@NoRepositoryBean
public interface CreateAndReadRepository<T, ID extends Serializable> extends Repository<T, ID> {
    T findOne(ID id);

//    T save(T entity);

    T saveUsingMerge(T entity);
}
