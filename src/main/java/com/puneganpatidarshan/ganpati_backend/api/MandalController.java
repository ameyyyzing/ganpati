package com.puneganpatidarshan.ganpati_backend.api;

import com.puneganpatidarshan.ganpati_backend.model.Mandal;
import com.puneganpatidarshan.ganpati_backend.repo.MandalRepo;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api")
public class MandalController {
    private final MandalRepo repo;
    public MandalController(MandalRepo repo){ this.repo=repo; }

    // GET /api/mandals?topOnly=true
    @GetMapping("/mandals")
    public List<Mandal> all(@RequestParam(required=false) Boolean topOnly){
        if (Boolean.TRUE.equals(topOnly)) return repo.findByRankIsNotNullOrderByRankAsc();
        return repo.findAllSorted();
    }

    @DeleteMapping("/mandals/reset")
    public java.util.Map<String,Object> resetAndReseed() {
        long before = repo.count();
        repo.deleteAll();
        var seeded = seed(); // reuse your seed() method that inserts Manache 5 + Dagdusheth
        return java.util.Map.of("deleted", before, "reseeded", seeded);
    }


    // Seed Manache 5 + Dagdusheth (idempotent-ish)
    @PostMapping("/mandals/seed")
    public Map<String,Object> seed(){
        if (repo.count() > 0) return Map.of("skipped", true, "count", repo.count());
        String dir = "https://www.google.com/maps/dir/?api=1&destination=";
        List<Mandal> list = List.of(
                new Mandal(null,"Kasba Ganpati","Shaniwar Peth",1,new GeoJsonPoint(73.8567,18.5187),dir+"18.5187,73.8567","Pune’s ग्रामदैवत.","6am–11pm"),
                new Mandal(null,"Tambdi Jogeshwari","Budhwar Peth",2,new GeoJsonPoint(73.8561,18.5181),dir+"18.5181,73.8561",null,null),
                new Mandal(null,"Guruji Talim","Laxmi Road",3,new GeoJsonPoint(73.8581,18.5192),dir+"18.5192,73.8581",null,null),
                new Mandal(null,"Tulshibaug Ganpati","Tulshibaug",4,new GeoJsonPoint(73.8585,18.5156),dir+"18.5156,73.8585",null,null),
                new Mandal(null,"Kesariwada Ganpati","Narayan Peth",5,new GeoJsonPoint(73.8529,18.5167),dir+"18.5167,73.8529",null,null),
                new Mandal(null,"Shreemant Dagdusheth Halwai","Budhwar Peth",null,new GeoJsonPoint(73.8567,18.5165),dir+"18.5165,73.8567","Iconic Pune mandal.",null)
        );
        repo.saveAll(list);
        return Map.of("inserted", list.size());
    }
}
