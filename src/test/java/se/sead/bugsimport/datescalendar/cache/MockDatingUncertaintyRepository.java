package se.sead.bugsimport.datescalendar.cache;

import se.sead.bugsimport.datesradio.seadmodel.DatingUncertainty;
import se.sead.repositories.DatingUncertaintyRepository;

class MockDatingUncertaintyRepository implements DatingUncertaintyRepository {

    @Override
    public DatingUncertainty findOne(Integer id) {
        return null;
    }

    @Override
    public DatingUncertainty findByName(String uncertaintySymbol) {
        DatingUncertainty uncertainty = new DatingUncertainty();
        uncertainty.setName(uncertaintySymbol);
        return uncertainty;
    }
}
