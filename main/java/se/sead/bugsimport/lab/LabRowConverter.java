package se.sead.bugsimport.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.converter.DatingLabUpdater;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.lab.search.DatingLabSearch;

import java.util.List;

import static se.sead.bugsimport.lab.search.DatingLabSearch.NO_LAB_FOUND;

@Component
public class LabRowConverter implements BugsTableRowConverter<BugsLab, DatingLab> {

    @Autowired
    private List<DatingLabSearch> searchStrategies;
    @Autowired
    private DatingLabUpdater updater;

    @Override
    public DatingLab convertForDataRow(BugsLab bugsData) {
        DatingLab found = NO_LAB_FOUND;
        for (DatingLabSearch search :
                searchStrategies) {
            found = search.findFor(bugsData);
            if(found != NO_LAB_FOUND){
                break;
            }
        }
        if(found == NO_LAB_FOUND){
            return create(bugsData);
        } else if(found.isErrorFree()){
            return update(found, bugsData);
        } else {
            return found;
        }
    }

    private DatingLab create(BugsLab bugsLab){
        return update(new DatingLab(), bugsLab);
    }

    private DatingLab update(DatingLab original, BugsLab bugsLab){
        updater.update(original, bugsLab);
        return original;
    }
}
