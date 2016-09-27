package se.sead.model;

import se.sead.bugsimport.mcrnames.seadmodel.MCRName;

public class TestMCRName extends MCRName {

    private TestMCRName(Integer id){
        setId(id);
    }

    public static MCRName create(Integer id, String notes, String shortName, Short number, String mcrName){
        MCRName name = new TestMCRName(id);
        name.setMcrName(mcrName);
        name.setMcrNumber(number);
        name.setShortName(shortName);
        name.setNotes(notes);
        return name;
    }
}
