package se.sead.repositories;

import org.springframework.data.jpa.repository.Query;
import se.sead.sead.contact.ContactType;

public interface ContactTypeRepository extends CreateAndReadRepository<ContactType, Integer> {

    @Query("select type from ContactType type where type.name = 'Identified by'")
    ContactType getIdentifiedByType();

    @Query("select type from ContactType type where type.name = 'Specimen repository'")
    ContactType getSpecimentRepositoryType();
}
