package se.sead.bugsimport.countsheets.converters;

import se.sead.bugsimport.countsheets.bugsmodel.Countsheet;
import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.sead.methods.Method;
import se.sead.sead.model.SamplingContext;

class SampleGroupCreator {

    private Countsheet bugsData;
    private SeadSite site;
    private SamplingContext context;
    private Method temporaryImportMethod;
    private SampleGroup seadVersion;

    SampleGroupCreator(Countsheet bugsData, SeadSite site, SamplingContext context, Method temporaryImportMethod) {
        this.bugsData = bugsData;
        this.site = site;
        this.context = context;
        this.temporaryImportMethod = temporaryImportMethod;
    }

    SampleGroup create(){
        seadVersion = new SampleGroup();
        setSamplingContext();
        setSamplingMehtod();
        setSite();
        setName();
        return seadVersion;
    }

    private void setSamplingContext(){
        if(bugsData.getContext() == null || bugsData.getContext().isEmpty()){
            seadVersion.addError("Empty context not allowed");
        } else if(context == null){
            seadVersion.addError("Unknown context: " + bugsData.getContext());
        } else {
            seadVersion.setSamplingContext(context);
        }
    }

    private void setSamplingMehtod(){
        seadVersion.setSamplingMethod(temporaryImportMethod);
    }

    private void setSite(){
        if(site == null){
            seadVersion.addError("No site found: " + bugsData.getSiteCode());
        } else {
            seadVersion.setSite(site);
        }
    }

    private void setName(){
        if(bugsData.getName() == null || bugsData.getName().isEmpty()){
            seadVersion.addError("Empty name not allowed");
        } else {
            seadVersion.setName(bugsData.getName());
        }
    }
}
