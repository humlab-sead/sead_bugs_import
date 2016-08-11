package se.sead.model;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;
import se.sead.sead.model.Biblio;

public class TestSiteReference extends SiteReference {

    private TestSiteReference(Integer id){
        setId(id);
    }

    public static SiteReference create(Integer id, SeadSite site, Biblio reference){
        SiteReference siteReference = new TestSiteReference(id);
        siteReference.setSite(site);
        siteReference.setReference(reference);
        return siteReference;
    }
}
