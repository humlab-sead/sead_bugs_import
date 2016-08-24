package se.sead.bugsimport.taxaseasonality.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.seadmodel.Season;
import se.sead.bugsimport.taxaseasonality.seadmodel.SeasonType;
import se.sead.repositories.SeasonRepository;
import se.sead.repositories.SeasonTypeRepository;

@Component
public class SeasonActiveAdultSeasonConverter {

    enum BugsSeadSeasonConversion {
          Ja("January")
        , Fe("February")
        , Mr("March")
        , Ap("April")
        , My("May")
        , Jn("June")
        , Jy("July")
        , Au("August")
        , Se("September")
        , Sep("September")
        , Oc("October")
        , No("November")
        , De("December")
        ;
        private String seadName;
        BugsSeadSeasonConversion(String seadName){
            this.seadName = seadName;
        }

        String getSeadName(){
            return seadName;
        }
    }

    @Autowired
    private SeasonRepository seasonRepository;
    private SeasonType monthType;

    private Season noSeasonFoundForName;
    private Season noSeasonSpecified;

    @Autowired
    public SeasonActiveAdultSeasonConverter(SeasonTypeRepository seasonTypeRepository){
        monthType = seasonTypeRepository.getBySeasonType("Month");
        noSeasonFoundForName = new Season();
        noSeasonFoundForName.addError("Unknown season");
        noSeasonSpecified = new Season();
        noSeasonSpecified.addError("Season cannot be empty");
    }

    public Season getSeason(SeasonActiveAdult bugsData){
        if(isEmptySeason(bugsData)){
            return noSeasonSpecified;
        }
        try {
            BugsSeadSeasonConversion conversion = BugsSeadSeasonConversion.valueOf(bugsData.getSeason());
            Season foundSeason = seasonRepository.findByNameAndType(conversion.getSeadName(), monthType);
            if(foundSeason != null){
                return foundSeason;
            }
        } catch (IllegalArgumentException iae){}
        return noSeasonFoundForName;
    }

    private boolean isEmptySeason(SeasonActiveAdult bugsData){
        return bugsData.getSeason() == null || bugsData.getSeason().isEmpty();
    }
}
