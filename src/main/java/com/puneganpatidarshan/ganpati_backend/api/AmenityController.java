package com.puneganpatidarshan.ganpati_backend.api;

import com.puneganpatidarshan.ganpati_backend.model.Amenity;
import com.puneganpatidarshan.ganpati_backend.repo.AmenityRepo;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api")
public class AmenityController {
    private final AmenityRepo repo;
    public AmenityController(AmenityRepo repo){ this.repo=repo; }

    @GetMapping("/amenities")
    public List<Amenity> all(@RequestParam(required=false) String type){
        if (type==null || type.isBlank()) return repo.findAll();
        return repo.findByTypeIgnoreCase(type.trim());
    }

    @PostMapping("/amenities/seed")
    public Amenity seed(){
        double lat=18.5189, lng=73.8559;
        return repo.save(new Amenity(
                null, "toilet", "Public Toilet - Tulshibaug", "06:00–22:00",
                new org.springframework.data.mongodb.core.geo.GeoJsonPoint(lng, lat)
        ));
    }

    @PostMapping("/amenities/seed-food")
    public java.util.Map<String,Object> seedFood() {
        var list = java.util.List.of(
                amenity("food","Poona Guest House","11am–3pm, 7pm–10pm",
                        "100, Laxmi Road, Ganapati Chowk, New Nana Peth, Budhwar Peth, Pune",
                        java.util.List.of("Gavran Thali","Maharashtrian Meals"), "₹₹"),
                amenity("food","Shri Krishna Bhuvan","7:30am–4:30pm",
                        "1164, Budhwar Peth Rd, Tulshibaug, Budhwar Peth, Pune",
                        java.util.List.of("Misal","Kanda Poha","Sheera","Taak"), "₹"),
                amenity("food","Gujar Cold Drink House","Open 24 hours",
                        "Budhwar Peth (near Mandai), Pune",
                        java.util.List.of("Mastani","Ice-cream Milk"), "₹"),
                amenity("food","Vaidya Upahar Gruha","7:30am–11:30am, 3pm–7pm",
                        "676, Bagade Rd, Budhwar Peth, Pune",
                        java.util.List.of("Misal (green chilli base)","Kanda Poha","Batata Bhaji"), "₹"),
                amenity("food","Prabha Vishranti Gruha","8:30am–12pm, 4:30pm–8:30pm",
                        "569, Narasinha Chimtamani Kalker Rd, Narayan Peth, Pune",
                        java.util.List.of("Batata Vada","Sabudana Vada","Misal"), "₹"),
                amenity("food","R Bhagat Tarachand","12pm–4pm, 7pm–11pm",
                        "1st Floor, 2433, East St, Hulshur, Camp, Pune",
                        java.util.List.of("Veg Thali","Chaas","North Indian Meals"), "₹₹₹"),
                amenity("food","New Sweet Home (Perugate)","11am–8:30pm",
                        "765, RB Kumthekar Rd, Perugate, Sadashiv Peth, Pune",
                        java.util.List.of("Sabudana Khichdi","Pohe","Upma","Snacks"), "₹")
        );

        int upserted = 0;
        for (var a : list) {
            var ex = repo.findFirstByLabelAndType(a.getLabel(), a.getType());
            if (ex.isPresent()) {
                var e = ex.get();
                e.setHours(a.getHours());
                e.setAddress(a.getAddress());
                e.setMenuItems(a.getMenuItems());
                e.setPriceRange(a.getPriceRange());
                // keep location/urls if you later add them
                repo.save(e);
            } else {
                repo.save(a);
                upserted++;
            }
        }
        return java.util.Map.of("foodUpserted", upserted, "totalAmenities", repo.count());
    }

    private Amenity amenity(String type, String label, String hours,
                            String address, java.util.List<String> menu, String price) {
        var a = new Amenity();
        a.setType(type); a.setLabel(label); a.setHours(hours);
        a.setAddress(address); a.setMenuItems(menu); a.setPriceRange(price);
        return a;
    }

}
