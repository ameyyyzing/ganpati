package com.puneganpatidarshan.ganpati_backend.repo;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.puneganpatidarshan.ganpati_backend.model.*;

import java.util.List;
import java.util.Optional;

public interface AmenityRepo extends MongoRepository<Amenity, String> {
    List<Amenity> findByTypeIgnoreCase(String type);
    Optional<Amenity> findFirstByLabelAndType(String label, String type);
}
