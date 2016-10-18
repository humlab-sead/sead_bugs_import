package se.sead.bugsimport.datesperiod.converters;

import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.repositories.DatingUncertaintyRepository;
import se.sead.sead.methods.Method;
import se.sead.utils.ErrorCopier;

import java.util.Objects;

public abstract class BaseRelativeDateUpdater{

    private SampleTracerHelper sampleTracerHelper;
    private DatingUncertaintyRepository uncertaintyRepository;
    private RelativeDateMethodManager datingMethodManager;

    protected RelativeDate original;

    public BaseRelativeDateUpdater(
            SampleTracerHelper sampleTracerHelper,
            DatingUncertaintyRepository uncertaintyRepository,
            RelativeDateMethodManager datingMethodManager,
            RelativeDate original) {
        this.sampleTracerHelper = sampleTracerHelper;
        this.uncertaintyRepository = uncertaintyRepository;
        this.datingMethodManager = datingMethodManager;
        this.original = original;
    }

    final void update(){
        boolean updated = setSample();
        updated = setUncertainty() || updated;
        updated = setRelativeAge() || updated;
        updated = setDatingMethod() || updated;
        updated = setNotes() || updated;
        original.setUpdated(updated);
    }

    private boolean setSample(){
        Sample originalSample = original.getSample();
        String bugsSampleCode = getBugsSampleCode();
        if(bugsSampleCode == null || bugsSampleCode.isEmpty()){
            original.addError("No sample specified");
            return false;
        }
        Sample fromLastTrace = sampleTracerHelper.getFromLastTrace(bugsSampleCode);
        if(fromLastTrace == null){
            original.addError("No sample found for code");
            return false;
        }
        original.setSample(fromLastTrace);
        return !Objects.equals(originalSample, fromLastTrace);
    }

    protected abstract String getBugsSampleCode();

    private boolean setUncertainty(){
        DatingUncertainty originalUncertainty = original.getUncertainty();
        DatingUncertainty bugsUncertainty = null;
        String bugsUncertaintyStringValue = getBugsUncertainty();
        if(bugsUncertaintyStringValue != null && !bugsUncertaintyStringValue.equals(" ")){
            bugsUncertainty = uncertaintyRepository.findByName(bugsUncertaintyStringValue);
            if(bugsUncertainty == null){
                original.addError("No uncertainty found for definition");
                return false;
            }
        }
        original.setUncertainty(bugsUncertainty);
        return !Objects.equals(originalUncertainty, bugsUncertainty);
    }

    protected abstract String getBugsUncertainty();

    protected abstract boolean setRelativeAge();

    private boolean setDatingMethod(){
        Method originalDatingMethod = original.getDatingMethod();
        Method relativeDateMethod = datingMethodManager.getRelativeDateMethod(getBugsDatingMethod());
        original.setDatingMethod(relativeDateMethod);
        if(relativeDateMethod != null) {
            ErrorCopier.copyPotentialErrors(original, relativeDateMethod);
        }
        return !Objects.equals(originalDatingMethod, relativeDateMethod);
    }

    protected abstract String getBugsDatingMethod();

    private boolean setNotes(){
        String originalNotes = original.getNotes();
        String bugsNotes = getBugsNotes();
        original.setNotes(bugsNotes);
        return !Objects.equals(originalNotes, bugsNotes);
    }

    protected abstract String getBugsNotes();
}
