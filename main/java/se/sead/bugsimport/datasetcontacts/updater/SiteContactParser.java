package se.sead.bugsimport.datasetcontacts.updater;

public class SiteContactParser {

    private String compressedBugsSiteInformation;
    private String identifiedBy;
    private String specimenRepository;
    private boolean parsed = false;

    public SiteContactParser(String compressedBugsSiteInformation){
        this.compressedBugsSiteInformation = compressedBugsSiteInformation;
    }


    public String getIdentifiedBy() {
        if(!parsed){
            parse();
        }
        return identifiedBy;
    }

    private void parse(){
        String[] information = compressedBugsSiteInformation
                .substring(1,compressedBugsSiteInformation.length() -1)
                .split(",");
        if(information.length < 10){
            throw new IllegalStateException("compressed site information does not conform to original layout.");
        }
        identifiedBy = information[8];
        specimenRepository = information[10];
        parsed = true;
    }

    public String getSpecimenRepository() {
        if(!parsed){
            parse();
        }
        return specimenRepository;
    }
}
