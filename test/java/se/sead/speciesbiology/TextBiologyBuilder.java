package se.sead.speciesbiology;

import se.sead.bugsimport.species.seadmodel.TaxaSpecies;
import se.sead.bugsimport.speciesbiology.seadmodel.TextBiology;
import se.sead.model.TestEqualityHelper;
import se.sead.model.TestTextBiology;
import se.sead.util.TestSpeciesProvider;

import java.util.ArrayList;
import java.util.List;

class TextBiologyBuilder {

    private BiblioBuilder biblioBuilder;
    private TestSpeciesProvider speciesProvider;
    private List<TextBiology> expected;

    public TextBiologyBuilder(BiblioBuilder biblioBuilder, TestSpeciesProvider testSpeciesProvider) {
        this.biblioBuilder = biblioBuilder;
        this.speciesProvider = testSpeciesProvider;
    }

    List<TextBiology> createExpectedResults(){
        expected = new ArrayList<>();
        expected.add(
                TestTextBiology.create(1, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Richards 1926"), "hunting on bare, sandy ground on heath.")
        );
        expected.add(
                TestTextBiology.create(2, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Lindroth 1974"), "on dry, sandy soil in sun-exposed positions on heath or in thin coniferous forest with Calluna.")
        );
        expected.add(
                TestTextBiology.create(3, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Harde 1984"), "on sandy ground, especially  heathland, but generally local and rare.")
        );
        expected.add(
                TestTextBiology.create(4, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Koch 1989"), "on sandy Calluna heath; thin Pinus woodland, coastal dunes.")
        );
        expected.add(
                TestTextBiology.create(5, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Hyman 1992"), "heathland and heathy areas in open coniferous woodland. Predatory, occurs on dry, sandy soils in sunny situations. Larvae in burrows in soil, adults amongst heather, here they run about rapidly, March-July.")
        );
        expected.add(
                TestTextBiology.create(6, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Luff 1998"), "on dry, sandy heaths. A spring breeder, actively flying.")
        );
        expected.add(
                TestTextBiology.create(7, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Lindroth 1985"), "on dry, sandy and sun-exposed ground, mainly in open pine forest with sparse vegetation of Calluna, Empetrum and Cladonia, also on heaths and sandy grass areas, often on recently burnt areas. Strongly heliophilous. Often preys on Formica spp. Flies well. June-July.")
        );
        expected.add(
                TestTextBiology.create(8, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Atty 1983"), "on heaths.")
        );
        expected.add(
                TestTextBiology.create(9, speciesProvider.getSpecies("10010020"), biblioBuilder.get("Strand 1946"), "on tracks (on sandy ground, most often in conifer woods, but also on Calluna heath).")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010050"), biblioBuilder.get("Lindroth 1974"), "on open land and gravel, not confined to the coast.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010050"), biblioBuilder.get("Harde 1984"), "In sandy places near the sea, but also inland, even up to 2,000m.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010050"), biblioBuilder.get("Koch 1989"), "on sandy banks, dunes, sand and gravel pits, sandy dry turf, sandy light woodland.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010050"), biblioBuilder.get("Hyman 1992"), "on open sandy areas on or near coast and for short distances inland, typically on sand dunes. Predatory, larvae probably develop in burrows in sand, adults active hunters, flying and running rapidly in search of prey.")
        );
        /* not included as this should generate an error report
        expected.add(
                TestTextBiology.create(null, speciesBuilder.getSpecies("10010050"), biblioBuilder.get("Böcher (1995)"), "open sun-exposed biotopes such as dry, sandy grass areas and dunes, a highly thermophilic sp.")
        );*/
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010050"), biblioBuilder.get("Luff 1998"), "on dunes and similar sandy soil, usually on coast, occasionally inland. Active flier breeding in summer.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010050"), biblioBuilder.get("Lindroth 1985"), "in open sun-exposed country, especially dry sandy grassy areas. Often in dunes and occasionally on seashore with C maritima. Also on fine gravel, in pits and on moraine. Very thermophilous, on S facing slopes. From early spring through summer.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Whitehead 1997a"), "in area of mobile dunes on coast.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Hodge & Jones 1995"), "Coastal sand dunes.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Lindroth 1974"), "on sterile sand, almost exclusively on the coast.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Harde 1984"), "Open sandy ground only.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Koch 1989"), "fine sandy sparsely vegetated shores and coastal dunes; to 100km inland on the sandy non-saline banks of the River Elbe.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Hyman 1992"), "on sand along the driftline and in inter-tidal areas of beaches, also in sand dunes, almost exclusively coastal. Predatory, larvae live in burrows in open areas of hard packed sand, adults active hunters, flying and running rapidly, May-Aug.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Key 1996a"), "on bare sand in dune slack, at margin between marram dominated low dune and sea lavender dominated vegetation of sheltered dune slack.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Duff 1993a"), "on bare sand at coast, especially  in sunshine.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Luff 1998"), "coastal, on dunes, along driftline and inter-tidal areas. Spring and summer breeder which flies readily.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Lindroth 1985"), "stenotopic, on bare shifting sand near water, mainly on seashore, also along large rivers, exceptionally by lakes. Prefers dry sand.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Atty 1983"), "on estuarine river banks.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010060"), biblioBuilder.get("Cooter 1990c"), "on areas of flat, bare sand.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010070"), biblioBuilder.get("Richards 1926"), "hunting on bare, sandy ground on heath.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010070"), biblioBuilder.get("Lindroth 1974"), "an unfastidious sp., with preference for sandy and heathy ground; already active in early spring.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010070"), biblioBuilder.get("Koch 1989"), "thin dry woods, sunny woodland paths, heaths, bare peaty soils, dry meadows, sandy fields, sandpits, sandy coasts.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010070"), biblioBuilder.get("Luff 1998"), "on open dry heaths and moors, an annual sp breeding early in spring, larvae inhabiting vertical burrows in soil. Flies readily.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010070"), biblioBuilder.get("Lindroth 1985"), "less thermophilous and more eurytopic than the other Cicindela spp, occurring in habitats of differing humidity and vegetation cover.  especially prominent on peaty soil, but also in clayey, sandy and gravelly places, such as bare ground in Calluna vegetation and on sunny woodland paths.  Typically spring active, especially in May, with little activity later in the year. Flies readily.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010070"), biblioBuilder.get("Duff 1993a"), "on somewhat dry soils, often on limestone hillsides and tracks or on heaths and exposed peat, especial in sunshine.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010070"), biblioBuilder.get("Lazenby 2011"), "on moorland and heaths and sandy heaths; also recorded from ballast and colliery spoil.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010070"), biblioBuilder.get("Marsh 2009"), "in open areas of heath and moorland. A spring breeder and very active on the wing.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010080"), biblioBuilder.get("Lindroth 1974"), "in open grassland near the coast.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010080"), biblioBuilder.get("Koch 1989"), "dry arable fields, often harvested cornfields, grassy dry weedy places, steppe, heaths, chalky pastures, dry woodland margins.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010080"), biblioBuilder.get("Hyman 1992"), "damp sandy or silty areas almost devoid of vegetation, such as sandy undercliffs, landslips and seepage sites. Predatory, larvae found in burrows in damp sand, adults running rapidly over sand in bare or sparsely vegetated areas, apparently reluctant to fly, June-Aug.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010080"), biblioBuilder.get("Else 1993"), "detailed study of habitat.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010080"), biblioBuilder.get("Lott 2003"), "at slumping cliff seepages, on exposed soft sediments.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010080"), biblioBuilder.get("Luff 1998"), "on bare sand or silt near damp coastal flushes. Adults in mid late summer, overwintering as larvae in burrows. Seldom flies.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010080"), biblioBuilder.get("Appleton 2004"), "running on mud.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("10010080"), biblioBuilder.get("Hurka 1996"), "mainly in lowlands on pastures, field edges, avoiding sandy ground.")
        );
        expected.add(
                TestTextBiology.create(null, speciesProvider.getSpecies("9999"), biblioBuilder.get("Note"), "Bugs Place holder for sites/fossil record items with no species name.")
        );
        return expected;
    }

    List<TextBiology> getExpectedSeadData(TaxaSpecies species){
        List<TextBiology> expectedMaterial = new ArrayList<>();
        for (TextBiology expectedBiology :
                expected) {
            if (TestEqualityHelper.equalsWithoutIdIfNeeded(expectedBiology.getSpecies(), species)){
                expectedMaterial.add(expectedBiology);
            }
        }
        return expectedMaterial;
    }
}
