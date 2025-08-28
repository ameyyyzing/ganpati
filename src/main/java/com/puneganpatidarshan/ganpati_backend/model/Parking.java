package com.puneganpatidarshan.ganpati_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("parking")
public class Parking {
    @Id private String id;
    private String label;            // "Shaniwar Wada Parking"
    private Integer capacity;        // optional
    private String fee;              // "₹20/hr"
    private String hours;            // "06:00–23:00"
    private String status;           // open|full|closed

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;   // new GeoJsonPoint(lng, lat)

    private String gmapsDirectionsUrl;

    public Parking() {}
    public Parking(String id, String label, Integer capacity, String fee, String hours, String status,
                   GeoJsonPoint location, String gmapsDirectionsUrl) {
        this.id = id; this.label = label; this.capacity = capacity; this.fee = fee; this.hours = hours;
        this.status = status; this.location = location; this.gmapsDirectionsUrl = gmapsDirectionsUrl;
    }

    public String getId() { return id; } public void setId(String id){ this.id=id; }
    public String getLabel(){ return label; } public void setLabel(String label){ this.label=label; }
    public Integer getCapacity(){ return capacity; } public void setCapacity(Integer capacity){ this.capacity=capacity; }
    public String getFee(){ return fee; } public void setFee(String fee){ this.fee=fee; }
    public String getHours(){ return hours; } public void setHours(String hours){ this.hours=hours; }
    public String getStatus(){ return status; } public void setStatus(String status){ this.status=status; }
    public GeoJsonPoint getLocation(){ return location; } public void setLocation(GeoJsonPoint location){ this.location=location; }
    public String getGmapsDirectionsUrl(){ return gmapsDirectionsUrl; } public void setGmapsDirectionsUrl(String s){ this.gmapsDirectionsUrl=s; }
}
