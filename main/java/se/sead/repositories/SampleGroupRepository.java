package se.sead.repositories;

import se.sead.bugsimport.countsheets.seadmodel.SampleGroup;
import se.sead.bugsimport.site.seadmodel.SeadSite;

import java.util.List;

public interface SampleGroupRepository extends CreateAndReadRepository<SampleGroup, Integer>{

    List<SampleGroup> findAll();

    List<SampleGroup> findAllBySiteAndNameIgnoreCase(SeadSite site, String name);
}
