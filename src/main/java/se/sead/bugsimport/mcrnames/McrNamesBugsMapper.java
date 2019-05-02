package se.sead.bugsimport.mcrnames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.bugsmodel.MCRNamesBugsTable;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;

@Component
public class McrNamesBugsMapper extends BugsSeadMapper<BugsMCRNames, MCRName> {

    @Autowired
    public McrNamesBugsMapper(
            McrNameTableRowConverter singleBugsTableRowConverterForMapper) {
        super(new MCRNamesBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
