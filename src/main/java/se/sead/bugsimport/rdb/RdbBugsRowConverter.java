package se.sead.bugsimport.rdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.converter.RdbUpdater;
import se.sead.bugsimport.rdb.seadmodel.Rdb;
import se.sead.bugsimport.rdb.search.RdbSearch;

import java.util.List;

@Component
public class RdbBugsRowConverter implements BugsTableRowConverter<BugsRDB, Rdb> {


    @Autowired
    private List<RdbSearch> rdbSearchStrategies;

    @Autowired
    private RdbUpdater rdbUpdater;

    @Override
    public Rdb convertForDataRow(BugsRDB bugsData) {
        Rdb found = null;
        for (RdbSearch search :
                rdbSearchStrategies) {
            found = search.findFor(bugsData);
            if(found != RdbSearch.NO_RDB_FOUND){
                break;
            }
        }
        if(found == RdbSearch.NO_RDB_FOUND){
            return create(bugsData);
        } else {
            return update(found, bugsData);
        }
    }

    private Rdb create(BugsRDB bugsData){
        return update(new Rdb(), bugsData);
    }

    private Rdb update(Rdb original, BugsRDB bugsData){
        rdbUpdater.update(original, bugsData);
        return original;
    }
}
