package se.sead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.sead.bugsimport.Importer;

import java.util.List;

@Component
public class ImportRunner {

    @Autowired
    private ExitReporter exitReporter;

    public void run(List<Importer> importers) throws Exception{
        for (Importer importer :
                importers) {
            importer.run();
        }
        exitReporter.reportErrors();
    }

}
