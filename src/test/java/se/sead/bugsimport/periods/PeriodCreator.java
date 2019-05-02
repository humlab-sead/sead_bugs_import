package se.sead.bugsimport.periods;

import se.sead.bugsimport.periods.bugsmodel.Period;

import java.util.Arrays;
import java.util.List;

public class PeriodCreator {

    public static Period create(
            String periodCode,
            String periodName,
            String periodType,
            String periodDesc,
            String ref,
            String periodGeog,
            Integer begin,
            String beginBCad,
            Integer end,
            String endBCad,
            String yearsType
    ){
        Period period = new Period();
        period.setPeriodCode(periodCode);
        period.setName(periodName);
        period.setType(periodType);
        period.setDesc(periodDesc);
        period.setRef(ref);
        period.setGeography(periodGeog);
        period.setBegin(begin);
        period.setBeginBCad(beginBCad);
        period.setEnd(end);
        period.setEndBCad(endBCad);
        period.setYearsType(yearsType);
        return period;
    }

    public static PeriodBuilder createBuilder(){
        return new PeriodBuilder();
    }

    public static class PeriodBuilder {

        public static final List<String> TYPES = Arrays.asList(
                "Archaeological",
                "Geological",
                "Blytt-Sernander",
                "Other",
                "Historical",
                "Oxygen Isotope Stage",
                "Pollen zone"
        );

        public static final String DEFAULT_PERIOD_CODE = "PeriodCODE";
        public static final String DEFAULT_PERIOD_NAME = "Period name";
        public static final String DEFAULT_TYPE = TYPES.get(0);
        public static final String DEFAULT_DESCRIPTION = "description";
        public static final String DEFAULT_REFERENCE = "reference";
        public static final String DEFAULT_GEOGRAPHY = "geography";
        public static final int DEFAULT_AGE = 1;
        public static final String BC_AGE_RELATION_SPECIFICATION = "BC";
        public static final String AD_AGE_RELATION_SPECIFICATION = "AD";
        public static final String C14_AGE_RELATION_SPECIFICATION = "BP";
        public static final String CALENDAR_YEAR_TYPE = "Calendar";
        public static final String C14_YEAR_TYPE = "Radiometric";
        public static final String RADIOMETRIC_YEAR_TYPE = "Radiometric";

        public static final String DEFAULT_AGE_RELATION = C14_AGE_RELATION_SPECIFICATION;

        private Period instance;

        public PeriodBuilder(){
            instance = PeriodCreator.create(
                    DEFAULT_PERIOD_CODE,
                    DEFAULT_PERIOD_NAME,
                    DEFAULT_TYPE,
                    DEFAULT_DESCRIPTION,
                    DEFAULT_REFERENCE,
                    DEFAULT_GEOGRAPHY,
                    Integer.valueOf(DEFAULT_AGE),
                    C14_AGE_RELATION_SPECIFICATION,
                    Integer.valueOf(DEFAULT_AGE),
                    C14_AGE_RELATION_SPECIFICATION,
                    DEFAULT_AGE_RELATION
            );
        }

        public PeriodBuilder setPeriodCODE(String code){
            instance.setPeriodCode(code);
            return this;
        }

        public PeriodBuilder setName(String name){
            instance.setName(name);
            return this;
        }

        public PeriodBuilder setType(String type){
            instance.setType(type);
            return this;
        }

        public PeriodBuilder setDescription(String description){
            instance.setDesc(description);
            return this;
        }

        public PeriodBuilder setReference(String reference){
            instance.setRef(reference);
            return this;
        }

        public PeriodBuilder setGeography(String geography){
            instance.setGeography(geography);
            return this;
        }

        public PeriodBuilder setBegin(Integer begin){
            instance.setBegin(begin);
            return this;
        }

        public PeriodBuilder setBeginBCAD(String bcad){
            instance.setBeginBCad(bcad);
            return this;
        }

        public PeriodBuilder setEnd(Integer end){
            instance.setEnd(end);
            return this;
        }

        public PeriodBuilder setEndBCAD(String bcad){
            instance.setEndBCad(bcad);
            return this;
        }

        public PeriodBuilder asCalendar(){
            return setYearsType(CALENDAR_YEAR_TYPE);
        }

        public PeriodBuilder setYearsType(String yearsType){
            instance.setYearsType(yearsType);
            return this;
        }

        public PeriodBuilder asC14(){
            return setBeginBCAD(C14_AGE_RELATION_SPECIFICATION)
                    .setEndBCAD(C14_AGE_RELATION_SPECIFICATION)
                    .setYearsType(C14_YEAR_TYPE);
        }

        public PeriodBuilder asRadiometric(){
            return setYearsType(RADIOMETRIC_YEAR_TYPE);
        }

        public Period done(){
            return instance;
        }
    }
}
