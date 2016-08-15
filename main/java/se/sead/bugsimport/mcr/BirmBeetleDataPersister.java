package se.sead.bugsimport.mcr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.mcr.bugsmodel.BirmBeetleDat;
import se.sead.bugsimport.Persister;
import se.sead.bugsimport.mcr.seadmodel.BirmBeetleData;
import se.sead.repositories.BirmBeetleDataRepository;

/**
 * Created by erer0001 on 2016-05-13.
 */
@Component
public class BirmBeetleDataPersister extends Persister<BirmBeetleDat, BirmBeetleData> {

    @Autowired
    private BirmBeetleDataRepository dataRepository;

    @Override
    protected BirmBeetleData save(BirmBeetleData seadValue) {
        return dataRepository.saveOrUpdate(seadValue);
    }
}
