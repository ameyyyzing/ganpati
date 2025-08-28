package com.puneganpatidarshan.ganpati_backend.dto;

import java.util.List;

public class NearResponse {
    private List<NearItem> toilets;
    private List<NearItem> medical;
    private List<NearItem> parking;
    private List<NearItem> metro;

    public NearResponse() {}
    public NearResponse(List<NearItem> toilets, List<NearItem> medical,
                        List<NearItem> parking, List<NearItem> metro) {
        this.toilets=toilets; this.medical=medical; this.parking=parking; this.metro=metro;
    }

    public List<NearItem> getToilets(){ return toilets; } public void setToilets(List<NearItem> v){ this.toilets=v; }
    public List<NearItem> getMedical(){ return medical; } public void setMedical(List<NearItem> v){ this.medical=v; }
    public List<NearItem> getParking(){ return parking; } public void setParking(List<NearItem> v){ this.parking=v; }
    public List<NearItem> getMetro(){ return metro; } public void setMetro(List<NearItem> v){ this.metro=v; }
}
