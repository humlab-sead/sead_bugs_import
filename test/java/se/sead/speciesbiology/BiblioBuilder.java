package se.sead.speciesbiology;

import se.sead.sead.model.Biblio;
import se.sead.model.TestBiblio;

import java.util.HashMap;
import java.util.Map;

class BiblioBuilder {
    private Map<String, Biblio> data;

    BiblioBuilder(){
        data = new HashMap<>();
        setup();
    }

    private void setup(){
        data.put("Appleton 2004", TestBiblio.create(858, "Appleton, D. (2004)", "Appleton 2004", "Scarcer Coleoptera in Hampshire and Isle of Wight 1964-2001. The Coleopterist, 13, 41-80."));
        data.put("Atty 1983", TestBiblio.create(901, "Atty, D. B. (1983)", "Atty 1983", "Coleoptera of Gloucestershire. Cheltenham."));
        data.put("Cooter 1990c", TestBiblio.create(1820, "Cooter, J. (1990)", "Cooter 1990c", "Some uncommon beetles captured at Merthyr Mawr Warren, Glamorganshire. Entomologist's Monthly Magazine, 126, 32."));
        data.put("Duff 1993a", TestBiblio.create(2187, "Duff, A. (1993)", "Duff 1993a", "Beetles of Somerset: their status and distribution. Somerset Archaeological & Natural History Society, Taunton."));
        data.put("Else 1993", TestBiblio.create(2268, "Else, G. R. (1993)", "Else 1993", "The distribution and habitat requirements of the tiger beetle Cicindela germanica Linnaeus in southern Britain. British Journal of Entomology and Natural History, 6, 17-21."));
        data.put("Harde 1984", TestBiblio.create(2841, "Harde, K.W. (1984)", "Harde 1984", "A Field Guide in Colour to Beetles. Octopus, London."));
        data.put("Hodge & Jones 1995", TestBiblio.create(2977, "Hodge, P.J. & Jones, R.A. (1995)", "Hodge & Jones 1995", "New British Beetles. Species not in Joy's practical handbook. British Entomological and Natural History Society, Reading."));
        data.put("Hurka 1996", TestBiblio.create(3125, "Hurka, K. (1996)", "Hurka 1996", "Carabidae of the Czech and Slovak Republics. Kabourek, Zlin. (565pp.)"));
        data.put("Hyman 1992", TestBiblio.create(3136, "Hyman, P.S. (1992)", "Hyman 1992", "A review of the scarce and threatened Coleoptera of Great Britain, Part 1 (Revised & updated by M.S.Parsons). UK Joint Nature Conservation Committee, Peterborough."));
        data.put("Key 1996a", TestBiblio.create(3498, "Key, R.S. (1996)", "Key 1996a", "The Dune Tiger Beetle Cicindela maritima Latreille & Dejean (Carabidae) on the north Norfolk coast. Coleopterist, 5, 21."));
        data.put("Koch 1989", TestBiblio.create(1478, "Koch, K. (1989)", "Koch 1989", "Die Käfer Mitteleuropas. Ökologie, 1. Goecke & Evers, Krefeld."));
        data.put("Lazenby 2011", TestBiblio.create(3665, "Lazenby, A. S. (2011)", "Lazenby 2011", "Ground beetles of the Sorby area. Sorby Record Special Series, 15, 1-56."));
        data.put("Lindroth 1974", TestBiblio.create(3779, "Lindroth, C.H. (1974)", "Lindroth 1974", "Coleoptera: Carabidae. Handbooks for the Identification of British Insects IV,2. Royal Entomological Society of London."));
        data.put("Lindroth 1985", TestBiblio.create(3780, "Lindroth, C.H. (1985)", "Lindroth 1985", "The Carabidae (Coleoptera) of Fennoscandia and Denmark. Fauna Entomologica Scandinavica, 15,1. E.J.Brill, Leiden."));
        data.put("Lott 2003", TestBiblio.create(3836, "Lott, D. A. (2003)", "Lott 2003", "An annotated list of wetland ground beetles (Carabidae) and rove beetles (Staphylinidae) found in the British Isles including a literature review of their ecology. English Nature Report No. 488. Peterborough."));
        data.put("Luff 1998", TestBiblio.create(3874, "Luff, M. L. (1998)", "Luff 1998", "Provisional atlas of the ground beetles (Coleoptera, Carabidae) of Britain. Centre for Ecology & Hydrology, Biological Records Centre, Abbots Ripton."));
        data.put("Marsh 2009", TestBiblio.create(3968, "Marsh, R. (2009)", "Marsh 2009", "A provisional atlas of the Coleoptera of Yorkshire (Vice Counties 61-65). Part 1 - Suborder Adephaga: Family Carabidae."));
        data.put("Note", TestBiblio.create(4250, "Buckland P.I., Buckland P.C, Yuan Zhuo Don & Sadler J. (2002)", "Note", "Bugs Coleopteran Ecology Package [CD-ROM]."));
        data.put("Palm 1959", TestBiblio.create(1397, "Palm, T. (1959)", "Palm 1959", "Die Holz und Rindenkäfer der sud- und mittelschwedischen Laubbaume. Opuscula Entomologica Suppl. 16."));
        data.put("Richards 1926", TestBiblio.create(4667, "Richards, O. W. (1926)", "Richards 1926", "Studies on the ecology of English heaths. III. Animal communities of the felling and burn successions at Oxshott Heath, Surrey. Journal of Ecology, 14, 244-281."));
        data.put("Strand 1946", TestBiblio.create(5054, "Strand, A. (1946)", "Strand 1946", "Nord Norges Coleoptera. Tromsø Museums Arshefter, Naturhistorisk Avd. Nr. 34, 67(1). (629pp.)"));
        data.put("Whitehead 1997a", TestBiblio.create(5395, "Whitehead, P. F. (1997)", "Whitehead 1997a", "Notes on the coastal Coleoptera of Merthyr Mawr, Glamorgan (SS 87). Entomologist's monthly Magazine, 133, 172."));
    }

    Biblio get(String ref){
        return data.get(ref);
    }
}
