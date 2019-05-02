package se.sead.bugsimport.datesperiod.converters;

import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.bugsimport.sample.converters.SampleTracerHelper;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.repositories.DatasetMasterRepository;
import se.sead.repositories.DatingUncertaintyRepository;
import se.sead.sead.data.AnalysisEntity;
import se.sead.sead.data.DataType;
import se.sead.sead.data.Dataset;
import se.sead.sead.methods.Method;
import se.sead.utils.ErrorCopier;

import java.util.Objects;

public abstract class BaseRelativeDateUpdater{

    private SampleTracerHelper sampleTracerHelper;
    private DatingUncertaintyRepository uncertaintyRepository;
    private RelativeDateMethodManager datingMethodManager;
    private DatasetMasterRepository datasetMasterRepository;

    protected RelativeDate original;

    public BaseRelativeDateUpdater(
            DatingUncertaintyRepository uncertaintyRepository,
            DatasetMasterRepository datasetMasterRepository,
            SampleTracerHelper sampleTracerHelper,
            RelativeDateMethodManager datingMethodManager,
            RelativeDate original) {
        this.datasetMasterRepository = datasetMasterRepository;
        this.sampleTracerHelper = sampleTracerHelper;
        this.uncertaintyRepository = uncertaintyRepository;
        this.datingMethodManager = datingMethodManager;
        this.original = original;
    }

    final void update(){
        boolean updated = setSample();
        updated = setUncertainty() || updated;
        updated = setRelativeAge() || updated;
        updated = setDataset() || updated;
        updated = setNotes() || updated;
        original.setUpdated(updated);
    }

    private boolean setSample(){
        AnalysisEntity originalAnalysisEntity = original.getAnalysisEntity();
        if(originalAnalysisEntity == null){
            originalAnalysisEntity = new AnalysisEntity();
            original.setAnalysisEntity(originalAnalysisEntity);
        }
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
        originalAnalysisEntity.setSample(fromLastTrace);
        return !Objects.equals(originalAnalysisEntity.getSample(), fromLastTrace);
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

    private boolean setDataset(){
        if(!original.isErrorFree()){
            return false;
        } else if(original.getRelativeAge() == null){
            throw new NullPointerException("Must set relative age before adding dating method to relative dates");
        } else if(original.getAnalysisEntity() == null){
            throw new NullPointerException("Must set sample before setting dataset");
        }
        RelativeDateDatasetUpdater datasetUpdater =
                new RelativeDateDatasetUpdater(
                        getDataType(),
                        datingMethodManager.getRelativeDateMethod(getBugsDatingMethod(), original.getRelativeAge()),
                        getBugsDatesId(),
                        datasetMasterRepository.findBugsMasterSet());
        Dataset originalDataset = original.getAnalysisEntity().getDataset();
        Dataset dataset = datasetUpdater.update(originalDataset);
        ErrorCopier.copyPotentialErrors(original, dataset);
        original.getAnalysisEntity().setDataset(dataset);
        return dataset.isUpdated();
    }

    protected abstract DataType getDataType();

    protected abstract String getBugsDatesId();

    protected abstract String getBugsDatingMethod();

    private boolean setNotes(){
        String originalNotes = original.getNotes();
        String bugsNotes = getBugsNotes();
        original.setNotes(bugsNotes);
        return !Objects.equals(originalNotes, bugsNotes);
    }

    protected abstract String getBugsNotes();
}
