package se.sead.bugsimport.datesperiod.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.repositories.DatingUncertaintyRepository;

import java.util.Objects;

@Component
public class RelativeDatesUpdaterForPeriod extends BaseRelativeDatesUpdater<DatesPeriod>{

    @Autowired
    private PeriodTraceHelper periodTraceHelper;

    @Override
    protected BaseRelativeDateUpdater createUpdater(RelativeDate original, DatesPeriod bugsData){
        return new Updater(
                sampleTracerHelper,
                uncertaintyRepository,
                datingMethodManager,
                original,
                bugsData
        );
    }

    private class Updater extends BaseRelativeDateUpdater {

        private DatesPeriod bugsData;

        public Updater(SampleTracerHelper sampleTracerHelper, DatingUncertaintyRepository uncertaintyRepository, RelativeDateMethodManager datingMethodManager, RelativeDate original, DatesPeriod bugsData) {
            super(sampleTracerHelper, uncertaintyRepository, datingMethodManager, original);
            this.bugsData = bugsData;
        }

        @Override
        protected boolean setRelativeAge(){
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

        @Override
        protected String getBugsSampleCode() {
            return bugsData.getSampleCode();
        }

        @Override
        protected String getBugsUncertainty() {
            return bugsData.getUncertainty();
        }

        @Override
        protected String getBugsDatingMethod() {
            return bugsData.getDatingMethod();
        }

        @Override
        protected String getBugsNotes() {
            return bugsData.getNotes();
        }
    }
}
