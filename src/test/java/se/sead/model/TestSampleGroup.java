package se.sead.model;

import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.sead.methods.Method;
import se.sead.sead.model.SamplingContext;

public class TestSampleGroup extends SampleGroup {

    private TestSampleGroup(Integer id){
        setId(id);
    }

    public static SampleGroup create(Integer id, String name, String description, SeadSite site, SamplingContext context, Method method){
        SampleGroup group = new TestSampleGroup(id);
        group.setName(name);
        group.setDescription(description);
        group.setSite(site);
        group.setSamplingContext(context);
        group.setSamplingMethod(method);
        return group;
    }
}
