package se.sead.bugsimport;

import se.sead.bugs.TraceableBugsData;
import se.sead.sead.model.LoggableEntity;

import java.util.List;

public interface MappingResult<BugsType extends TraceableBugsData, SeadType extends LoggableEntity> {
    void add(BugsType bugsData, List<SeadType> seadItems);

    List<BugsListSeadMapping<BugsType, SeadType>> getData();
}
