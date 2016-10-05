package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.sead.methods.Method;
import se.sead.sead.methods.MethodGroup;

public interface MethodRepository extends Repository<Method, Integer> {
    Method getByNameAndGroup(String name, MethodGroup group);
    Method getByAbbreviationAndGroup(String abbreviation, MethodGroup group);
    Method findOne(Integer id);

    @Query("select method from Method method " +
            "where method.name = 'Depth from datum' and " +
            "method.group.name = 'Coordinate and altitude systems'")
    Method getBugsSampleDimensionMethod();
}
