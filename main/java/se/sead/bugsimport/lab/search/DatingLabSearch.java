package se.sead.bugsimport.lab.search;

import se.sead.bugsimport.lab.bugsmodel.BugsLab;
import se.sead.bugsimport.lab.seadmodel.DatingLab;

public interface DatingLabSearch {
    DatingLab NO_LAB_FOUND = new DatingLab();

    DatingLab findFor(BugsLab bugsLab);
}
