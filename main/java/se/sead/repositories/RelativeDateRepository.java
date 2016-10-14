package se.sead.repositories;

import se.sead.bugsimport.datesperiod.seadmodel.RelativeDate;

import java.util.List;

public interface RelativeDateRepository extends  CreateAndReadRepository<RelativeDate, Integer>{
    List<RelativeDate> findAll();
}
