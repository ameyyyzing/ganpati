package com.puneganpatidarshan.ganpati_backend.dto.plan;

public class PlanStop {
    private String id;
    private String name;
    private double lat;
    private double lng;

    private int dwellMins;
    private double legDistanceMeters;
    private int legTravelMins;

    private String arrivalIso;
    private String departIso;

    public PlanStop() {}

    public PlanStop(String id, String name, double lat, double lng,
                    int dwellMins, double legDistanceMeters, int legTravelMins,
                    String arrivalIso, String departIso) {
        this.id = id; this.name = name; this.lat = lat; this.lng = lng;
        this.dwellMins = dwellMins; this.legDistanceMeters = legDistanceMeters;
        this.legTravelMins = legTravelMins; this.arrivalIso = arrivalIso; this.departIso = departIso;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public int getDwellMins() { return dwellMins; }
    public double getLegDistanceMeters() { return legDistanceMeters; }
    public int getLegTravelMins() { return legTravelMins; }
    public String getArrivalIso() { return arrivalIso; }
    public String getDepartIso() { return departIso; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }
    public void setDwellMins(int dwellMins) { this.dwellMins = dwellMins; }
    public void setLegDistanceMeters(double legDistanceMeters) { this.legDistanceMeters = legDistanceMeters; }
    public void setLegTravelMins(int legTravelMins) { this.legTravelMins = legTravelMins; }
    public void setArrivalIso(String arrivalIso) { this.arrivalIso = arrivalIso; }
    public void setDepartIso(String departIso) { this.departIso = departIso; }
}
