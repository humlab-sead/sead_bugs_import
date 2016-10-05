package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.sead.methods.MethodGroup;
import se.sead.sead.model.Dimension;

public interface DimensionRepository extends Repository<Dimension, Integer>{
    Dimension findOne(Integer id);
    Dimension findByName(String name);
    Dimension findByNameAndMethodGroup(String name, MethodGroup methodGroup);

    @Query("select dim from Dimension dim " +
            "where dim.methodGroup.name = 'Coordinate and altitude systems' " +
            "and dim.name = 'Upper boundary depth from unknown reference'")
    Dimension getUpperDepthFromUnknownReference();
    @Query("select dim from Dimension dim " +
            "where dim.methodGroup.name = 'Coordinate and altitude systems' " +
            "and dim.name = 'Lower boundary depth from unknown reference'")
    Dimension getLowerDepthFromUnknownReference();
}
