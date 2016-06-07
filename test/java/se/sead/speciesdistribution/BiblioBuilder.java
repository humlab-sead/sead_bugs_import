package se.sead.speciesdistribution;

import se.sead.sead.model.Biblio;
import se.sead.model.TestBiblio;

import java.util.HashMap;
import java.util.Map;

public class BiblioBuilder {

    private Map<String, Biblio> references;

    BiblioBuilder(){
        references = new HashMap<>();
        addData();
    }

    private void addData(){
        references.put("Appleton 2004", TestBiblio.create(1,"Appleton, D. (2004)","Appleton 2004","Scarcer Coleoptera in Hampshire and Isle of Wight 1964-2001. The Coleopterist, 13, 41-80."));
        references.put("Atty 1983", TestBiblio.create(2,"Atty, D. B. (1983)","Atty 1983","Coleoptera of Gloucestershire. Cheltenham."));
        references.put("Harde 1984", TestBiblio.create(3,"Harde, K.W. (1984)","Harde 1984","A Field Guide in Colour to Beetles. Octopus, London."));
        references.put("Koch 1989", TestBiblio.create(4,"Koch, K. (1989)","Koch 1989","Die Käfer Mitteleuropas. Ökologie, 1. Goecke & Evers, Krefeld."));
        references.put("Berry 1985", TestBiblio.create(5,"Berry, R. J. (1985)","Berry 1985","The Natural History of Orkney. Collins, London."));
        references.put("Duff 1993a", TestBiblio.create(6,"Duff, A. (1993)","Duff 1993a","Beetles of Somerset: their status and distribution. Somerset Archaeological & Natural History Society, Taunton."));
        references.put("Hodge & Jones 1995", TestBiblio.create(7,"Hodge, P.J. & Jones, R.A. (1995)","Hodge & Jones 1995","New British Beetles. Species not in Joy's practical handbook. British Entomological and Natural History Society, Reading."));
        references.put("Hyman 1992", TestBiblio.create(8,"Hyman, P.S. (1992)","Hyman 1992","A review of the scarce and threatened Coleoptera of Great Britain, Part 1 (Revised & updated by M.S.Parsons). UK Joint Nature Conservation Committee, Peterborough."));
        references.put("Key 1996a", TestBiblio.create(9,"Key, R.S. (1996)","Key 1996a","The Dune Tiger Beetle Cicindela maritima Latreille & Dejean (Carabidae) on the north Norfolk coast. Coleopterist, 5, 21."));
        references.put("Lindroth 1974", TestBiblio.create(10,"Lindroth, C.H. (1974)","Lindroth 1974","Coleoptera: Carabidae. Handbooks for the Identification of British Insects IV,2. Royal Entomological Society of London."));
        references.put("Lindroth 1985", TestBiblio.create(11,"Lindroth, C.H. (1985)","Lindroth 1985","The Carabidae (Coleoptera) of Fennoscandia and Denmark. Fauna Entomologica Scandinavica, 15,1. E.J.Brill, Leiden."));
        references.put("Luff 1998", TestBiblio.create(12,"Luff, M. L. (1998)","Luff 1998","Provisional atlas of the ground beetles (Coleoptera, Carabidae) of Britain. Centre for Ecology & Hydrology, Biological Records Centre, Abbots Ripton."));
        references.put("Strand 1946", TestBiblio.create(13,"Strand, A. (1946)","Strand 1946","Nord Norges Coleoptera. Tromsø Museums Arshefter, Naturhistorisk Avd. Nr. 34, 67(1). (629pp.)"));
        references.put("Whitehead 1997a", TestBiblio.create(14,"Whitehead, P. F. (1997)","Whitehead 1997a","Notes on the coastal Coleoptera of Merthyr Mawr, Glamorgan (SS 87). Entomologist's monthly Magazine, 133, 172."));

    }

    Biblio get(String bugsReference){
        if(references.containsKey(bugsReference)){
            return references.get(bugsReference);
        }
        throw new UnsupportedOperationException(bugsReference);
    }
}
