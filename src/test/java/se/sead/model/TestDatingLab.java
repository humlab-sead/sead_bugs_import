package se.sead.model;

import se.sead.bugsimport.lab.seadmodel.DatingLab;
import se.sead.bugsimport.locations.seadmodel.Location;

public class TestDatingLab extends DatingLab {

    private TestDatingLab(Integer id){
        setId(id);
    }

    public static DatingLab create(
            Integer id,
            String labId,
            String name,
            Location country
    ){
        DatingLab lab = new TestDatingLab(id);
        lab.setLabId(labId);
        lab.setName(name);
        lab.setCountry(country);
        return lab;
    }
}
