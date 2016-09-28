package se.sead.model;

import se.sead.bugsimport.attributes.seadmodel.TaxaMeasuredAttributes;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

import java.math.BigDecimal;

public class TestTaxaMeasuredAttributes extends TaxaMeasuredAttributes {

    private TestTaxaMeasuredAttributes(Integer id){
        setId(id);
    }

    public static TaxaMeasuredAttributes create(
            Integer id,
            String type,
            String measure,
            BigDecimal value,
            String unit,
            TaxaSpecies species
    ){
        TaxaMeasuredAttributes attributes = new TestTaxaMeasuredAttributes(id);
        attributes.setMeasure(measure);
        attributes.setType(type);
        attributes.setValue(value);
        attributes.setUnits(unit);
        attributes.setSpecies(species);
        return attributes;
    }
}
