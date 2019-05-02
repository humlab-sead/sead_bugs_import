package se.sead.bugsimport.datesperiod.converters;

import org.springframework.beans.factory.annotation.Autowired;
import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.repositories.DatingUncertaintyRepository;

public abstract class BaseRelativeDatesUpdater<BugsType> {

    @Autowired
    protected SampleTracerHelper sampleTracerHelper;
    @Autowired
    protected DatingUncertaintyRepository uncertaintyRepository;
    @Autowired
    protected RelativeDateMethodManager datingMethodManager;

    public void update(RelativeDate original, BugsType bugsType){
        createUpdater(original, bugsType).update();
    }

    protected abstract BaseRelativeDateUpdater createUpdater(RelativeDate original, BugsType bugsData);
}
