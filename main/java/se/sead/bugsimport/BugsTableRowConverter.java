package se.sead.bugsimport;

import java.util.Arrays;
import java.util.List;

/**
 * Created by erer0001 on 2016-04-27.
 */
public interface BugsTableRowConverter<BugsType, SeadType> {

    default SeadType convertForDataRow(BugsType bugsData){
        return null;
    }

    default List<SeadType> convertListForDataRow(BugsType bugsData){
        return Arrays.asList(convertForDataRow(bugsData));
    }

    default boolean returnsMultipleRows(){return false;}
}
