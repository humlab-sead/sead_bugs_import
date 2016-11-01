package se.sead.ecocodes.koch;

import se.sead.bugsimport.ecocodes.koch.bugsmodel.EcoKoch;

class KochEcoLogVerifier extends se.sead.ecocodes.LogVerifier<EcoKoch> {

    @Override
    protected String getNoCodeFoundMessage() {
        return "No koch code found";
    }

    @Override
    protected String getNoCodeSpecifiedMessage() {
        return "No koch code specified";
    }
}
