package se.sead.model;

import se.sead.bugsimport.site.seadmodel.SeadSite;

import java.math.BigDecimal;

public class TestSeadSite extends SeadSite {

    private TestSeadSite(Integer id){
        super.setId(id);
    }

    public static SeadSite create(Integer id, String name, String nationalId, BigDecimal latitude, BigDecimal longitude, BigDecimal altitude, String description){
        SeadSite site = new TestSeadSite(id);
        site.setName(name);
        site.setNationalSiteIdentifier(nationalId);
        site.setLatitude(latitude);
        site.setLongitude(longitude);
        site.setAltitude(altitude);
        site.setDescription(description);
        return site;
    }
}
