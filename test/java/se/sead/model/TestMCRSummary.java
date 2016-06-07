package se.sead.model;

import se.sead.bugsimport.mcr.seadmodel.MCRSummary;
import se.sead.bugsimport.species.seadmodel.TaxaSpecies;

public class TestMCRSummary extends MCRSummary {

    private TestMCRSummary(Integer id){
        super.setId(id);
    }

    public static MCRSummary create(
            Integer id,
            TaxaSpecies species,
            Integer maxLo,
            Integer maxHi,
            Integer minLo,
            Integer minHi,
            Integer rangeLo,
            Integer rangeHi,
            Integer cogMidMax,
            Integer cogMidRange) {
        MCRSummary summary = new TestMCRSummary(id);
        summary.setSpecies(species);
        summary.setMaxLo(maxLo.shortValue());
        summary.setMaxHi(maxHi.shortValue());
        summary.setMinLo(minLo.shortValue());
        summary.setMinHi(minHi.shortValue());
        summary.setRangeLo(rangeLo.shortValue());
        summary.setRangeHi(rangeHi.shortValue());
        summary.setCogMidMax(cogMidMax.shortValue());
        summary.setCogMidRange(cogMidRange.shortValue());
        return summary;
    }
}
