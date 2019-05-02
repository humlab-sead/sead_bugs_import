package se.sead.repositories;

import org.springframework.data.repository.Repository;
import se.sead.sead.model.SamplingContext;

public interface SamplingContextRepository extends Repository<SamplingContext, Integer> {
    SamplingContext findByNameIgnoreCase(String name);
}
