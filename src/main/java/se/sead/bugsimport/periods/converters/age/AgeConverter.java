package se.sead.bugsimport.periods.converters.age;

import se.sead.bugsimport.periods.bugsmodel.Period;

import java.math.BigDecimal;
import java.util.List;

public abstract class AgeConverter {

    private List<AgeConversion> conversions;

    protected AgeConverter(List<AgeConversion> conversions){
        this.conversions = conversions;
    }

    public BigDecimal getBeginAge(Period bugsData){
        String beginBCad = bugsData.getBeginBCad();
        Integer beginValue = bugsData.getBegin();
        return convert(beginBCad, beginValue);
    }

    private BigDecimal convert(String beginBCad, Integer beginValue) {
        for (AgeConversion converter :
                conversions) {
            if (converter.handleValue(beginBCad, beginValue)){
                return converter.convert(beginBCad, beginValue);
            }
        }
        throw new IllegalStateException("Unimplemented data setup " + getClass() + ": " + beginBCad + ", " + beginValue);
    }

    public BigDecimal getEndAge(Period bugsData){
        String endBCad = bugsData.getEndBCad();
        Integer end = bugsData.getEnd();
        return convert(endBCad, end);
    }
}
