package com.puneganpatidarshan.ganpati_backend.dto.plan;

import java.time.Instant;
import java.util.List;

public class PlanRequest {
    private double startLat;
    private double startLng;
    private Instant startTime;
    private int durationMins;
    private List<String> mustSeeIds;

    public PlanRequest() {}

    public double getStartLat() { return startLat; }
    public void setStartLat(double startLat) { this.startLat = startLat; }

    public double getStartLng() { return startLng; }
    public void setStartLng(double startLng) { this.startLng = startLng; }

    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }

    public int getDurationMins() { return durationMins; }
    public void setDurationMins(int durationMins) { this.durationMins = durationMins; }

    public List<String> getMustSeeIds() { return mustSeeIds; }
    public void setMustSeeIds(List<String> mustSeeIds) { this.mustSeeIds = mustSeeIds; }
}
