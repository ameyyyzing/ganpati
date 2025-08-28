package com.puneganpatidarshan.ganpati_backend.api;

import com.puneganpatidarshan.ganpati_backend.dto.plan.PlanRequest;
import com.puneganpatidarshan.ganpati_backend.dto.plan.PlanResponse;
import com.puneganpatidarshan.ganpati_backend.service.PlanService;
import org.springframework.web.bind.annotation.*;

// (optional) enable CORS for your Vite dev server
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class PlanController {

    private final PlanService service;
    public PlanController(PlanService service){ this.service = service; }

    @PostMapping("/plan")
    public PlanResponse plan(@RequestBody PlanRequest req){
        return service.plan(req);
    }
}
