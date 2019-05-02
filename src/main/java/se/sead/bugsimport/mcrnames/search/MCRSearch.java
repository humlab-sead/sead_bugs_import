package se.sead.bugsimport.mcrnames.search;

import se.sead.bugsimport.mcrnames.bugsmodel.BugsMCRNames;
import se.sead.bugsimport.mcrnames.seadmodel.MCRName;

public interface MCRSearch {
    MCRName NO_MCR_NAME_FOUND = new MCRName();
    MCRName findFor(BugsMCRNames bugsData);
}
