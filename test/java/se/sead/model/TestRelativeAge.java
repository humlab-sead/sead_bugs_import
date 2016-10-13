package se.sead.model;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.periods.seadmodel.RelativeAgeType;

import java.math.BigDecimal;

public class TestRelativeAge extends RelativeAge {

    private TestRelativeAge(Integer id){
        setId(id);
    }

    public static RelativeAge create(
            Integer id,
            String abbreviation,
            String name,
            BigDecimal c14AgeOlder,
            BigDecimal c14AgeYounger,
            BigDecimal calAgeOlder,
            BigDecimal calAgeYounger,
            String description,
            RelativeAgeType type,
            Location location
    ) {
        RelativeAge relativeAge = new TestRelativeAge(id);
        relativeAge.setAbbreviation(abbreviation);
        relativeAge.setName(name);
        relativeAge.setC14AgeOlder(c14AgeOlder);
        relativeAge.setC14AgeYounger(c14AgeYounger);
        relativeAge.setCalAgeOlder(calAgeOlder);
        relativeAge.setCalAgeYounger(calAgeYounger);
        relativeAge.setDescription(description);
        relativeAge.setType(type);
        relativeAge.setGeographicExtent(location);
        return relativeAge;
    }
}
