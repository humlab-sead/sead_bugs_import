package se.sead.bugsimport.sample.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.countsheets.converters.SampleGroupBugsTraceReader;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.sample.bugsmodel.BugsSample;
import se.sead.bugsimport.sample.seadmodel.Sample;
import se.sead.utils.ErrorCopier;

import java.util.Objects;

@Component
public class SampleUpdater {

    @Autowired
    private SampleGroupBugsTraceReader sampleGroupBugsTraceReader;
    @Autowired
    private DimensionUpdater dimensionUpdater;

    public void update(Sample original, BugsSample bugsData){
        new Updater(original, bugsData).update();
    }

    private class Updater {
        private Sample original;
        private BugsSample bugsData;
        private boolean updated;

        Updater(Sample original, BugsSample bugsData) {
            this.original = original;
            this.bugsData = bugsData;
        }

        void update(){
            checkXorYValues();
            updated = setName();
            updated = setGroup() || updated;
            updated = setDimensions() || updated;
            if(updated){
                original.setUpdated(true);
            }
        }

        private boolean setName(){
            String originalName = original.getName();
            original.setName(bugsData.getRefNrContext());
            return !Objects.equals(originalName, bugsData.getRefNrContext());
        }

        private boolean setGroup(){
            if(bugsData.getCountSheetCode() == null ||
                bugsData.getCountSheetCode().isEmpty()){
                original.addError("No countsheet specified");
                return false;
            }
            SampleGroup originalGroup = original.getGroup();
            SampleGroup groupByLastTrace = sampleGroupBugsTraceReader.getFromLastTrace(bugsData.getCountSheetCode());
            if(groupByLastTrace == null){
                original.addError("No countsheet found");
                return false;
            }
            original.setGroup(groupByLastTrace);
            ErrorCopier.copyPotentialErrors(original, groupByLastTrace);
            return Objects.equals(originalGroup, groupByLastTrace);
        }

        private void checkXorYValues(){
            if((bugsData.getX() != null && !bugsData.getX().isEmpty()) ||
                    (bugsData.getY() != null && !bugsData.getY().isEmpty())){
                original.addError("X or Y values is probably an error");
            }
        }

        private boolean setDimensions(){
            return dimensionUpdater.update(original, bugsData);
        }
    }

}
