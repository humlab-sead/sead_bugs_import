package se.sead.bugsimport.ecocodedefinitiongroups.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.ecocodedefinitiongroups.seadmodel.EcocodeSystem;
import se.sead.repositories.EcocodeSystemRepository;

@Component
public class KochSystemManager {

    private EcocodeSystem kochSystem;
    @Autowired
    private EcocodeSystemRepository systemRepository;

    public EcocodeSystem getKochSystem(){
        if(kochSystem == null){
            kochSystem = systemRepository.findKochSystem();
        }
        return kochSystem;
    }
}
