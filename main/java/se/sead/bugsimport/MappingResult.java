package se.sead.bugsimport;

import javafx.collections.ObservableList;
import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class MappingResult<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private List<BugsListSeadMapping<BugsType, SeadType>> data;

    public MappingResult(){
        data = new ArrayList<>();
    }

    public void add(BugsType bugsData, List<SeadType> seadItems){
        data.add(new BugsListSeadMapping<BugsType, SeadType>(bugsData, seadItems));
    }

    protected void add(BugsListSeadMapping<BugsType, SeadType> mapping){
        data.add(mapping);
    }

    public List<BugsListSeadMapping<BugsType, SeadType>> getData(){ return data;}

    public static class BugsListSeadMapping<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

        private BugsType bugsData;
        private List<SeadType> seadData;
        private Boolean cachedIsErrorFree = null;
        private Boolean cachedIsNewData = null;
        private Boolean cachedIsUpdated = null;
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
            seadData.forEach(seadType -> errors.addAll(seadType.getErrors()));
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
}
