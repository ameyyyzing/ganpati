package com.puneganpatidarshan.ganpati_backend.repo;
import com.puneganpatidarshan.ganpati_backend.model.Parking;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ParkingRepo extends MongoRepository<Parking, String> {}
