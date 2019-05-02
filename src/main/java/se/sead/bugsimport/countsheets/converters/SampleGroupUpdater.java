package se.sead.bugsimport.countsheets.converters;

import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.sead.methods.Method;
import se.sead.sead.model.SamplingContext;

public class SampleGroupUpdater extends SampleGroupCreator {

    public SampleGroupUpdater(Countsheet bugsData, SeadSite site, SamplingContext context, Method temporaryImportMethod) {
        super(bugsData, site, context, temporaryImportMethod);
    }

    public SampleGroup update(SampleGroup existingVersion){
        SampleGroup importVersion = create();
        if(!importVersion.isErrorFree()){
            return importVersion;
        } else if(!existingVersion.equals(importVersion)){
            existingVersion
                .setUpdated(
                    doUpdates(existingVersion, importVersion)
                );
        }
        return existingVersion;
    }

    private boolean doUpdates(SampleGroup existingVersion, SampleGroup importVersion){
        boolean updated = updateName(existingVersion, importVersion);
        updated = updated || updateSite(existingVersion, importVersion);
        updated = updated || updateContext(existingVersion, importVersion);
        return updated;
    }

    private boolean updateName(SampleGroup existingVersion, SampleGroup importVersion) {
        if(!existingVersion.getName().equals(importVersion.getName())){
            existingVersion.setName(importVersion.getName());
            return true;
        }
        return false;
    }

    private boolean updateSite(SampleGroup existingVersion, SampleGroup importVersion) {
        if(!existingVersion.getSite().equals(importVersion.getSite())){
            existingVersion.setSite(importVersion.getSite());
            return true;
        }
        return false;
    }

    private boolean updateContext(SampleGroup existingVersion, SampleGroup importVersion) {
        if(!existingVersion.getSamplingContext().equals(importVersion.getSamplingContext())){
            existingVersion.setSamplingContext(importVersion.getSamplingContext());
            return true;
        }
        return false;
    }
}
