package se.sead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Importer;

import java.util.List;

@Component
public class DefaultImportRunner {

    @Autowired
    private List<Importer> importers;
    @Autowired
    private ExitReporter exitReporter;

    public void run() throws Exception {
        for (Importer importer :
                importers) {
            importer.run();
        }
        exitReporter.reportErrors();
    }

}
