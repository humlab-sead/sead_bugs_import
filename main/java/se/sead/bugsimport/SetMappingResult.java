package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetMappingResult<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> implements MappingResult<BugsType, SeadType> {

    private Set<BugsListSeadMapping<BugsType, SeadType>> data;

    public SetMappingResult(){
        data = new HashSet<>();
    }

    @Override
    public void add(BugsType bugsData, List<SeadType> seadItems) {
        data.add(new BugsSetSeadMapping<>(bugsData, seadItems));
    }

    @Override
    public List<BugsListSeadMapping<BugsType, SeadType>> getData() {
        return new ArrayList<>(data);
    }

    private static class BugsSetSeadMapping<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> extends BugsListSeadMapping<BugsType, SeadType> {

        public BugsSetSeadMapping(BugsType bugsData, List<SeadType> seadData) {
            super(bugsData, seadData);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BugsListSeadMapping<?, ?> that = (BugsListSeadMapping<?, ?>) o;
            BugsType bugsData = getBugsData();
            List<SeadType> seadData = getSeadData();

            if (bugsData != null ? !bugsData.equals(that.getBugsData()) : that.getBugsData() != null) return false;
            if (seadData != null ? !seadData.equals(that.getSeadData()) : that.getSeadData() != null) return false;
            if (cachedIsErrorFree != null ? !cachedIsErrorFree.equals(that.cachedIsErrorFree) : that.cachedIsErrorFree != null)
                return false;
            if (cachedIsNewData != null ? !cachedIsNewData.equals(that.cachedIsNewData) : that.cachedIsNewData != null)
                return false;
            return cachedIsUpdated != null ? cachedIsUpdated.equals(that.cachedIsUpdated) : that.cachedIsUpdated == null;
        }

        @Override
        public int hashCode() {
            BugsType bugsData = getBugsData();
            List<SeadType> seadData = getSeadData();
            int result = bugsData != null ? bugsData.hashCode() : 0;
            result = 31 * result + (seadData != null ? seadData.hashCode() : 0);
            result = 31 * result + (cachedIsErrorFree != null ? cachedIsErrorFree.hashCode() : 0);
            result = 31 * result + (cachedIsNewData != null ? cachedIsNewData.hashCode() : 0);
            result = 31 * result + (cachedIsUpdated != null ? cachedIsUpdated.hashCode() : 0);
            return result;
        }
    }
}
