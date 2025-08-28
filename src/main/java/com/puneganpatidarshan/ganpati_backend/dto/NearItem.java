package com.puneganpatidarshan.ganpati_backend.dto;

public class NearItem {
    private String id;
    private String name;          // label/name
    private String type;          // toilet|medical|parking|metro
    private double lat;
    private double lng;
    private double distanceMeters;
    private String directionsUrl; // Google Maps
    private String status;        // parking only

    public NearItem() {}
    public NearItem(String id, String name, String type, double lat, double lng,
                    double distanceMeters, String directionsUrl, String status) {
        this.id=id; this.name=name; this.type=type; this.lat=lat; this.lng=lng;
        this.distanceMeters=distanceMeters; this.directionsUrl=directionsUrl; this.status=status;
    }

    public String getId(){ return id; } public void setId(String id){ this.id=id; }
    public String getName(){ return name; } public void setName(String name){ this.name=name; }
    public String getType(){ return type; } public void setType(String type){ this.type=type; }
    public double getLat(){ return lat; } public void setLat(double lat){ this.lat=lat; }
    public double getLng(){ return lng; } public void setLng(double lng){ this.lng=lng; }
    public double getDistanceMeters(){ return distanceMeters; } public void setDistanceMeters(double d){ this.distanceMeters=d; }
    public String getDirectionsUrl(){ return directionsUrl; } public void setDirectionsUrl(String d){ this.directionsUrl=d; }
    public String getStatus(){ return status; } public void setStatus(String status){ this.status=status; }
}
