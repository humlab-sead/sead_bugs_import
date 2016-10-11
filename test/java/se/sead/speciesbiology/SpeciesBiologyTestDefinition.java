package se.sead.speciesbiology;

import se.sead.bugsimport.species.seadmodel.TaxaOrder;
import se.sead.bugsimport.speciesbiology.bugsmodel.Biology;
import se.sead.species.TestSpeciesProvider;

import java.util.Arrays;
import java.util.List;

public class SpeciesBiologyTestDefinition {

    static final String ACCESS_DATABASE_FILE = "biology.mdb";
    static final List<Biology> EXPECTED_ACCESS_DATA =
            Arrays.asList(
                    create(1.001006d, "Whitehead 1997a", "in area of mobile dunes on coast."),
                    create(1.001007d, "Richards 1926", "hunting on bare, sandy ground on heath."),
                    create(1.001007d, "Lindroth 1974", "an unfastidious sp., with preference for sandy and heathy ground; already active in early spring."),
                    create(1.001007d, "Koch 1989", "thin dry woods, sunny woodland paths, heaths, bare peaty soils, dry meadows, sandy fields, sandpits, sandy coasts."),
                    create(1.001008d, "Lindroth 1974", "in open grassland near the coast."),
                    create(1.001008d, "Koch 1989", "dry arable fields, often harvested cornfields, grassy dry weedy places, steppe, heaths, chalky pastures, dry woodland margins."),
                    create(1.001008d, "Hyman 1992", "damp sandy or silty areas almost devoid of vegetation, such as sandy undercliffs, landslips and seepage sites. Predatory, larvae found in burrows in damp sand, adults running rapidly over sand in bare or sparsely vegetated areas, apparently reluctant to fly, June-Aug."),
                    create(1.001002d, "Richards 1926", "hunting on bare, sandy ground on heath."),
                    create(1.001002d, "Lindroth 1974", "on dry, sandy soil in sun-exposed positions on heath or in thin coniferous forest with Calluna."),
                    create(1.001002d, "Harde 1984", "on sandy ground, especially  heathland, but generally local and rare."),
                    create(1.001002d, "Koch 1989", "on sandy Calluna heath; thin Pinus woodland, coastal dunes."),
                    create(1.001002d, "Hyman 1992", "heathland and heathy areas in open coniferous woodland. Predatory, occurs on dry, sandy soils in sunny situations. Larvae in burrows in soil, adults amongst heather, here they run about rapidly, March-July."),
                    create(1.001005d, "Lindroth 1974", "on open land and gravel, not confined to the coast."),
                    create(1.001005d, "Harde 1984", "In sandy places near the sea, but also inland, even up to 2,000m."),
                    create(1.001005d, "Koch 1989", "on sandy banks, dunes, sand and gravel pits, sandy dry turf, sandy light woodland."),
                    create(1.001005d, "Hyman 1992", "on open sandy areas on or near coast and for short distances inland, typically on sand dunes. Predatory, larvae probably develop in burrows in sand, adults active hunters, flying and running rapidly in search of prey."),
                    create(1.001005d, "Böcher (1995)", "open sun-exposed biotopes such as dry, sandy grass areas and dunes, a highly thermophilic sp."),
                    create(1.001006d, "Hodge & Jones 1995", "Coastal sand dunes."),
                    create(1.001006d, "Lindroth 1974", "on sterile sand, almost exclusively on the coast."),
                    create(1.001006d, "Harde 1984", "Open sandy ground only."),
                    create(1.001006d, "Koch 1989", "fine sandy sparsely vegetated shores and coastal dunes; to 100km inland on the sandy non-saline banks of the River Elbe."),
                    create(1.001006d, "Hyman 1992", "on sand along the driftline and in inter-tidal areas of beaches, also in sand dunes, almost exclusively coastal. Predatory, larvae live in burrows in open areas of hard packed sand, adults active hunters, flying and running rapidly, May-Aug."),
                    create(1.001006d, "Key 1996a", "on bare sand in dune slack, at margin between marram dominated low dune and sea lavender dominated vegetation of sheltered dune slack."),
                    create(9999.0000001d, "Note", "Bugs Place holder for sites/fossil record items with no species name."),
                    create(1.001008d, "Else 1993", "detailed study of habitat."),
                    create(1.001008d, "Lott 2003", "at slumping cliff seepages, on exposed soft sediments."),
                    create(1.001007d, "Luff 1998", "on open dry heaths and moors, an annual sp breeding early in spring, larvae inhabiting vertical burrows in soil. Flies readily."),
                    create(1.001008d, "Luff 1998", "on bare sand or silt near damp coastal flushes. Adults in mid late summer, overwintering as larvae in burrows. Seldom flies."),
                    create(1.001008d, "Appleton 2004", "running on mud."),
                    create(1.001002d, "Luff 1998", "on dry, sandy heaths. A spring breeder, actively flying."),
                    create(1.001005d, "Luff 1998", "on dunes and similar sandy soil, usually on coast, occasionally inland. Active flier breeding in summer."),
                    create(1.001006d, "Duff 1993a", "on bare sand at coast, especially  in sunshine."),
                    create(1.001006d, "Luff 1998", "coastal, on dunes, along driftline and inter-tidal areas. Spring and summer breeder which flies readily."),
                    create(1.001007d, "Lindroth 1985", "less thermophilous and more eurytopic than the other Cicindela spp, occurring in habitats of differing humidity and vegetation cover.  especially prominent on peaty soil, but also in clayey, sandy and gravelly places, such as bare ground in Calluna vegetation and on sunny woodland paths.  Typically spring active, especially in May, with little activity later in the year. Flies readily."),
                    create(1.001007d, "Duff 1993a", "on somewhat dry soils, often on limestone hillsides and tracks or on heaths and exposed peat, especial in sunshine."),
                    create(1.001002d, "Lindroth 1985", "on dry, sandy and sun-exposed ground, mainly in open pine forest with sparse vegetation of Calluna, Empetrum and Cladonia, also on heaths and sandy grass areas, often on recently burnt areas. Strongly heliophilous. Often preys on Formica spp. Flies well. June-July."),
                    create(1.001005d, "Lindroth 1985", "in open sun-exposed country, especially dry sandy grassy areas. Often in dunes and occasionally on seashore with C maritima. Also on fine gravel, in pits and on moraine. Very thermophilous, on S facing slopes. From early spring through summer."),
                    create(1.001006d, "Lindroth 1985", "stenotopic, on bare shifting sand near water, mainly on seashore, also along large rivers, exceptionally by lakes. Prefers dry sand."),
                    create(1.001006d, "Atty 1983", "on estuarine river banks."),
                    create(1.001002d, "Atty 1983", "on heaths."),
                    create(1.001008d, "Hurka 1996", "mainly in lowlands on pastures, field edges, avoiding sandy ground."),
                    create(1.001006d, "Cooter 1990c", "on areas of flat, bare sand."),
                    create(1.001002d, "Strand 1946", "on tracks (on sandy ground, most often in conifer woods, but also on Calluna heath)."),
                    create(1.001007d, "Lazenby 2011", "on moorland and heaths and sandy heaths; also recorded from ballast and colliery spoil."),
                    create(1.001007d, "Marsh 2009", "in open areas of heath and moorland. A spring breeder and very active on the wing.")
                    );

    private TestSpeciesProvider testSpeciesProvider;
    private TextBiologyBuilder textBiologyBuilder;

    SpeciesBiologyTestDefinition(TaxaOrder defaultOrder){
        testSpeciesProvider = new TestSpeciesProvider(new SpeciesBiologyTestSpeciesTreeBuiler(defaultOrder));
        textBiologyBuilder = new TextBiologyBuilder(new BiblioBuilder(), testSpeciesProvider);
        textBiologyBuilder.createExpectedResults();
    }

    private static final Biology create(Double code, String reference, String data){
        Biology biology = new Biology();
        biology.setCode(code);
        biology.setRef(reference);
        biology.setData(data);
        return biology;
    }

    TextBiologyBuilder getBiologyBuilder(){
        return textBiologyBuilder;
    }

    TestSpeciesProvider getSpeciesBuilder(){
        return testSpeciesProvider;
    }

}
