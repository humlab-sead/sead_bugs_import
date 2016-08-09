package se.sead.bugsimport.site.conversion;

import se.sead.bugsimport.country.seadmodel.Location;
import se.sead.bugsimport.site.locations.LocationHandler;
import se.sead.bugsimport.site.seadmodel.SeadSite;
import se.sead.bugsimport.site.seadmodel.SiteLocation;

import java.util.List;
import java.util.stream.Collectors;

class SiteLocationUpdater {
    private List<SiteLocation> storedLocations;
    private List<Location> bugsLocations;
    private List<SiteLocation> itemsToRemove;
    private List<SiteLocation> newItems;

    SiteLocationUpdater(LocationHandler locationHandler, SeadSite storedSite){
        this.bugsLocations = locationHandler.getLocations();
        this.storedLocations = storedSite.getSiteLocations();
    }

    boolean differingLocations(){
        setupRemoveItems();
        setupNewItems();
        return itemsToRemove.size() > 0 || newItems.size() > 0;
    }

    private void setupRemoveItems() {
        if(itemsToRemove == null) {
            itemsToRemove = storedLocations.stream()
                    .filter(siteLocation -> !bugsLocations.contains(siteLocation.getLocation()))
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
                            !storedLocations.stream().map(
                                    siteLocation -> siteLocation.getLocation()
                            )
                                    .anyMatch(
                                            storedLocation -> location.equals(storedLocation))
                    ).map(location -> {
                                SiteLocation newLocation = new SiteLocation();
                                newLocation.setLocation(location);
                                return newLocation;
                            }
                    ).collect(Collectors.toList());
        }
    }

    void update(SeadSite carrier){
        itemsToRemove.addAll(newItems);
        carrier.setSiteLocations(itemsToRemove);
    }
}
