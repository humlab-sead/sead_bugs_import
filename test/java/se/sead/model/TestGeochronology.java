package se.sead.model;

import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.datesradio.seadmodel.Geochronology;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.sead.data.AnalysisEntity;

import java.math.BigDecimal;

public class TestGeochronology extends Geochronology {

    private TestGeochronology(Integer id){
        setId(id);
    }

    public static Geochronology create(
            Integer id,
            AnalysisEntity ae,
            DatingLab lab,
            DatingUncertainty datingUncertainty,
            String labNumber,
            BigDecimal age,
            BigDecimal errorOlder,
            BigDecimal errorYounger,
            String notes
    ){
        Geochronology geochron = new TestGeochronology(id);
        geochron.setNotes(notes);
        geochron.setAnalysisEntity(ae);
        geochron.setDatingLaboratory(lab);
        geochron.setAge(age);
        geochron.setErrorOlder(errorOlder);
        geochron.setErrorYounger(errorYounger);
        geochron.setLabSampleNumber(labNumber);
        geochron.setUncertainty(datingUncertainty);
        return geochron;
    }
}
