package se.sead.bugsimport.lab.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.repositories.DatingLabRepository;

@Component
@Order(2)
public class DatingLabByLabIdSearch implements DatingLabSearch {

    @Autowired
    private DatingLabRepository repository;

    @Override
    public DatingLab findFor(BugsLab bugsLab) {
        DatingLab found = repository.findByLabId(bugsLab.getLabId());
        if(found == null){
            return NO_LAB_FOUND;
        } else {
            return found;
        }
    }
}
