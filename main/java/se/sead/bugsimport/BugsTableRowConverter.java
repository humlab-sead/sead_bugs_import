package se.sead.bugsimport;

/**
 * Created by erer0001 on 2016-04-27.
 */
public interface BugsTableRowConverter<BugsType, SeadType> {

    public SeadType convertForDataRow(BugsType bugsData);
}
