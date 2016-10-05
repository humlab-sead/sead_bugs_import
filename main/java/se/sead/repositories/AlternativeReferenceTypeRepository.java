package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import se.sead.bugsimport.sample.seadmodel.AlternativeReferenceType;

import java.util.List;

public interface AlternativeReferenceTypeRepository extends Repository<AlternativeReferenceType, Integer> {

    AlternativeReferenceType findOne(Integer id);

    @Query("select alternativeType from AlternativeReferenceType alternativeType " +
            "where name = 'Other alternative sample name'")
    AlternativeReferenceType findOtherAlternativeSampleName();
}
