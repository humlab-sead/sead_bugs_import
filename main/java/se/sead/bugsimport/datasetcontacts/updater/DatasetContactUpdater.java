package se.sead.bugsimport.datasetcontacts.updater;

import org.springframework.stereotype.Component;
import se.sead.bugsimport.site.helper.SiteFromCodeAllowDeletedSite;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.sead.contact.Contact;
import se.sead.sead.data.Dataset;

import java.util.List;

@Component
public class DatasetContactUpdater {

    private SiteFromCodeAllowDeletedSite siteFromCodeAllowDeletedSite;

    public void update(Dataset original, String siteCode){

    }

    private List<Contact> getContactsFromSite(String siteCode){
        BugsTrace latest = siteFromCodeAllowDeletedSite.getLatest(siteCode);
        String compressedBugsData = latest.getCompressedBugsData();
        throw new UnsupportedOperationException();
    }

}
