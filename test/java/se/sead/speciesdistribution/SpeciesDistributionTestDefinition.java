package se.sead.speciesdistribution;

import se.sead.bugsimport.speciesdistribution.bugsmodel.Distrib;
import se.sead.bugsimport.speciesdistribution.seadmodel.TextDistribution;
import se.sead.bugsimport.tracing.seadmodel.BugsError;
import se.sead.bugsimport.tracing.seadmodel.BugsTrace;
import se.sead.model.TestTextDistribution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpeciesDistributionTestDefinition {


    static final List<Distrib> EXPECTED_BUGS_ITEMS =
            Arrays.asList(
                    // already stored
                create(01.0010020, "Appleton 2004", "England: Hants (1969)."),
                create(01.0010020, "Atty 1983", "England: Gloucs, 1 example on Durdham Down."),
                create(01.0010020, "Harde 1984", "N & E parts of C Europe. S counties of England."),
                create(01.0010020, "Koch 1989", "Europe."),
                create(01.0010020, "Hyman 1992", "England: S & N Hants, E Kent, Surrey, Cambs, W Gloucs, N Lincs, before 1970, since Dorset, S & N Hants, W Sussex, Surrey. RDB status notable A, very local."),
                create(01.0010020, "Lindroth 1974", "England: N to Cambs. Local and usually rare."),
                create(01.0010020, "Lindroth 1985", "Denmark: rare but widespread in Jutland, NEZ and Bornholm. Sweden: throughout but uncommon, more local in S and Lapland. Norway: S of 63 N and E Finnmark. E Fennoscandia: generally distributed except in N. Palaearctic, E to Amur, not in Mediterranean region."),
                create(01.0010020, "Luff 1998", "England: Surrey and Dorset heaths. N Lincs (Manton Warren 1926) Cambs & Gloucs, Kent (19th C). Europe: not far N and Mediterranean."),
                create(01.0010020, "Strand 1946", "Europe and Asia. Norway: S and Finnmark, E. Sweden, N to Lapland. Finland: N to Lapmark and Kola. Denmark. England."),
                    // no biblio item found
                create(01.0010050, "Böcher (1995)", "Eurasia, boreal to S temperate, polytypic."),
                    // add
                create(01.0010050, "Harde 1984", "Most parts of Europe but very local in Britain."),
                create(01.0010050, "Koch 1989", "Europe."),
                create(01.0010050, "Hyman 1992", "England: W Cornwall, N Devon, Isle of Wight, Suffolk, E Norfolk, Ches, S Lancs, Cumberland. Wales: Glamorgans, Merioneths. Scotland: Fife. All before 1970, since S Lancs, Cumberland. RDB status vulnerable, formerly widespread."),
                create(01.0010050, "Lindroth 1974", "England: Norfolk, Ches, Lancs, Cumberland. Wales: Merioneth. local."),
                create(01.0010050, "Lindroth 1985", "Denmark: widespread and common except in LFM and SZ. Sweden: widespread, not rare from Skane to S Vrm. Norway: local in SE. Finland: S & C, more frequent to E and in adjacent parts of Russia. Europe & Siberia."),
                create(01.0010050, "Luff 1998", "England: S Lancs to Cumbria, earlier records from SW, Norfolk & Wales. Scotland: Fife (doubtful). Europe: throughout except far N."),
                    // add for new taxon
                create(01.0010060, "Atty 1983", "England: Gloucs, banks of Severn."),
                create(01.0010060, "Duff 1993a", "England: Somerset, very local and decreasing (1992)."),
                create(01.0010060, "Hodge & Jones 1995", "British Isles: very local but more frequent than C hydrida."),
                create(01.0010060, "Harde 1984", "Widespread in Europe and Asia."),
                create(01.0010060, "Koch 1989", "Europe: North Sea and Baltic coasts, Hamburg, River Elbe."),
                create(01.0010060, "Key 1996a", "England: Norfolk 1995, last previous record 1952."),
                create(01.0010060, "Hyman 1992", "England: SE, S, SW, E Anglia, E & W Midlands. Wales. RDB status notable B."),
                create(01.0010060, "Lindroth 1974", "England: N to Norfolk and Ches. Wales: N to Caernarvon. local."),
                create(01.0010060, "Luff 1998", "England: N Devon, Norfolk. Gloucs (19th C). Wales. All coastal, but not exclusively so in Europe, where widespread but local."),
                create(01.0010060, "Whitehead 1997a", "Wales: Glamorgan, Merthyr Mawr."),
                    // no species found
                create(01.0010070, "Berry 1985", "Orkney.")
            );

    private static Distrib create(double code, String ref, String data){
        Distrib distrib = new Distrib();
        distrib.setCode(code);
        distrib.setRef(ref);
        distrib.setData(data);
        return distrib;
    }


    private SpeciesBuilder speciesBuilder;
    private BiblioBuilder biblioBuilder;

    SpeciesDistributionTestDefinition(SpeciesBuilder speciesBuilder){
        this.speciesBuilder = speciesBuilder;
        biblioBuilder = new BiblioBuilder();
    }

    List<TextDistribution> getExpectedResults(){
        List<TextDistribution> expected = new ArrayList<>();
        expected.add(TestTextDistribution.create(1, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Appleton 2004"), "England: Hants (1969)."));
        expected.add(TestTextDistribution.create(2, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Atty 1983"), "England: Gloucs, 1 example on Durdham Down."));
        expected.add(TestTextDistribution.create(3, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Harde 1984"), "N & E parts of C Europe. S counties of England."));
        expected.add(TestTextDistribution.create(4, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Koch 1989"), "Europe."));
        expected.add(TestTextDistribution.create(5, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Hyman 1992"), "England: S & N Hants, E Kent, Surrey, Cambs, W Gloucs, N Lincs, before 1970, since Dorset, S & N Hants, W Sussex, Surrey. RDB status notable A, very local."));
        expected.add(TestTextDistribution.create(6, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Lindroth 1974"), "England: N to Cambs. Local and usually rare."));
        expected.add(TestTextDistribution.create(7, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Lindroth 1985"), "Denmark: rare but widespread in Jutland, NEZ and Bornholm. Sweden: throughout but uncommon, more local in S and Lapland. Norway: S of 63 N and E Finnmark. E Fennoscandia: generally distributed except in N. Palaearctic, E to Amur, not in Mediterranean region."));
        expected.add(TestTextDistribution.create(8, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Luff 1998"), "England: Surrey and Dorset heaths. N Lincs (Manton Warren 1926) Cambs & Gloucs, Kent (19th C). Europe: not far N and Mediterranean."));
        expected.add(TestTextDistribution.create(9, speciesBuilder.getSpecies("01.0010020"), biblioBuilder.get("Strand 1946"), "Europe and Asia. Norway: S and Finnmark, E. Sweden, N to Lapland. Finland: N to Lapmark and Kola. Denmark. England."));
        
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010050"), biblioBuilder.get("Harde 1984"), "Most parts of Europe but very local in Britain."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010050"), biblioBuilder.get("Koch 1989"), "Europe."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010050"), biblioBuilder.get("Hyman 1992"), "England: W Cornwall, N Devon, Isle of Wight, Suffolk, E Norfolk, Ches, S Lancs, Cumberland. Wales: Glamorgans, Merioneths. Scotland: Fife. All before 1970, since S Lancs, Cumberland. RDB status vulnerable, formerly widespread."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010050"), biblioBuilder.get("Lindroth 1974"), "England: Norfolk, Ches, Lancs, Cumberland. Wales: Merioneth. local."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010050"), biblioBuilder.get("Lindroth 1985"), "Denmark: widespread and common except in LFM and SZ. Sweden: widespread, not rare from Skane to S Vrm. Norway: local in SE. Finland: S & C, more frequent to E and in adjacent parts of Russia. Europe & Siberia."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010050"), biblioBuilder.get("Luff 1998"), "England: S Lancs to Cumbria, earlier records from SW, Norfolk & Wales. Scotland: Fife (doubtful). Europe: throughout except far N."));
       
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Atty 1983"), "England: Gloucs, banks of Severn."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Duff 1993a"), "England: Somerset, very local and decreasing (1992)."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Hodge & Jones 1995"), "British Isles: very local but more frequent than C hydrida."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Harde 1984"), "Widespread in Europe and Asia."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Koch 1989"), "Europe: North Sea and Baltic coasts, Hamburg, River Elbe."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Key 1996a"), "England: Norfolk 1995, last previous record 1952."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Hyman 1992"), "England: SE, S, SW, E Anglia, E & W Midlands. Wales. RDB status notable B."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Lindroth 1974"), "England: N to Norfolk and Ches. Wales: N to Caernarvon. local."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Luff 1998"), "England: N Devon, Norfolk. Gloucs (19th C). Wales. All coastal, but not exclusively so in Europe, where widespread but local."));
        expected.add(TestTextDistribution.create(null, speciesBuilder.getSpecies("01.0010060"), biblioBuilder.get("Whitehead 1997a"), "Wales: Glamorgan, Merthyr Mawr."));
        
        return expected;
    }

    public void assertTraces(List<BugsTrace> traces, Distrib bugsData) {
        if(bugsData.getCode() == 01.0010060d){
            assertAddedMatieral(traces);
        } else if(bugsData.getCode() == 01.0010050d &&
                !bugsData.getRef().equals("Böcher (1995)")){
            assertAddedMatieral(traces);
        } else {
            assertEquals(0, traces.size());
        }
    }

    private void assertAddedMatieral( List<BugsTrace> traces) {
        assertEquals(1, traces.size());
        assertTrue(
                traces.stream()
                .map(trace -> trace.getSeadTable())
                .allMatch(tableName -> "tbl_text_distribution".equals(tableName))
        );
    }

    public void assertErrors(List<BugsError> errors, Distrib bugsData) {
        if(bugsData.getRef().equals("Böcher (1995)")){
            assertEquals(1, errors.size());
            BugsError bugsError = errors.get(0);
            assertEquals("No reference found for ref: Böcher (1995)", bugsError.getMessage());
        } else if(bugsData.getCode() == 01.0010070d){
            assertEquals(1, errors.size());
            BugsError bugsError = errors.get(0);
            assertEquals("No species found for code: " + bugsData.getCode(), bugsError.getMessage());
        } else {
            assertEquals(0, errors.size());
        }
    }
}
