package com.puneganpatidarshan.ganpati_backend.api;

import com.puneganpatidarshan.ganpati_backend.dto.NearResponse;
import com.puneganpatidarshan.ganpati_backend.service.NearService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class NearController {
    private final NearService service;
    public NearController(NearService service){ this.service=service; }

    @GetMapping("/near")
    public NearResponse near(@RequestParam double lat,
                             @RequestParam double lng,
                             @RequestParam(defaultValue = "2") double radiusKm,
                             @RequestParam(defaultValue = "3") int limit) {
        return service.findNear(lat, lng, radiusKm, limit);
    }
}
