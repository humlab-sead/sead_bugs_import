package se.sead.bugsimport.datesperiod.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datesperiod.bugsmodel.DatesPeriod;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.converters.PeriodTraceHelper;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatingUncertaintyRepository;
import se.sead.sead.data.DataType;

import java.util.Objects;

@Component
public class RelativeDatesUpdaterForPeriod extends BaseRelativeDatesUpdater<DatesPeriod>{

    @Autowired
    private PeriodTraceHelper periodTraceHelper;
    @Autowired
    private DatasetMasterRepository datasetMasterRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Override
    protected BaseRelativeDateUpdater createUpdater(RelativeDate original, DatesPeriod bugsData){
        return new Updater(
                sampleTracerHelper,
                uncertaintyRepository,
                datingMethodManager,
                dataTypeRepository,
                original,
                bugsData
        );
    }

    private class Updater extends BaseRelativeDateUpdater {

        private DataTypeRepository dataTypeRepository;
        private DatesPeriod bugsData;

        public Updater(
                SampleTracerHelper sampleTracerHelper,
                DatingUncertaintyRepository uncertaintyRepository,
                RelativeDateMethodManager datingMethodManager,
                DataTypeRepository dataTypeRepository,
                RelativeDate original,
                DatesPeriod bugsData) {
            super(uncertaintyRepository, datasetMasterRepository, sampleTracerHelper, datingMethodManager, original);
            this.bugsData = bugsData;
            this.dataTypeRepository = dataTypeRepository;
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
        protected DataType getDataType() {
            return dataTypeRepository.findByName("Uncalibrated dates");
        }

        @Override
        protected String getBugsDatesId() {
            return bugsData.getPeriodDateCode();
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
