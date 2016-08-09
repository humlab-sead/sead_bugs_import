package se.sead.site;

import se.sead.BigDecimalDefinition;
import se.sead.bugsimport.country.seadmodel.LocationType;
import se.sead.bugsimport.site.bugsmodel.BugsSite;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.site.seadmodel.SiteLocation;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.bugsimport.tracing.seadmodel.BugsTraceType;
import se.sead.bugsimport.translations.model.TypeTranslation;
import se.sead.model.TestLocation;
import se.sead.model.TestSeadSite;
import se.sead.model.TestSiteLocation;
import se.sead.repositories.LocationTypeRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SiteTestDefinition {

    static final List<BugsSite> EXPECTED_BUGS_DATA =
            Arrays.asList(
                    // existing items
                    create("SITE000006", "Abingdon: Stert Street", "Oxfordshire", "England", "SU 498973", 51.672222f, -1.2811111f, 60f, "Robinson", "15th-16th C well/pit with a few calcified remains of Porcellio dilatatus, cf. Fannia sp and Sphaeroceridae.\r\nNot quantified.", "University of Oxford"),
                    create("SITE000008", "Agerod V", "Skåne", "Sweden", null, 55.934105f, 13.400202f, 39f, "Lemdahl", "Ageröd V. Specimens hand picked by the excavators of a mesolithic midden.", null),
                    // locations exist
                    create("SITE000009", "Aghnadarragh", "Antrim", "Ireland", "NG 457642", 54.596592f, -6.255964f, 30f, "Coope", "Woody detritus peat overlying organic silt between Early Midlandian and Late Midlandian tills. [Dates need checking - duplicate/missing lab numbers, SSR should be SRR?]\r\nU8  Detrital mud.\r\nU6  Woody detrital peat.", null),
                    // no country exists for Greece
                    create("SITE000010", "Akrotiri", "Santorini", "Greece", null, 36.046227f, 25.419664f, 10f, "Panagiotakopulu", "Largely pests of stored products, preserved by both charring and desiccation by tephra burying Late Bronze Age town, ca. 1600 BC.", "Edinburgh University, UK"),

                    // created items
                    create("SITE000011", "Alcester: Alluvial", "Warwickshire", "England", "SP 099577", 52.2175f, -1.8563889f, 38f, "Osborne", "Early Holocene alluvial sequence in River Alne preserved beneath land slip.\r\nAdditional date for site: Layer G 4160 +/- 290 Birm-696 is much too old.", "University of Birmingham, UK"),
                    create("SITE000012", "Alcester: Coulter's Garage", "Warwickshire", "England", "SP 088575", 52.215557f, -1.8725f, 43f, "Girling", "Late Iron Age natural assemblage adjacent to Roman town.", null),
                    create("SITE000013", "Alcester: Roman Pit", "Warwickshire", "England", "SP 089570", 52.211113f, -1.8711112f, 43f, "Osborne", "3rd C AD pit in a Roman town.", "University of Birmingham, UK."),

                    // no country exists for egypt
                    create("SITE000017", "el Amarna: Workmens Village", "Minya", "Egypt", null, 27.629723f, 30.915f, 65f, "Panagiotakopulu", "Coprolites from Workmen's Village, XVIII Dynasty Amarna.\r\n1 sample, species presence only.\r\nNOTE: Attagenus  sp. = Attagenus (Telopes)_astacurus Peyer. (which is not as yet in the Bugs checklist)", "Edinburgh University, UK & Amarna Site magazine, Egypt"),
                    create("SITE000018", "Amiens", "Somme", "France", null, 49.887363f, 2.301486f, 46f, "Yvinec", "Late 2nd C ad Roman granary, charred material.", null),
                    create("SITE000019", "Endletvatn", "Andoya", "Norway", null, 69.73333f, 19.083334f, null, "Fjellberg", "mid-Weichselian sediments dated by 14C to 12-18,000 BP, part of a continuous sequence. At a depth of 7-10m rich in fragment of chironomids, incl Corycera ambigua, Cladocera and mites.", null),
                    create("SITE000020", "Ängdala", "Skåne", "Sweden", null, 55.583332f, 13.116667f, null, "Lemdahl", "Ängdala. 3 Lateglacial into Early Holocene gyttja and peat successions.\r\nFull list not published.\r\n[1 sample, species presence only.]", null),
                    create("SITE001274", "Møen unlocated 1896", "Møen", "Denmark", null, null, null, null, "Henriksen", "Holocene peat.", null)
            );

    static BugsSite create(
            String code,
            String name,
            String region,
            String country,
            String ngr,
            Float latDD,
            Float longDD,
            Float alt,
            String idBy,
            String interp,
            String speciments){
        BugsSite site = new BugsSite();
        site.setCode(code);
        site.setName(name);
        site.setRegion(region);
        site.setCountry(country);
        site.setNgr(ngr);
        site.setLatDD(latDD);
        site.setLongDD(longDD);
        site.setAlt(alt);
        site.setiDBy(idBy);
        site.setInterp(interp);
        site.setSpecimens(speciments);
        return site;
    }

    private boolean canCreateCountry;
    private boolean canUpdateSite;

    SiteTestDefinition(boolean canCreateCountry, boolean canUpdateSite){
        this.canCreateCountry = canCreateCountry;
        this.canUpdateSite = canUpdateSite;
    }


    List<SeadSite> getExpectedResults(LocationTypeRepository typeRepository){
        LocationType countryType = typeRepository.getCountryType();
        LocationType administrativeRegionType = typeRepository.getAdministrativeRegionType();
        LocationType aggregateRegionType = typeRepository.getAggregateNonAdministrativeRegionType();
        SeadSitePreStoredBuilder existingBuilder = new SeadSitePreStoredBuilder(countryType, administrativeRegionType, canUpdateSite, canCreateCountry);
        SeadSiteCreatedBuilder creationBuilder = new SeadSiteCreatedBuilder(countryType, administrativeRegionType, aggregateRegionType, canCreateCountry);
        List<SeadSite> expectedResults = new ArrayList<>();
        expectedResults.addAll(existingBuilder.buildExistingSites());
        expectedResults.addAll(creationBuilder.buildCreatedSites());
        return expectedResults;
    }

    List<TypeTranslation> getTypeTranslations(){
        return new TestSiteTypeTranslationBuilder().getTranslations();
    }

    static BigDecimal convertToSeadFormat(Float floatValue){
        return BigDecimalDefinition.convertToSeadContext(floatValue);
    }

    private static class SeadSitePreStoredBuilder {

        private LocationType countryType;
        private LocationType administrativeRegionType;
        private boolean canUpdateSite;
        private boolean canCreateCountry;

        SeadSitePreStoredBuilder(LocationType countryType, LocationType administrativeRegionType, boolean canUpdateSite, boolean canCreateCountry){
            this.countryType = countryType;
            this.administrativeRegionType = administrativeRegionType;
            this.canUpdateSite = canUpdateSite;
            this.canCreateCountry = canCreateCountry;
        }

        List<SeadSite> buildExistingSites(){
            return Arrays.asList(
                buildAbingdonSite(),
                buildAgerodSite(),
                buildAghnadarragh()
            );
        }

        private SeadSite buildAbingdonSite() {
            SeadSite site_000006 = TestSeadSite.create(
                    1,
                    "Abingdon: Stert Street",
                    "SU 498973",
                    createSeadBigDecimal(51.67222f),
                    createSeadBigDecimal(-1.281111f),
                    createSeadBigDecimal(60f),
                    "15th-16th C well/pit with a few calcified remains of Porcellio dilatatus, cf. Fannia sp and Sphaeroceridae.\r\nNot quantified.");
            List<SiteLocation> abingdonSiteLocations;
            if(canUpdateSite && canCreateCountry) {
                abingdonSiteLocations = Arrays.asList(
                        TestSiteLocation.create(1, TestLocation.create(747, "Oxfordshire", administrativeRegionType), site_000006),
                        TestSiteLocation.create(null, TestLocation.create(null, "United Kingdom", countryType), site_000006)
                );
            } else {
                abingdonSiteLocations = Arrays.asList(
                        TestSiteLocation.create(1, TestLocation.create(747, "Oxfordshire", administrativeRegionType), site_000006),
                        TestSiteLocation.create(2, TestLocation.create(240, "England", countryType), site_000006)
                );
            }
            site_000006.setSiteLocations(abingdonSiteLocations);
            return site_000006;
        }

        private BigDecimal createSeadBigDecimal(Float floatValue){
            return SiteTestDefinition.convertToSeadFormat(floatValue);
        }

        private SeadSite buildAgerodSite(){
            SeadSite site_000008 = TestSeadSite.create(
                    2,
                    "Agerod V",
                    null,
                    createSeadBigDecimal(55.934105f),
                    createSeadBigDecimal(13.400202f),
                    createSeadBigDecimal(39f),
                    "Ageröd V. Specimens hand picked by the excavators of a mesolithic midden.");
            List<SiteLocation> agerodLocations = Arrays.asList(
                    TestSiteLocation.create(3, TestLocation.create(780, "Skåne", administrativeRegionType), site_000008),
                    TestSiteLocation.create(4, TestLocation.create(205, "Sweden", countryType), site_000008)
            );
            site_000008.setSiteLocations(agerodLocations);
            return site_000008;
        }

        private SeadSite buildAghnadarragh() {
            //"Aghnadarragh", "Antrim", "Ireland", "NG 457642", 54.596592f, -6.255964f, 30f, "Coope", "Woody detritus peat overlying organic silt between Early Midlandian and Late Midlandian tills. [Dates need checking - duplicate/missing lab numbers, SSR should be SRR?]\r\nU8  Detrital mud.\r\nU6  Woody detrital peat.", null),
            String expectedDescription = canUpdateSite ?
                    "Woody detritus peat overlying organic silt between Early Midlandian and Late Midlandian tills. [Dates need checking - duplicate/missing lab numbers, SSR should be SRR?]\r\nU8  Detrital mud.\r\nU6  Woody detrital peat." :
                    "Woody detritus peat overlying";
            SeadSite site_000009 = TestSeadSite.create(
                    3,
                    "Aghnadarragh",
                    "NG 457642",
                    createSeadBigDecimal(54.596592f),
                    createSeadBigDecimal(-6.255964f),
                    createSeadBigDecimal(30f),
                    expectedDescription);
            List<SiteLocation> aghnadarraghLocations = Arrays.asList(
                    TestSiteLocation.create(null, TestLocation.create(590, "Antrim", administrativeRegionType), site_000009),
                    TestSiteLocation.create(null, TestLocation.create(105, "Ireland", countryType), site_000009)
            ) ;
            site_000009.setSiteLocations(aghnadarraghLocations);
            return site_000009;
        }
    }

    private static class SeadSiteCreatedBuilder {

        private LocationType countryType;
        private LocationType administrativeRegionType;
        private LocationType aggregateRegionType;
        private boolean canCreateCountry;

        SeadSiteCreatedBuilder(LocationType countryType, LocationType administrativeRegionType, LocationType aggregateRegionType, boolean canCreateCountry){
            this.countryType = countryType;
            this.administrativeRegionType = administrativeRegionType;
            this.aggregateRegionType = aggregateRegionType;
            this.canCreateCountry = canCreateCountry;
        }


        List<SeadSite> buildCreatedSites(){
            List<SeadSite> createdSites = new ArrayList<>();
            createdSites.add(buildAlcesterAlluvial());
            createdSites.add(buildAlcesterCoulters());
            createdSites.add(buildAlcesterRoman());
            createdSites.add(buildAmiens());
            createdSites.add(buildEndletvatn());
            createdSites.add(buildAngdala());
            createdSites.add(buildMoen());
            if(canCreateCountry){
                createdSites.add(buildAkrotiri());
                createdSites.add(buildElAmarna());
            }
            return createdSites;
        }

        private BigDecimal createSeadBigDecimal(Float floatValue){
            return SiteTestDefinition.convertToSeadFormat(floatValue);
        }

        private SeadSite buildAkrotiri(){
            //"Akrotiri", "Santorini", "Greece", null, 36.046227f, 25.419664f, 10f, "Panagiotakopulu", "Largely pests of stored products, preserved by both charring and desiccation by tephra burying Late Bronze Age town, ca. 1600 BC.", "Edinburgh University, UK"),
            SeadSite site_000010 = TestSeadSite.create(
                    null,
                    "Akrotiri",
                    null,
                    createSeadBigDecimal(36.046227f),
                    createSeadBigDecimal(25.419664f),
                    createSeadBigDecimal(10f),
                    "Largely pests of stored products, preserved by both charring and desiccation by tephra burying Late Bronze Age town, ca. 1600 BC.");
            List<SiteLocation> akrotiriLocations = Arrays.asList(
                    TestSiteLocation.create(null, TestLocation.create(770, "Santorini", aggregateRegionType), site_000010),
                    TestSiteLocation.create(null, TestLocation.create(null, "Greece", countryType), site_000010)
            );
            site_000010.setSiteLocations(akrotiriLocations);
            return site_000010;
        }

        private SeadSite buildAlcesterAlluvial(){
            //"Alcester: Alluvial", "Warwickshire", "England", "SP 099577", 52.2175f, -1.8563889f, 38f, "Osborne", "Early Holocene alluvial sequence in River Alne preserved beneath land slip.\r\nAdditional date for site: Layer G 4160 +/- 290 Birm-696 is much too old.", "University of Birmingham, UK"),
            SeadSite site_000011 = TestSeadSite.create(
                    null,
                    "Alcester: Alluvial",
                    "SP 099577",
                    createSeadBigDecimal(52.2175f),
                    createSeadBigDecimal(-1.8563889f),
                    createSeadBigDecimal(38f),
                    "Early Holocene alluvial sequence in River Alne preserved beneath land slip.\r\nAdditional date for site: Layer G 4160 +/- 290 Birm-696 is much too old.");
            List<SiteLocation> alcesterLocations = Arrays.asList(
                TestSiteLocation.create(null, TestLocation.create(819, "Warwickshire", administrativeRegionType), site_000011),
                TestSiteLocation.create(null, TestLocation.create(240, "England", countryType), site_000011)
            );
            site_000011.setSiteLocations(alcesterLocations);
            return site_000011;
        }

        private SeadSite buildAlcesterCoulters(){
            //"Alcester: Coulter's Garage", "Warwickshire", "England", "SP 088575", 52.215557f, -1.8725f, 43f, "Girling", "Late Iron Age natural assemblage adjacent to Roman town.", null),
            SeadSite site_000012 = TestSeadSite.create(
                    null,
                    "Alcester: Coulter's Garage",
                    "SP 088575",
                    createSeadBigDecimal(52.215557f),
                    createSeadBigDecimal(-1.8725f),
                    createSeadBigDecimal(43f),
                    "Late Iron Age natural assemblage adjacent to Roman town.");
            site_000012.setSiteLocations(
                    Arrays.asList(
                        TestSiteLocation.create(null, TestLocation.create(819,"Warwickshire", administrativeRegionType), site_000012),
                        TestSiteLocation.create(null, TestLocation.create(240, "England", countryType), site_000012)
                    )
            );
            return site_000012;
        }

        private SeadSite buildAlcesterRoman(){
            //"Alcester: Roman Pit", "Warwickshire", "England", "SP 089570", 52.211113f, -1.8711112f, 43f, "Osborne", "3rd C AD pit in a Roman town.", "University of Birmingham, UK."),
            SeadSite site_000013 = TestSeadSite.create(
                    null,
                    "Alcester: Roman Pit",
                    "SP 089570",
                    createSeadBigDecimal(52.211113f),
                    createSeadBigDecimal(-1.8711112f),
                    createSeadBigDecimal(43f),
                    "3rd C AD pit in a Roman town.");
            site_000013.setSiteLocations(Arrays.asList(
                    TestSiteLocation.create(null, TestLocation.create(819, "Warwickshire", administrativeRegionType), site_000013),
                    TestSiteLocation.create(null, TestLocation.create(240, "England", countryType), site_000013)
            ));
            return site_000013;
        }

        private SeadSite buildElAmarna(){
            //"el Amarna: Workmens Village", "Minya", "Egypt", null, 27.629723f, 30.915f, 65f, "Panagiotakopulu", "Coprolites from Workmen's Village, XVIII Dynasty Amarna.\r\n1 sample, species presence only.\r\nNOTE: Attagenus  sp. = Attagenus (Telopes)_astacurus Peyer. (which is not as yet in the Bugs checklist)", "Edinburgh University, UK & Amarna SeadSite magazine, Egypt"),
            SeadSite site_000017 = TestSeadSite.create(
                    null,
                    "el Amarna: Workmens Village",
                    null,
                    createSeadBigDecimal(27.629723f),
                    createSeadBigDecimal(30.915f),
                    createSeadBigDecimal(65f),
                    "Coprolites from Workmen's Village, XVIII Dynasty Amarna.\r\n1 sample, species presence only.\r\nNOTE: Attagenus  sp. = Attagenus (Telopes)_astacurus Peyer. (which is not as yet in the Bugs checklist)" );
            site_000017.setSiteLocations(Arrays.asList(
                    TestSiteLocation.create(null, TestLocation.create(null, "Minya", administrativeRegionType), site_000017),
                    TestSiteLocation.create(null, TestLocation.create(null, "Egypt", countryType), site_000017)
            ));
            return site_000017;
        }

        private SeadSite buildAmiens(){
            //"Amiens", "Somme", "France", null, 49.887363f, 2.301486f, 46f, "Yvinec", "Late 2nd C ad Roman granary, charred material.", null),
            SeadSite site_00018 = TestSeadSite.create(
                    null,
                    "Amiens",
                    null,
                    createSeadBigDecimal(49.887363f),
                    createSeadBigDecimal(2.301486f),
                    createSeadBigDecimal(46f),
                    "Late 2nd C ad Roman granary, charred material.");
            site_00018.setSiteLocations(Arrays.asList(
                    TestSiteLocation.create(null, TestLocation.create(null, "Somme", administrativeRegionType), site_00018),
                    TestSiteLocation.create(null, TestLocation.create(241, "France", countryType), site_00018)
            ));
            return site_00018;
        }

        private SeadSite buildEndletvatn(){
            //"Endletvatn", "Andoya", "Norway", null, 69.73333f, 19.083334f, null, "Fjellberg", "mid-Weichselian sediments dated by 14C to 12-18,000 BP, part of a continuous sequence. At a depth of 7-10m rich in fragment of chironomids, incl Corycera ambigua, Cladocera and mites.", null),
            SeadSite site_000019 = TestSeadSite.create(
                    null,
                    "Endletvatn",
                    null,
                    createSeadBigDecimal(69.73333f),
                    createSeadBigDecimal(19.083334f),
                    null,
                    "mid-Weichselian sediments dated by 14C to 12-18,000 BP, part of a continuous sequence. At a depth of 7-10m rich in fragment of chironomids, incl Corycera ambigua, Cladocera and mites.");
            site_000019.setSiteLocations(Arrays.asList(
                    TestSiteLocation.create(null, TestLocation.create(null, "Andoya", administrativeRegionType), site_000019),
                    TestSiteLocation.create(null, TestLocation.create(162, "Norway", countryType), site_000019)
            ));
            return site_000019;
        }

        private SeadSite buildAngdala(){
            //"Ängdala", "Skåne", "Sweden", null, 55.583332f, 13.116667f, null, "Lemdahl", "Ängdala. 3 Lateglacial into Early Holocene gyttja and peat successions.\r\nFull list not published.\r\n[1 sample, species presence only.]", null),
            SeadSite site_000020 = TestSeadSite.create(
                    null,
                    "Ängdala",
                    null,
                    createSeadBigDecimal(55.583332f),
                    createSeadBigDecimal(13.116667f),
                    null,
                    "Ängdala. 3 Lateglacial into Early Holocene gyttja and peat successions.\r\nFull list not published.\r\n[1 sample, species presence only.]");
            site_000020.setSiteLocations(Arrays.asList(
                    TestSiteLocation.create(null, TestLocation.create(780, "Skåne", administrativeRegionType), site_000020),
                    TestSiteLocation.create(null, TestLocation.create(205, "Sweden", countryType), site_000020)
            ));
            return site_000020;
        }

        private SeadSite buildMoen(){
            //"Møen unlocated 1896", "Møen", "Denmark", null, null, null, null, "Henriksen", "Holocene peat.", null)
            SeadSite site_001274 = TestSeadSite.create(
                    null,
                    "Møen unlocated 1896", null, null, null, null, "Holocene peat.");
            site_001274.setSiteLocations(Arrays.asList(
                TestSiteLocation.create(null, TestLocation.create(707, "Møn", administrativeRegionType), site_001274),
                    TestSiteLocation.create(null, TestLocation.create(58, "Denmark", countryType), site_001274)
            ));
            return site_001274;
        }

    }

    private static class TestSiteTypeTranslationBuilder {

        private static final String BUGS_TABLE_NAME = "TSite";

        List<TypeTranslation> getTranslations(){
            return Arrays.asList(
                     createTypeTranslation("region", "Møen", "region", "Møn")
                    ,createTypeTranslation("code", "SITE000006", "country", "United Kingdom")
            );
        }

        private TypeTranslation createTypeTranslation(String triggeringColumn, String triggeringValue, String targetColumn, String replacementValue){
            TypeTranslation translation = new TypeTranslation();
            translation.setBugsTable(BUGS_TABLE_NAME);
            translation.setBugsColumn(triggeringColumn);
            translation.setTriggeringColumnValue(triggeringValue);
            translation.setTargetColumn(targetColumn);
            translation.setReplacementValue(replacementValue);
            return translation;
        }
    }

}
