package se.sead.bugsimport.rdbcodes.search;

import se.sead.bugsimport.rdbcodes.bugsmodel.BugsRDBCodes;
import se.sead.bugsimport.rdbcodes.seadmodel.RdbCode;

public interface SearchStrategy {
    public static final RdbCode NO_CODE_FOUND = new RdbCode();

    RdbCode get(BugsRDBCodes bugsData);
}
