package se.sead.bugsimport.lab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.BugsSeadMapper;
import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.bugsmodel.LabBugsTable;
import se.sead.bugsimport.lab.seadmodel.DatingLab;

@Component
public class LabBugsSeadMapper extends BugsSeadMapper<BugsLab, DatingLab> {

    @Autowired
    public LabBugsSeadMapper(
        LabRowConverter rowConverter
    ){
        super(new LabBugsTable(), rowConverter);
    }
}
