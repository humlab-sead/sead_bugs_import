package se.sead.bugsimport.rdb.search;

import se.sead.bugsimport.rdb.bugsmodel.BugsRDB;
import se.sead.bugsimport.rdb.seadmodel.Rdb;

public interface RdbSearch {

    Rdb NO_RDB_FOUND = new Rdb();

    Rdb findFor(BugsRDB rdb);
}
