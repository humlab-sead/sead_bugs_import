package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BugsListSeadMapping<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private BugsType bugsData;
    private List<SeadType> seadData;
    protected Boolean cachedIsErrorFree = null;
    protected Boolean cachedIsNewData = null;
    protected Boolean cachedIsUpdated = null;
    private List<SeadType> storedSeadData;

    public BugsListSeadMapping(BugsType bugsData, List<SeadType> seadData){
        this.bugsData = bugsData;
        if(seadData == null){
            this.seadData = Collections.EMPTY_LIST;
        } else {
            this.seadData = seadData;
        }
    }

    public boolean isNewSeadData() {
        if(cachedIsNewData == null){
            cachedIsNewData = seadData.stream().anyMatch(seadData -> seadData.isNewItem());
        }
        return cachedIsNewData;
    }

    public boolean isUpdatedSeadData() {
        if(cachedIsUpdated == null){
            cachedIsUpdated = seadData.stream().anyMatch(seadData -> seadData.isUpdated());
        }
        return cachedIsUpdated;
    }

    public boolean isErrorFree() {
        if(cachedIsErrorFree == null){
            cachedIsErrorFree = seadData.stream().allMatch(seadType -> seadType.isErrorFree());
        }
        return cachedIsErrorFree;
    }

    public BugsType getBugsData() {
        return bugsData;
    }

    public List<SeadType> getSeadData(){
        return seadData;
    }

    public List<String> getErrorMessages(){
        List<String> errors = new ArrayList<>();
        seadData.forEach(seadType -> errors.addAll(seadType.getErrorMessages()));
        return errors;
    }

    public void setSavedData(SeadType preStoredVersion, SeadType postStoredVersion){
        if(storedSeadData == null){
            storedSeadData = new ArrayList<>(seadData.size());
        }
        storedSeadData.add(postStoredVersion);
    }

    public void resyncStoredItems() {
        seadData = storedSeadData;
    }
}
