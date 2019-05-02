package se.sead.bugsimport.sitelocations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocation;
import se.sead.bugsimport.sitelocations.bugsmodel.BugsSiteLocationBugsTable;
import se.sead.bugsimport.sitelocations.converters.SiteLocationBugsTableRowConverter;
import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

@Component
public class SiteLocationBugsMapper extends BugsSeadMapper<BugsSiteLocation, SiteLocation> {

    @Autowired
    public SiteLocationBugsMapper(
            SiteLocationBugsTableRowConverter singleBugsTableRowConverterForMapper) {
        super(new BugsSiteLocationBugsTable(), singleBugsTableRowConverterForMapper);
    }
}
