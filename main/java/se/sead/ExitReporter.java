package se.sead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sead.repositories.BugsErrorRepository;

@Service
public class ExitReporter {

    @Autowired
    private BugsErrorRepository errorRepository;

    public void reportErrors(){
        int numberOfErrorsForTodaysRun = errorRepository.getNumberOfErrorsForTodaysRun();
        if(numberOfErrorsForTodaysRun > 0){
            System.err.print("there where " + numberOfErrorsForTodaysRun + " found during todays run");
        } else {
            System.out.println("import done.");
        }
    }
}
