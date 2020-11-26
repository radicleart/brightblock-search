package org.brightblock.search.service.project;

import org.brightblock.search.api.model.RatesModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RatesRepository extends MongoRepository<RatesModel, String> {

}
