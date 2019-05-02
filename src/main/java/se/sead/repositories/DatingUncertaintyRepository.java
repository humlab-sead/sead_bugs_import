package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;

public interface DatingUncertaintyRepository extends Repository<DatingUncertainty, Integer> {
    DatingUncertainty findOne(Integer id);

    DatingUncertainty findByName(String uncertaintySymbol);
}
