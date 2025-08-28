package com.puneganpatidarshan.ganpati_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("mandals")
public class Mandal {
    @Id private String id;
    private String name;
    private String area;
    private Integer rank; // 1..5 for Manache; null for others

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;   // new GeoJsonPoint(lng, lat)

    private String gmapsDirectionsUrl;
    private String about;
    private String timings;

    public Mandal() {}
    public Mandal(String id, String name, String area, Integer rank,
                  GeoJsonPoint location, String gmapsDirectionsUrl,
                  String about, String timings) {
        this.id=id; this.name=name; this.area=area; this.rank=rank;
        this.location=location; this.gmapsDirectionsUrl=gmapsDirectionsUrl;
        this.about=about; this.timings=timings;
    }

    public String getId(){ return id; } public void setId(String id){ this.id=id; }
    public String getName(){ return name; } public void setName(String name){ this.name=name; }
    public String getArea(){ return area; } public void setArea(String area){ this.area=area; }
    public Integer getRank(){ return rank; } public void setRank(Integer rank){ this.rank=rank; }
    public GeoJsonPoint getLocation(){ return location; } public void setLocation(GeoJsonPoint l){ this.location=l; }
    public String getGmapsDirectionsUrl(){ return gmapsDirectionsUrl; } public void setGmapsDirectionsUrl(String s){ this.gmapsDirectionsUrl=s; }
    public String getAbout(){ return about; } public void setAbout(String about){ this.about=about; }
    public String getTimings(){ return timings; } public void setTimings(String timings){ this.timings=timings; }
}
