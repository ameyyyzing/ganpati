package com.puneganpatidarshan.ganpati_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("advisories")
public class Advisory {
    @Id
    private String id;

    private String title;
    private String body;
    private String area;
    private Instant startsAt;     // null = no start bound
    private Instant endsAt;       // null = no end bound
    private String severity;      // info | warning | closure
    private boolean active;       // true = show

    public Advisory() {}

    public Advisory(String id, String title, String body, String area,
                    Instant startsAt, Instant endsAt, String severity, boolean active) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.area = area;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.severity = severity;
        this.active = active;
    }

    // Getters & setters
    public String getId() { return id; }
    public void   setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void   setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void   setBody(String body) { this.body = body; }

    public String getArea() { return area; }
    public void   setArea(String area) { this.area = area; }

    public Instant getStartsAt() { return startsAt; }
    public void    setStartsAt(Instant startsAt) { this.startsAt = startsAt; }

    public Instant getEndsAt() { return endsAt; }
    public void    setEndsAt(Instant endsAt) { this.endsAt = endsAt; }

    public String getSeverity() { return severity; }
    public void   setSeverity(String severity) { this.severity = severity; }

    public boolean isActive() { return active; }
    public void    setActive(boolean active) { this.active = active; }
}
