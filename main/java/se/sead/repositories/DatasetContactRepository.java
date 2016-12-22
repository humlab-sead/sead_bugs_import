package se.sead.repositories;

import se.sead.sead.data.DatasetContact;

import java.util.List;

public interface DatasetContactRepository extends CreateAndReadRepository<DatasetContact, Integer> {

    List<DatasetContact> findAll();
}
