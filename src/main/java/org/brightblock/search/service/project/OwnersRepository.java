package org.brightblock.search.service.project;

import org.brightblock.search.api.model.OwnersModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OwnersRepository extends MongoRepository<OwnersModel, String> {

}
