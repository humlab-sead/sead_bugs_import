package se.sead.bugsimport.siteotherproxies.converters;

public enum BugsProxyToSeadRecordTypeNames {

      Pollen("External pollen data")
    , PlantMacro("External plant macro data")
    , Diatoms("Diatoms")
    , Chironomids("Chironomids")
    , SoilChemistry("Soil chemistry/properties")
    , Isotopes("Isotopes")
    , AnimalBones("Animal bones")
    , Archaeology("Other archaeology")
    , Molluscs("Molluscs")
    ;

    private String seadName;

    private BugsProxyToSeadRecordTypeNames(String seadName){
        this.seadName = seadName;
    }

    public String getSeadName(){
        return seadName;
    }
}
