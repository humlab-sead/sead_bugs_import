package se.sead.bugsimport.site.conversion;

import org.springframework.util.StringUtils;
import se.sead.testutils.BigDecimalDefinition;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;

import java.math.BigDecimal;

class SeadSiteCreator {
    private BugsSite bugsVersion;
    private SeadSite seadVersion;
    private String errorMessage;

    public SeadSiteCreator(BugsSite bugsVersion) {
        this.bugsVersion = bugsVersion;
    }

    public SeadSiteCreator(BugsSite bugsVersion, String errorMessage) {
        this.bugsVersion = bugsVersion;
        this.errorMessage = errorMessage;
    }

    SeadSite create(){
        seadVersion = new SeadSite();
        setName();
        setAltitude();
        setLatitude();
        setLongitude();
        setNationalIdentifier();
        setDescription();
        setCustomErrorMessage();
        return seadVersion;
    }

    private void setName() {
        if(StringUtils.isEmpty(bugsVersion.getName())){
            seadVersion.addError("Site name cannot be empty");
        }
        seadVersion.setName(bugsVersion.getName());
    }

    private void setAltitude() {
        seadVersion.setAltitude(convertToBigDecimal(bugsVersion.getAlt()));
    }

    private static BigDecimal convertToBigDecimal(Float bugsVersion){
        return BigDecimalDefinition.convertToSeadContext(bugsVersion);
    }

    private void setLatitude() {
        seadVersion.setLatitude(convertToBigDecimal(bugsVersion.getLatDD()));
    }

    private void setLongitude() {
        seadVersion.setLongitude(convertToBigDecimal(bugsVersion.getLongDD()));
    }

    private void setNationalIdentifier() {
        seadVersion.setNationalSiteIdentifier(bugsVersion.getNgr());
    }

    private void setDescription() {
        seadVersion.setDescription(bugsVersion.getInterp());
    }

    private void setCustomErrorMessage(){
        if(!StringUtils.isEmpty(errorMessage)){
            seadVersion.addError(errorMessage);
        }
    }
}
