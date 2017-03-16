package se.sead.bugsimport.datescalendar.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.datescalendar.bugsmodel.DatesCalendar;
import se.sead.bugsimport.datesperiod.converters.BaseRelativeDateUpdater;
import se.sead.bugsimport.datesperiod.converters.BaseRelativeDatesUpdater;
import se.sead.bugsimport.datesperiod.converters.RelativeDateMethodManager;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.periods.seadmodel.RelativeAge;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.repositories.DataTypeRepository;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatingUncertaintyRepository;
import se.sead.sead.data.DataType;

import java.util.Objects;

@Component
public class RelativeDateUpdaterForCalendar extends BaseRelativeDatesUpdater<DatesCalendar> {

    @Autowired
    private RelativeAgeManager relativeAgeManager;
    @Autowired
    private DatasetMasterRepository datasetMasterRepository;
    @Autowired
    private DataTypeRepository dataTypeRepository;

    @Override
    protected BaseRelativeDateUpdater createUpdater(RelativeDate original, DatesCalendar bugsData) {
        return new Updater(sampleTracerHelper, uncertaintyRepository, datasetMasterRepository, datingMethodManager, dataTypeRepository, original, bugsData);
    }

    private class Updater extends BaseRelativeDateUpdater {

        private DatesCalendar bugsData;
        private DataTypeRepository dataTypeRepository;

        public Updater(
                SampleTracerHelper sampleTracerHelper,
                DatingUncertaintyRepository uncertaintyRepository,
                DatasetMasterRepository datasetMasterRepository,
                RelativeDateMethodManager datingMethodManager,
                DataTypeRepository dataTypeRepository,
                RelativeDate original,
                DatesCalendar bugsData) {
            super(uncertaintyRepository, datasetMasterRepository, sampleTracerHelper, datingMethodManager, original);
            this.dataTypeRepository = dataTypeRepository;
            this.bugsData = bugsData;
        }

        @Override
        protected String getBugsSampleCode() {
            return bugsData.getSample();
        }

        @Override
        protected String getBugsUncertainty() {
            return bugsData.getUncertainty();
        }

        @Override
        protected String getBugsDatesId() {
            return bugsData.getCalendarCODE();
        }

        @Override
        protected DataType getDataType() {
            return dataTypeRepository.findByName("Calendar date");
        }

        @Override
        protected boolean setRelativeAge() {
            RelativeAge originalRelativeAge = original.getRelativeAge();
            if(bugsData.getBcadbp() == null){
                original.addError("No BCADBP specified");
                return false;
            }
            RelativeAge bugsBasedRelativeAge = relativeAgeManager.getOrCreateRelativeAge(bugsData);
            original.setRelativeAge(bugsBasedRelativeAge);
            return !Objects.equals(originalRelativeAge, bugsBasedRelativeAge);
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
