package se.sead.bugsimport.sitereferences.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsTableRowConverter;
import se.sead.bugsimport.site.helper.SiteFromCodeDisallowDeletedSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.sitereferences.bugsmodel.BugsSiteRef;
import se.sead.bugsimport.sitereferences.seadmodel.SiteReference;
import se.sead.repositories.BiblioDataRepository;
import se.sead.repositories.SiteReferenceRepository;
import se.sead.sead.model.Biblio;

import java.util.List;

@Component
public class SiteReferenceBugsTableRowConverter implements BugsTableRowConverter<BugsSiteRef, SiteReference> {

    @Autowired
    private SiteFromCodeDisallowDeletedSite siteHelper;
    @Autowired
    private BiblioDataRepository biblioRepository;
    @Autowired
    private SiteReferenceRepository siteReferenceRepository;

    @Override
    public SiteReference convertForDataRow(BugsSiteRef bugsData) {
        SeadSite seadSite = siteHelper.getSeadSiteFromBugsCode(bugsData.getSiteCode());
        if(seadSite == null){
            return createMissingSiteErrorReference(bugsData);
        }
        Biblio reference = biblioRepository.getByBugsReferenceIgnoreCase(bugsData.getRef());
        if(reference == null){
            return createMissingBibliographyError(bugsData);
        }
        return createOrUpdate(seadSite, reference);
    }

    private SiteReference createMissingSiteErrorReference(BugsSiteRef bugsData) {
        return createError("Missing site: " + bugsData.getSiteCode());
    }

    private SiteReference createError(String error) {
        SiteReference siteReference = new SiteReference();
        siteReference.addError(error);
        return siteReference;
    }

    private SiteReference createMissingBibliographyError(BugsSiteRef bugsData) {
        return createError("Missing reference: " + bugsData.getRef());
    }

    private SiteReference createOrUpdate(SeadSite seadSite, Biblio reference) {
        List<SiteReference> existing = siteReferenceRepository.findAllBySite(seadSite);
        SiteReference versionFromBugsData = createNew(seadSite, reference);
        if(existing.isEmpty() || !existing.contains(versionFromBugsData)){
            return versionFromBugsData;
        } else {
            return existing.get(existing.indexOf(versionFromBugsData));
        }
    }

    private SiteReference createNew(SeadSite seadSite, Biblio reference) {
        SiteReference siteReference = new SiteReference();
        siteReference.setReference(reference);
        siteReference.setSite(seadSite);
        return siteReference;
    }
}
