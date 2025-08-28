package com.puneganpatidarshan.ganpati_backend.repo;

import com.puneganpatidarshan.ganpati_backend.model.Mandal;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MandalRepo extends MongoRepository<Mandal, String> {
    List<Mandal> findByRankIsNotNullOrderByRankAsc();
    default List<Mandal> findAllSorted() {
        return findAll(Sort.by(
                Sort.Order.asc("rank").nullsLast(),
                Sort.Order.asc("name")
        ));
    }
}
