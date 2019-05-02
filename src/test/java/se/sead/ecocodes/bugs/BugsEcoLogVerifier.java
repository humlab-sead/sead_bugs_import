package se.sead.ecocodes.bugs;

import se.sead.bugsimport.ecocodes.bugs.bugsmodel.EcoBugs;

class BugsEcoLogVerifier extends se.sead.ecocodes.LogVerifier<EcoBugs> {

    @Override
    protected String getNoCodeSpecifiedMessage() {
        return "No bugs code specified";
    }

    @Override
    protected String getNoCodeFoundMessage() {
        return "No bugs code found";
    }
}
