package se.sead.bugsimport.fossil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.sead.bugs.AccessReader;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.fossil.bugsmodel.Fossil;
import se.sead.bugsimport.fossil.bugsmodel.FossilBugsTable;
import se.sead.sead.data.Abundance;

// import java.util.List;
// import java.util.stream.Collectors;

@Component
public class FossilBugsSeadMapper extends BugsSeadMapper<Fossil, Abundance> {

    @Autowired
    public FossilBugsSeadMapper(FossilRowConverter singleBugsTableRowConverterForMapper) {
        super(new FossilBugsTable(), singleBugsTableRowConverterForMapper);
    }

    // @Override
    // protected List<Fossil> readItems(){
    //     List<Fossil> data = super.readItems();
    //     // Countsheet COUN000007
    //     data = data.stream().filter(z -> z.getSampleCODE().equals("SAMP000025") || z.getSampleCODE().equals("SAMP000026")).collect(Collectors.toList());
    //     return data;
    // }
}
