package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.sead.methods.MethodGroup;

public interface MethodGroupRepository extends Repository<MethodGroup, Integer>{
    MethodGroup findByName(String name);
    MethodGroup findOne(Integer id);
}
