package com.puneganpatidarshan.ganpati_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("transit")
public class Transit {
    @Id private String id;
    private String type;             // metro|bus
    private String label;            // "PMC Metro Station"
    private String line;             // e.g., "Aqua"
    private String firstTrain;       // "05:30"
    private String lastTrain;        // "23:00"

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    public Transit() {}
    public Transit(String id, String type, String label, String line, String firstTrain, String lastTrain, GeoJsonPoint location) {
        this.id=id; this.type=type; this.label=label; this.line=line; this.firstTrain=firstTrain; this.lastTrain=lastTrain; this.location=location;
    }

    public String getId(){ return id; } public void setId(String id){ this.id=id; }
    public String getType(){ return type; } public void setType(String type){ this.type=type; }
    public String getLabel(){ return label; } public void setLabel(String label){ this.label=label; }
    public String getLine(){ return line; } public void setLine(String line){ this.line=line; }
    public String getFirstTrain(){ return firstTrain; } public void setFirstTrain(String s){ this.firstTrain=s; }
    public String getLastTrain(){ return lastTrain; } public void setLastTrain(String s){ this.lastTrain=s; }
    public GeoJsonPoint getLocation(){ return location; } public void setLocation(GeoJsonPoint location){ this.location=location; }
}
