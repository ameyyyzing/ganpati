// src/main/java/com/puneganpatidarshan/ganpati_backend/service/PlanService.java
package com.puneganpatidarshan.ganpati_backend.service;

import com.puneganpatidarshan.ganpati_backend.dto.plan.*;
import com.puneganpatidarshan.ganpati_backend.model.Mandal;
import com.puneganpatidarshan.ganpati_backend.repo.MandalRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanService {
    private final MandalRepo mandalRepo;
    public PlanService(MandalRepo mandalRepo){ this.mandalRepo = mandalRepo; }

    // ~4.2 km/h walking => 70 m/min
    private static final double WALK_M_PER_MIN = 70.0;

    public PlanResponse plan(PlanRequest req){
        List<Mandal> all = mandalRepo.findAll();

        // Must-see (default Manache 1..5 + Dagdusheth + Rangari)
        List<Mandal> must = resolveMustSee(req.getMustSeeIds(), all);

        // Candidate pool: must first, then other Manache (ranked)
        List<Mandal> top = mandalRepo.findByRankIsNotNullOrderByRankAsc();
        LinkedHashMap<String,Mandal> pool = new LinkedHashMap<>();
        for (Mandal m : must) pool.put(m.getId(), m);
        for (Mandal m : top)  pool.putIfAbsent(m.getId(), m);

        double curLat = req.getStartLat();
        double curLng = req.getStartLng();
        Instant clock = (req.getStartTime()!=null) ? req.getStartTime() : Instant.now();
        int budget = (req.getDurationMins()>0) ? req.getDurationMins() : 180;

        List<PlanStop> out = new ArrayList<>();
        int travel = 0, dwell = 0;

        // 1) Must-sees ordered by distance from start
        List<Mandal> mustOrdered = new ArrayList<>(must);
        double finalCurLat = curLat;
        double finalCurLng = curLng;
        mustOrdered.sort(Comparator.comparingDouble(m -> distMeters(finalCurLat, finalCurLng, m)));

        for (Mandal m : mustOrdered) {
            PlanStop seg = makeStop(clock, m, curLat, curLng);
            if (travel + dwell + seg.getLegTravelMins() + seg.getDwellMins() > budget) break;
            out.add(seg);
            travel += seg.getLegTravelMins(); dwell += seg.getDwellMins();
            curLat = seg.getLat(); curLng = seg.getLng();
            clock  = Instant.parse(seg.getDepartIso());
            pool.remove(m.getId());
        }

        // 2) Fill remaining time with nearest of the rest
        while (!pool.isEmpty()){
            double finalCurLat1 = curLat;
            double finalCurLng1 = curLng;
            Mandal next = pool.values().stream()
                    .min(Comparator.comparingDouble(m -> distMeters(finalCurLat1, finalCurLng1, m)))
                    .orElse(null);
            if (next == null) break;

            PlanStop seg = makeStop(clock, next, curLat, curLng);
            if (travel + dwell + seg.getLegTravelMins() + seg.getDwellMins() > budget) break;

            out.add(seg);
            travel += seg.getLegTravelMins(); dwell += seg.getDwellMins();
            curLat = seg.getLat(); curLng = seg.getLng();
            clock  = Instant.parse(seg.getDepartIso());
            pool.remove(next.getId());
        }

        boolean over = must.size() > out.size(); // couldn't include all must-sees
        int total = travel + dwell;

        return new PlanResponse(
                out, travel, dwell, total,
                clock.toString(),
                over,
                over ? "Time budget couldn’t include all must-see stops." : "Plan fits within your time."
        );
    }

    private List<Mandal> resolveMustSee(List<String> ids, List<Mandal> all){
        if (ids == null || ids.isEmpty()){
            List<Mandal> manache = all.stream()
                    .filter(m -> m.getRank()!=null && m.getRank()>=1 && m.getRank()<=5)
                    .collect(Collectors.toList());

            List<Mandal> special = all.stream().filter(m -> {
                String n = (m.getName()==null) ? "" : m.getName().toLowerCase();
                return n.contains("dagdusheth") || n.contains("rangari");
            }).collect(Collectors.toList());

            List<Mandal> out = new ArrayList<>(manache);
            for (Mandal s : special) {
                boolean exists = out.stream().anyMatch(x -> Objects.equals(x.getId(), s.getId()));
                if (!exists) out.add(s);
            }
            return out;
        }
        Set<String> set = new HashSet<>(ids);
        return all.stream().filter(m -> set.contains(m.getId())).collect(Collectors.toList());
    }

    private PlanStop makeStop(Instant current, Mandal m, double fromLat, double fromLng){
        double meters = distMeters(fromLat, fromLng, m);
        int travelMins = (int)Math.ceil(meters / WALK_M_PER_MIN);
        int dwellMins  = dwellFor(m);

        Instant arrival = current.plusSeconds(travelMins * 60L);
        Instant depart  = arrival.plusSeconds(dwellMins  * 60L);

        return new PlanStop(
                m.getId(), m.getName(),
                m.getLocation().getY(), m.getLocation().getX(), // lat, lng
                dwellMins, meters, travelMins,
                arrival.toString(), depart.toString()
        );
    }

    private int dwellFor(Mandal m){
        if (m.getRank()!=null && m.getRank()>=1 && m.getRank()<=5) return 25;
        String n = (m.getName()==null) ? "" : m.getName().toLowerCase();
        if (n.contains("dagdusheth")) return 40;
        if (n.contains("rangari"))    return 20;
        return 15;
    }

    private static double distMeters(double lat, double lng, Mandal m){
        return haversineMeters(lat, lng, m.getLocation().getY(), m.getLocation().getX());
    }

    // Haversine in meters
    public static double haversineMeters(double lat1,double lon1,double lat2,double lon2){
        double R=6371000.0;
        double dLat=Math.toRadians(lat2-lat1);
        double dLon=Math.toRadians(lon2-lon1);
        double a=Math.sin(dLat/2)*Math.sin(dLat/2)+
                Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*
                        Math.sin(dLon/2)*Math.sin(dLon/2);
        return 2*R*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}
