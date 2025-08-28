package com.puneganpatidarshan.ganpati_backend.api;

import com.puneganpatidarshan.ganpati_backend.model.Transit;
import com.puneganpatidarshan.ganpati_backend.repo.TransitRepo;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api")
public class TransitController {
    private final TransitRepo repo;
    public TransitController(TransitRepo repo){ this.repo=repo; }

    @GetMapping("/transit")
    public List<Transit> all(@RequestParam(required=false) String type){
        if (type==null || type.isBlank()) return repo.findAll();
        return repo.findByTypeIgnoreCase(type.trim());
    }

    @PostMapping("/transit/seed")
    public Transit seed(){
        double lat=18.5198, lng=73.8570;
        return repo.save(new Transit(
                null, "metro", "PMC Metro Station", "Aqua", "05:30", "23:00",
                new org.springframework.data.mongodb.core.geo.GeoJsonPoint(lng, lat)
        ));
    }
}
