package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

public interface MethodRepository extends Repository<Method, Integer> {
    Method getByNameAndGroup(String name, MethodGroup group);
    Method getByAbbreviationAndGroup(String abbreviation, MethodGroup group);
    Method findOne(Integer id);
}
