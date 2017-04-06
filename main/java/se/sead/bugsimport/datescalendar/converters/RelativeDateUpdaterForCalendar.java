package se.sead.bugsimport.datescalendar.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private RelativeAgeManager relativeAgeManager;
    private DatasetMasterRepository datasetMasterRepository;
    private DataType relativeDateType;

    @Autowired
    public RelativeDateUpdaterForCalendar(
            RelativeAgeManager relativeAgeManager,
            DatasetMasterRepository datasetMasterRepository,
            DataTypeRepository dataTypeRepository,
            @Value("@{relative.calendar.date.data.type=Calendar date}") String defaultCalendarDateDataType) {
        this.relativeAgeManager = relativeAgeManager;
        this.datasetMasterRepository = datasetMasterRepository;
        relativeDateType = dataTypeRepository.findByName(defaultCalendarDateDataType);
    }

    @Override
    protected BaseRelativeDateUpdater createUpdater(RelativeDate original, DatesCalendar bugsData) {
        return new Updater(sampleTracerHelper, uncertaintyRepository, datasetMasterRepository, datingMethodManager, relativeDateType, original, bugsData);
    }

    private class Updater extends BaseRelativeDateUpdater {

        private DatesCalendar bugsData;
        private DataType relativeDateDataType;

        public Updater(
                SampleTracerHelper sampleTracerHelper,
                DatingUncertaintyRepository uncertaintyRepository,
                DatasetMasterRepository datasetMasterRepository,
                RelativeDateMethodManager datingMethodManager,
                DataType relativeDateDataType,
                RelativeDate original,
                DatesCalendar bugsData) {
            super(uncertaintyRepository, datasetMasterRepository, sampleTracerHelper, datingMethodManager, original);
            this.bugsData = bugsData;
            this.relativeDateDataType = relativeDateDataType;
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
            return relativeDateDataType;
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
