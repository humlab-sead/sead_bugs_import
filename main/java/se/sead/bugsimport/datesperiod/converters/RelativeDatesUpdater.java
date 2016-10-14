package se.sead.bugsimport.datesperiod.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.repositories.DatingUncertaintyRepository;
import se.sead.sead.methods.Method;
import se.sead.utils.ErrorCopier;

import java.util.Objects;

@Component
public class RelativeDatesUpdater {

    @Autowired
    private SampleTracerHelper sampleTracerHelper;
    @Autowired
    private DatingUncertaintyRepository uncertaintyRepository;
    @Autowired
    private PeriodTraceHelper periodTraceHelper;
    @Autowired
    private RelativeDateMethodManager datingMethodManager;

    public void update(RelativeDate original, DatesPeriod bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private RelativeDate original;
        private DatesPeriod bugsData;

        Updater(RelativeDate original, DatesPeriod bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            boolean updated = setSample();
            updated = setUncertainty() || updated;
            updated = setRelativeAge() || updated;
            updated = setDatingMethod() || updated;
            updated = setNotes() || updated;
            original.setUpdated(updated);
        }

        private boolean setSample(){
            Sample originalSample = original.getSample();
            if(bugsData.getSampleCode() == null || bugsData.getSampleCode().isEmpty()){
                original.addError("No sample specified");
                return false;
            }
            Sample fromLastTrace = sampleTracerHelper.getFromLastTrace(bugsData.getSampleCode());
            if(fromLastTrace == null){
                original.addError("No sample found for code");
                return false;
            }
            original.setSample(fromLastTrace);
            return !Objects.equals(originalSample, fromLastTrace);
        }

        private boolean setUncertainty(){
            DatingUncertainty originalUncertainty = original.getUncertainty();
            DatingUncertainty bugsUncertainty = null;
            if(bugsData.getUncertainty() != null && !bugsData.getUncertainty().equals(" ")){
                bugsUncertainty = uncertaintyRepository.findByName(bugsData.getUncertainty());
                if(bugsUncertainty == null){
                    original.addError("No uncertainty found for definition");
                    return false;
                }
            }
            original.setUncertainty(bugsUncertainty);
            return !Objects.equals(originalUncertainty, bugsUncertainty);
        }

        private boolean setRelativeAge(){
            RelativeAge originalRelativeAge = original.getRelativeAge();
            if(bugsData.getPeriodCode() == null || bugsData.getPeriodCode().isEmpty()){
                original.addError("No period code");
                return false;
            }
            RelativeAge fromLastTrace = periodTraceHelper.getFromLastTrace(bugsData.getPeriodCode());
            if(fromLastTrace == null){
                original.addError("No period found for code");
                return false;
            }
            original.setRelativeAge(fromLastTrace);
            return !Objects.equals(originalRelativeAge, fromLastTrace);
        }

        private boolean setDatingMethod(){
            Method originalDatingMethod = original.getDatingMethod();
            Method relativeDateMethod = datingMethodManager.getRelativeDateMethod(bugsData.getDatingMethod());
            original.setDatingMethod(relativeDateMethod);
            if(relativeDateMethod != null) {
               ErrorCopier.copyPotentialErrors(original, relativeDateMethod);
            }
            return !Objects.equals(originalDatingMethod, relativeDateMethod);
        }

        private boolean setNotes(){
            String originalNotes = original.getNotes();
            original.setNotes(bugsData.getNotes());
            return !Objects.equals(originalNotes, bugsData.getNotes());
        }
    }
}
