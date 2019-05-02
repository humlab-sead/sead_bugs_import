package se.sead.model;

import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.methods.Method;

public class TestRelativeDate extends RelativeDate {

    private TestRelativeDate(Integer id){
        setId(id);
    }

    public static RelativeDate create(
            Integer id,
            DatingUncertainty uncertainty,
            RelativeAge relativeAge,
            String notes,
            AnalysisEntity analysisEntity
    ){
        RelativeDate relativeDate = new TestRelativeDate(id);
        relativeDate.setUncertainty(uncertainty);
        relativeDate.setRelativeAge(relativeAge);
        relativeDate.setNotes(notes);
        relativeDate.setAnalysisEntity(analysisEntity);
        return relativeDate;
    }
}
