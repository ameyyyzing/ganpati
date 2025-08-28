package com.puneganpatidarshan.ganpati_backend.repo;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.puneganpatidarshan.ganpati_backend.model.*;

import java.util.List;

public interface TransitRepo  extends MongoRepository<Transit, String> {
    List<Transit> findByTypeIgnoreCase(String type);
}
