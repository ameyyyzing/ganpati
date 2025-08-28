package com.puneganpatidarshan.ganpati_backend.repo;

import com.puneganpatidarshan.ganpati_backend.model.Advisory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;

public interface AdvisoryRepo extends MongoRepository<Advisory, String> {

    // Active now = active:true AND (startsAt null or <= now) AND (endsAt null or >= now)
    @Query(value = "{ $and: [ " +
            "{ active: true }, " +
            "{ $or: [ { startsAt: null }, { startsAt: { $lte: ?0 } } ] }, " +
            "{ $or: [ { endsAt: null },   { endsAt:   { $gte: ?0 } } ] } " +
            "] }")
    List<Advisory> findActiveAtInstant(Instant now);
}
