package org.brightblock.search.service.project;

import org.brightblock.search.api.model.CreatorsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CreatorsRepository extends MongoRepository<CreatorsModel, String> {

}
