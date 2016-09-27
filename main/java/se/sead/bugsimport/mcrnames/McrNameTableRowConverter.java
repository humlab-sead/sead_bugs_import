package se.sead.bugsimport.mcrnames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.converters.McrNameTracerHelper;
import se.sead.bugsimport.mcrnames.converters.McrNameUpdater;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;
import se.sead.bugsimport.mcrnames.search.MCRSearch;

import java.util.List;

@Component
public class McrNameTableRowConverter implements BugsTableRowConverter<BugsMCRNames, MCRName> {

    @Autowired
    private List<MCRSearch> searchStrategies;
    @Autowired
    private McrNameUpdater updater;

    @Override
    public MCRName convertForDataRow(BugsMCRNames bugsData) {
        MCRName found = MCRSearch.NO_MCR_NAME_FOUND;
        for (MCRSearch searchStrategy :
                searchStrategies) {
            found = searchStrategy.findFor(bugsData);
            if(found != MCRSearch.NO_MCR_NAME_FOUND){
                break;
            }
        }
        if(found == MCRSearch.NO_MCR_NAME_FOUND){
            return create(bugsData);
        } else {
            return update(found, bugsData);
        }
    }

    private MCRName create(BugsMCRNames bugsData){
        return update(new MCRName(), bugsData);
    }

    private MCRName update(MCRName original, BugsMCRNames bugsData){
        updater.update(original, bugsData);
        return original;
    }
}
