package se.sead.model;

import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.siteotherproxies.seadmodel.SiteOtherRecord;
import se.sead.sead.model.Biblio;
import se.sead.sead.recordtypes.RecordType;

public class TestSiteOtherRecord extends SiteOtherRecord {

    private TestSiteOtherRecord(Integer id){
        super.setId(id);
    }

    public static SiteOtherRecord create(Integer id, RecordType recordType, SeadSite seadSite, Biblio reference){
        SiteOtherRecord otherRecord = new TestSiteOtherRecord(id);
        otherRecord.setSite(seadSite);
        otherRecord.setRecordType(recordType);
        otherRecord.setReference(reference);
        return otherRecord;
    }
}
