package se.sead.taxaseasonality;

import se.sead.bugsimport.taxaseasonality.bugsmodel.SeasonActiveAdult;
import se.sead.bugsimport.taxaseasonality.seadmodel.ActivityType;
import se.sead.bugsimport.taxaseasonality.seadmodel.SeasonType;
import se.sead.bugsimport.taxaseasonality.seadmodel.TaxaSeasonality;
import se.sead.model.TestTaxaSeasonality;
import se.sead.repositories.LocationRepository;
import se.sead.repositories.SeasonRepository;
import se.sead.repositories.SpeciesRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ImportTestDefinition {

    /*
        Ap
        Au
        De
        Fe
        Ja
        Jn
        Jy
        Mr
        My
        No
        Oc
        Se
        Sep
     */

    static final List<SeasonActiveAdult> EXPECTED_BUGS_DATA =
            Arrays.asList(
                create(1.0000001d, "Ja", "UK"),
                create(1.0000002d, "Fe", "UK"),
                create(1.0000003d, "Mr", "UK"),
                create(1.0000004d, "Ap", "UK"),
                create(1.0000005d, "My", "UK"),
                create(1.0000006d, "Jn", "UK"),
                create(1.0000007d, "Jy", "UK"),
                create(1.0000008d, "Au", "UK"),
                create(1.0000009d, "Sep", "UK"),
                create(1.0000010d, "Oc", "UK"),
                create(1.0000011d, "No", "UK"),
                create(1.0000012d, "De", "UK"),
                create(1.0000013d, "Ja", "UK"),
                create(1.0000013d, "Fe", "UK"),
                create(1.0000013d, "Mr", "UK"),
                create(1.0000014d, "My", "UK"),
                create(1.0000014d, "Jn", "UK"),
                create(1.0000014d, "Jy", "UK"),
                create(1.0000014d, "Au", "Eg"),
                create(1.0000014d, "Sep", "Eg"),
                create(1.0000015d, "Ja", "Ger"),
                create(1.0000016d, "Ja", "Swe"),
                create(1.0000017d, "Au", "Int"),
                create(1.0000018d, "Au", null),
                create(1.0000019d, null, "UK"),
                create(2.0000000d, "Au", "UK"),
                create(1.0000020d, "Au", "Qq"),
                create(1.0000021d, "We", "UK"),
                create(1.0000022d, "Se", "UK"),
                create(1.0000023d, "Ja", "UK"),
                create(1.0000024d, "Fe", "UK"),
                create(1.0000025d, "Ja", "UK"),
                create(1.0000025d, "Fe", "UK"),
                create(1.0000026d, "Ja", "UK"),
                create(1.0000026d, "Fe", "UK"),
                create(1.0000026d, "Mr", "UK")
            );

    private static SeasonActiveAdult create(Double code, String season, String countryCode){
        SeasonActiveAdult seasonActiveAdult = new SeasonActiveAdult();
        seasonActiveAdult.setCode(code);
        seasonActiveAdult.setSeason(season);
        seasonActiveAdult.setCountryCode(countryCode);
        return seasonActiveAdult;
    }

    private ActivityType adultActiveType;
    private SeasonType monthType;
    private SeasonRepository seasonRepository;
    private SpeciesRepository speciesRepository;
    private LocationRepository locationRepository;

    ImportTestDefinition(
            ActivityType adultActiveType,
            SeasonType monthType,
            SeasonRepository seasonRepository,
            SpeciesRepository speciesRepository,
            LocationRepository locationRepository){
        this.adultActiveType = adultActiveType;
        this.monthType = monthType;
        this.seasonRepository = seasonRepository;
        this.speciesRepository = speciesRepository;
        this.locationRepository = locationRepository;
    }

    List<TaxaSeasonality> getExpectedData(){
        List<TaxaSeasonality> expectedData = new ArrayList<>();
        expectedData.add(createNew(1, "January", "United Kingdom"));
        expectedData.add(createNew(2, "February", "United Kingdom"));
        expectedData.add(createNew(3, "March", "United Kingdom"));
        expectedData.add(createNew(4, "April", "United Kingdom"));
        expectedData.add(createNew(5, "May", "United Kingdom"));
        expectedData.add(createNew(6, "June", "United Kingdom"));
        expectedData.add(createNew(7, "July", "United Kingdom"));
        expectedData.add(createNew(8, "August", "United Kingdom"));
        expectedData.add(createNew(9, "September", "United Kingdom"));
        expectedData.add(createNew(10, "October", "United Kingdom"));
        expectedData.add(createNew(11, "November", "United Kingdom"));
        expectedData.add(createNew(12, "December", "United Kingdom"));
        expectedData.add(createNew(13, "January", "United Kingdom"));
        expectedData.add(createNew(13, "February", "United Kingdom"));
        expectedData.add(createNew(13, "March", "United Kingdom"));
        expectedData.add(createNew(14, "May", "United Kingdom"));
        expectedData.add(createNew(14, "June", "United Kingdom"));
        expectedData.add(createNew(14, "July", "United Kingdom"));
        expectedData.add(createNew(14, "August", "Egypt"));
        expectedData.add(createNew(14, "September", "Egypt"));
        expectedData.add(createNew(15, "January", "Germany"));
        expectedData.add(createNew(16, "January", "Sweden"));
        expectedData.add(createNew(17, "August", "International"));
        expectedData.add(createNew(22, "September", "United Kingdom"));
        expectedData.add(createExisting(1, 23, "January", "United Kingdom"));
        expectedData.add(createExisting(2, 24, "January", "United Kingdom"));
        expectedData.add(createExisting(3, 25, "January", "United Kingdom"));
        expectedData.add(createNew(25, "February", "United Kingdom"));
        expectedData.add(createNew(26, "January", "United Kingdom"));
        expectedData.add(createExisting(4, 26, "February", "United Kingdom"));
        expectedData.add(createExisting(5, 26, "March", "United Kingdom"));
        expectedData.add(createExisting(6, 26, "April", "United Kingdom"));
        return expectedData;
    }

    private TaxaSeasonality createNew(int speciesId, String seasonName, String countryName){
        return createExisting(null, speciesId, seasonName, countryName);
    }

    private TaxaSeasonality createExisting(Integer seasonalityId, int speciesId, String seasonName, String countryName) {
        return
                TestTaxaSeasonality.create(seasonalityId,
                        adultActiveType,
                        seasonRepository.findByNameAndType(seasonName, monthType),
                        speciesRepository.findOne(speciesId),
                        locationRepository.findCountryByName(countryName)
                );
    }
}
