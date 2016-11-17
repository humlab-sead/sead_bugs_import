package se.sead.attributes;

import se.sead.bugsimport.attributes.bugsmodel.BugsAttributes;

import java.util.Arrays;
import java.util.List;

public class ExpectedBugsData {

    static final List<BugsAttributes> EXPECTED_DATA =
            Arrays.asList(
                create(1d, "Stored", "Max", 1f, "mm"),
                create(1d, "Stored", "Min", 1f, "mm"),
                create(1d, null, "No type", 1f, "mm"),
                create(1d, "No value", "Max", null, "mm"),
                create(1d, "No unit", "Max", 1f, null),
                create(2d, "Stored", "Max", 1f, "mm"),
                create(2d, "New", "Min", 1f, "mm"),
                create(3d, "New", "Max", 1f, "mm"),
                create(3d, "New", "Min", 1f, "mm"),
                create(4d, "Stored w/o trace", "Max", 1f, "mm"),
                create(4d, "Stored w/o trace", "Min", 1f, "mm"),
                create(5d, "Changed", "Max", 2f, "mm"),
                create(5d, "Changed", "Min", 2f, "mm"),
                create(6d, "Stored", "Max", 1f, "mm"),
                create(6d, "Changed", "Min", 2f, "mm"),
                create(7d, "Changed after import", "Max", 2f, "mm"),
                create(7d, "Stored", "Min", 1f, "mm"),
                create(99d, "No species exist", "Max", 1f, "mm")
            );

    private static BugsAttributes create(
            Double code,
            String type,
            String measure,
            Float value,
            String units){
        BugsAttributes attributes = new BugsAttributes();
        attributes.setCode(code);
        attributes.setType(type);
        attributes.setMeasure(measure);
        attributes.setValue(value);
        attributes.setUnits(units);
        return attributes;
    }
}
