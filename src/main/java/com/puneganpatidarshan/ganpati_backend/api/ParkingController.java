package com.puneganpatidarshan.ganpati_backend.api;

import com.puneganpatidarshan.ganpati_backend.model.Parking;
import com.puneganpatidarshan.ganpati_backend.repo.ParkingRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api")
public class ParkingController {
    private final ParkingRepo repo;
    public ParkingController(ParkingRepo repo){ this.repo=repo; }

    @GetMapping("/parking")
    public List<Parking> all(){ return repo.findAll(); }

    @PostMapping("/parking/seed")
    public Parking seed(){
        double lat=18.5193, lng=73.8555;
        return repo.save(new Parking(
                null, "Shaniwar Wada Parking", 120, "₹20/hr", "06:00–23:00", "open",
                new org.springframework.data.mongodb.core.geo.GeoJsonPoint(lng, lat),
                "https://www.google.com/maps/dir/?api=1&destination="+lat+","+lng
        ));
    }
}
