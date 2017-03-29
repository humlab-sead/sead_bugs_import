package se.sead.configuration;

import org.springframework.boot.ApplicationArguments;
import se.sead.bugsimport.Importer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationArgumentManager {

    private ApplicationArguments arguments;

    public ApplicationArgumentManager(ApplicationArguments arguments){
        this.arguments = arguments;
    }

    public boolean shouldRun() {
        boolean shouldNotRun = arguments.getOptionNames().stream()
                .anyMatch(
                        name ->
                            name.equals("no-run") ||
                            name.equals("validate-schema")
                );
        return !shouldNotRun;
    }

    public List<Importer> filter(List<Importer> allImporters){
        List<String> namedImporters = arguments.getOptionValues("importers");
        if(namedImporters == null || namedImporters.isEmpty()) {
            return allImporters;
        } else {
            if(namedImporters.size() == 1){
                String importers = namedImporters.get(0);
                if(importers.contains(",")){
                    namedImporters = Arrays.asList(
                            importers.split(",")
                    );
                }
            }
            return filterImportersByNames(allImporters, namedImporters);
        }
    }

    private static List<Importer> filterImportersByNames(List<Importer> allImporters, List<String> namedImporters) {
        return allImporters.stream().filter(
                importer -> namedImporters.contains(
                        getSimpleNamedParsed(importer)
                )
        ).collect(Collectors.toList());
    }

    private static String getSimpleNamedParsed(Importer importer){
        String simpleName = importer.getClass().getSimpleName();
        if(simpleName.endsWith("Importer")){
            return simpleName.replace("Importer", "");
        } else {
            return simpleName;
        }
    }
}
