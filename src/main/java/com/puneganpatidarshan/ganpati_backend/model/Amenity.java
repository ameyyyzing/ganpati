package com.puneganpatidarshan.ganpati_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("amenities")
public class Amenity {
    @Id
    private String id;

    // existing fields
    private String type;             // toilet|medical|water|police|helpdesk|food
    private String label;
    private String hours;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;   // (lng,lat) — can be null for list-only food entries

    // NEW optional fields for Comfort Food panel
    private String gmapsDirectionsUrl;   // keep if you want to show directions later
    private String address;              // textual address
    private List<String> menuItems;      // ["Misal","Vada Pav",...]
    private String priceRange;           // "₹" | "₹₹" | "₹₹₹"

    public Amenity() {}

    // keep your old ctor so existing seed() still compiles
    public Amenity(String id, String type, String label, String hours, GeoJsonPoint location) {
        this.id = id; this.type = type; this.label = label; this.hours = hours; this.location = location;
    }

    // getters/setters
    public String getId(){ return id; } public void setId(String id){ this.id=id; }
    public String getType(){ return type; } public void setType(String type){ this.type=type; }
    public String getLabel(){ return label; } public void setLabel(String label){ this.label=label; }
    public String getHours(){ return hours; } public void setHours(String hours){ this.hours=hours; }
    public GeoJsonPoint getLocation(){ return location; } public void setLocation(GeoJsonPoint location){ this.location=location; }

    public String getGmapsDirectionsUrl() { return gmapsDirectionsUrl; }
    public void setGmapsDirectionsUrl(String gmapsDirectionsUrl) { this.gmapsDirectionsUrl = gmapsDirectionsUrl; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<String> getMenuItems() { return menuItems; }
    public void setMenuItems(List<String> menuItems) { this.menuItems = menuItems; }

    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }
}
