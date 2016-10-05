package se.sead.lab;

import se.sead.bugsimport.lab.bugsmodel.BugsLab;

import java.util.Arrays;
import java.util.List;

class ExpectedBugsData {

    static final List<BugsLab> EXPECTED_DATA =
            Arrays.asList(
                   create("A", "Arizona", "USA"),
                    create("B", "Bern", "Switzerland"),
                    create("BC*", "Brooklyn College", "USA"),
                    create("Birm", "Birmingham", "USA???"),
                    create("Test", null, "Sweden"),
                    create("Test 2", "Test without country", null),
                    create("Test 3", "Update", "USA"),
                    create("Test 4", "Sead data changed", "USA")
            );

    private static BugsLab create(
            String id,
            String name,
            String country
    ){
        return create(id, name, null, country, null, null,null);
    }

    private static BugsLab create(
            String id,
            String name,
            String address,
            String country,
            String telephone,
            String webSite,
            String email){
        BugsLab lab = new BugsLab();
        lab.setLabId(id);
        lab.setLabName(name);
        lab.setAddress(address);
        lab.setCountry(country);
        lab.setTelephone(telephone);
        lab.setWebsite(webSite);
        lab.setEmail(email);
        return lab;
    }
}
