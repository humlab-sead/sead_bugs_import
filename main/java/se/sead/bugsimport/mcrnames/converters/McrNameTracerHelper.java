package se.sead.bugsimport.mcrnames.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.bugsmodel.MCRNamesBugsTable;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.bugsimport.tracing.SeadDataFromTraceHelper;
import se.sead.repositories.McrNamesRepository;

@Component
public class McrNameTracerHelper extends SeadDataFromTraceHelper<BugsMCRNames, MCRName> {

    @Autowired
    public McrNameTracerHelper(McrNamesRepository mcrNamesRepository){
        super(MCRNamesBugsTable.TABLE_NAME, false, mcrNamesRepository);
    }

}
