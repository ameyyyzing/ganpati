package com.puneganpatidarshan.ganpati_backend.api;

import com.puneganpatidarshan.ganpati_backend.model.Advisory;
import com.puneganpatidarshan.ganpati_backend.repo.AdvisoryRepo;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdvisoryController {
    private final AdvisoryRepo repo;

    public AdvisoryController(AdvisoryRepo repo) {
        this.repo = repo;
    }

    // GET /api/advisories  -> only the advisories that are active "right now"
    @GetMapping("/advisories")
    public List<Advisory> active() {
        return repo.findActiveAtInstant(Instant.now());
    }

    // (Optional) GET /api/advisories/all -> list everything (debug / admin)
    @GetMapping("/advisories/all")
    public List<Advisory> all() {
        return repo.findAll();
    }

    // (Optional) POST /api/advisories/seed -> create one sample advisory to test quickly
    @PostMapping("/advisories/seed")
    public Advisory seed() {
        Instant now = Instant.now();
        Advisory adv = new Advisory(
                null,
                "Road closure near Tulshibaug",
                "Diversion 6–10 pm via Shanipar. Use alternate route.",
                "Budhwar Peth",
                now.minusSeconds(3600),   // started 1h ago
                now.plusSeconds(4*3600),  // ends in 4h
                "closure",
                true
        );
        return repo.save(adv);
    }
}
