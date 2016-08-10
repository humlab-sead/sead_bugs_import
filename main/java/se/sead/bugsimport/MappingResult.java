package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MappingResult<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {

    private List<BugsListSeadMapping<BugsType, SeadType>> data;
    private List<BugsSingleSeadMapping<BugsType, SeadType>> dataTuples;

    public MappingResult(){
        dataTuples = new ArrayList<>();
        data = new ArrayList<>();
    }

    @Deprecated
    public List<BugsSingleSeadMapping<BugsType, SeadType>> getTupleData(){return dataTuples;}

    @Deprecated
    public void add(BugsType bugsData, SeadType seadItem){
        dataTuples.add(new BugsSingleSeadMapping<>(bugsData, seadItem));
    }

    public void add(BugsType bugsData, List<SeadType> seadItems){
        data.add(new BugsListSeadMapping<BugsType, SeadType>(bugsData, seadItems));
    }

    public List<BugsListSeadMapping<BugsType, SeadType>> getData(){ return data;}

    @Deprecated
    public static class BugsSingleSeadMapping<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {
        private BugsType bugsData;
        private SeadType seadData;

        BugsSingleSeadMapping(BugsType bugsData, SeadType seadData){
            this.bugsData = bugsData;
            this.seadData = seadData;
        }

        public boolean isNewSeadData(){
            return seadData.isNewItem();
        }
        public boolean isUpdatedSeadData() {return seadData.isUpdated();}
        public boolean isErrorFree(){return seadData.isErrorFree();}

        public BugsType getBugsData() {
            return bugsData;
        }

        public SeadType getSeadData() {
            return seadData;
        }

        public void setSavedSeadData(SeadType newSeadData){
            this.seadData = newSeadData;
        }

    }

    public static class BugsListSeadMapping<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {
        private BugsType bugsData;
        private List<SeadType> seadData;

        BugsListSeadMapping(BugsType bugsData, List<SeadType> seadData){
            this.bugsData = bugsData;
            if(seadData == null){
                this.seadData = Collections.EMPTY_LIST;
            } else {
                this.seadData = seadData;
            }
        }

        public boolean isNewSeadData() {
            return seadData.stream().anyMatch(seadData -> seadData.isNewItem());
        }

        public boolean isUpdatedSeadData() {
            return seadData.stream().anyMatch(seadData -> seadData.isUpdated());
        }

        public boolean isErrorFree() {
            return seadData.stream().allMatch(seadType -> seadType.isErrorFree());
        }

        public BugsType getBugsData() {
            return bugsData;
        }

        public List<SeadType> getSeadData(){
            return seadData;
        }

        public List<String> getErrorMessages(){
            List<String> errors = new ArrayList<>();
            seadData.stream().forEach(seadType -> errors.addAll(seadType.getErrors()));
            return errors;
        }

        public void setSavedData(SeadType preStoredVersion, SeadType postStoredVersion){
            int positionOfPreStoredVersion = seadData.indexOf(preStoredVersion);
            seadData.set(positionOfPreStoredVersion, postStoredVersion);
        }
    }
}
