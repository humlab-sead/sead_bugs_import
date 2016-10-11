package se.sead.bugsimport.sitelocations.converters;

import se.sead.bugsimport.sitelocations.seadmodel.SiteLocation;

import java.util.List;
import java.util.stream.Collectors;

class SiteLocationUpdater {

    private List<SiteLocation> storedLocations;
    private List<SiteLocation> bugsLocations;
    private List<SiteLocation> itemsToRemove;
    private List<SiteLocation> newItems;

    SiteLocationUpdater(List<SiteLocation> bugsLocations, List<SiteLocation> storedLocations){
        this.bugsLocations = bugsLocations;
        this.storedLocations = storedLocations;
    }

    List<SiteLocation> getClearedItems(){
        if(update()) {
            return itemsToRemove;
        } else {
            return storedLocations;
        }
    }

    private boolean update(){
        if(differingLocations()) {
            itemsToRemove.addAll(newItems);
            return true;
        }
        return false;
    }

    private boolean differingLocations(){
        setupRemoveItems();
        setupNewItems();
        return itemsToRemove.size() > 0 || newItems.size() > 0;
    }

    private void setupRemoveItems() {
        if(itemsToRemove == null) {
            itemsToRemove = storedLocations.stream()
                    .filter(siteLocation -> !bugsLocations.contains(siteLocation))
                    .map(siteLocation -> {
                        siteLocation.markForDeletion();
                        return siteLocation;
                    })
                    .collect(Collectors.toList());
        }
    }

    private void setupNewItems(){
        if(newItems == null) {
            newItems = bugsLocations.stream()
                    .filter(location ->
                            !storedLocations.stream()
                                    .anyMatch(storedLocation -> storedLocation.equals(location))
                    )
                    .collect(Collectors.toList());
        }
    }
}
