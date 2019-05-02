package se.sead.repositories;

import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

import java.util.List;

public interface MeasuredAttributesRepository extends CreateAndReadRepository<TaxaMeasuredAttributes, Integer>{
    List<TaxaMeasuredAttributes> findAll();

    List<TaxaMeasuredAttributes> findBySpecies(TaxaSpecies species);

    TaxaMeasuredAttributes findByTypeAndMeasureAndUnitsAndSpecies(String attribType, String attribMeasure, String attribUnits, TaxaSpecies bugsSpeciesByCode);
}
