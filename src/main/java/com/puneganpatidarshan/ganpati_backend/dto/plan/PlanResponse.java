package com.puneganpatidarshan.ganpati_backend.dto.plan;

import java.util.List;

public class PlanResponse {
    private List<PlanStop> stops;
    private int totalTravelMins;
    private int totalDwellMins;
    private int totalMins;
    private String finishIso;
    private boolean overBudget;
    private String note;

    public PlanResponse() {}

    public PlanResponse(List<PlanStop> stops, int totalTravelMins, int totalDwellMins,
                        int totalMins, String finishIso, boolean overBudget, String note) {
        this.stops = stops; this.totalTravelMins = totalTravelMins; this.totalDwellMins = totalDwellMins;
        this.totalMins = totalMins; this.finishIso = finishIso; this.overBudget = overBudget; this.note = note;
    }

    public List<PlanStop> getStops() { return stops; }
    public int getTotalTravelMins() { return totalTravelMins; }
    public int getTotalDwellMins() { return totalDwellMins; }
    public int getTotalMins() { return totalMins; }
    public String getFinishIso() { return finishIso; }
    public boolean isOverBudget() { return overBudget; }
    public String getNote() { return note; }

    public void setStops(List<PlanStop> stops) { this.stops = stops; }
    public void setTotalTravelMins(int totalTravelMins) { this.totalTravelMins = totalTravelMins; }
    public void setTotalDwellMins(int totalDwellMins) { this.totalDwellMins = totalDwellMins; }
    public void setTotalMins(int totalMins) { this.totalMins = totalMins; }
    public void setFinishIso(String finishIso) { this.finishIso = finishIso; }
    public void setOverBudget(boolean overBudget) { this.overBudget = overBudget; }
    public void setNote(String note) { this.note = note; }
}
