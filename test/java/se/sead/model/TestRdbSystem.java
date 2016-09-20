package se.sead.model;

import se.sead.bugsimport.locations.seadmodel.Location;
import se.sead.bugsimport.rdbsystems.seadmodel.RdbSystem;
import se.sead.sead.model.Biblio;

public class TestRdbSystem extends RdbSystem {

    private TestRdbSystem(Integer id){
        super.setId(id);
    }

    public static RdbSystem create(Integer id, String systemName, String version, Integer systemDate, Short firstPublished, Biblio reference, Location country){
        RdbSystem system = new TestRdbSystem(id);
        system.setSystemName(systemName);
        system.setVersion(version);
        system.setSystemDate(systemDate);
        system.setFirstPublished(firstPublished);
        system.setReference(reference);
        system.setLocation(country);
        return system;
    }
}
