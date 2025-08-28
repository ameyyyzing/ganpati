package com.puneganpatidarshan.ganpati_backend.service;

import com.puneganpatidarshan.ganpati_backend.dto.NearItem;
import com.puneganpatidarshan.ganpati_backend.dto.NearResponse;
import com.puneganpatidarshan.ganpati_backend.model.Amenity;
import com.puneganpatidarshan.ganpati_backend.model.Parking;
import com.puneganpatidarshan.ganpati_backend.model.Transit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point; // <-- correct import
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NearService {
    private final MongoTemplate mongo;

    @Value("${app.gmaps.dir-prefix:https://www.google.com/maps/dir/?api=1&destination=}")
    private String dirPrefix;

    public NearService(MongoTemplate mongo) { this.mongo = mongo; }

    public NearResponse findNear(double lat, double lng, double radiusKm, int limit) {
        var toilets = nearAmenities(lat, lng, radiusKm, limit, "toilet");
        var medical = nearAmenities(lat, lng, radiusKm, limit, "medical");
        var parking = nearParking(lat, lng, radiusKm, limit);
        var metro   = nearTransit(lat, lng, radiusKm, limit, "metro");
        return new NearResponse(toilets, medical, parking, metro);
    }

    private List<NearItem> nearAmenities(double lat, double lng, double rKm, int limit, String type) {
        Query q = Query.query(Criteria.where("type").is(type));
        var res = geoNear(lat, lng, rKm, limit, q, Amenity.class);
        return res.stream().map(r -> {
            Amenity a = r.getContent();
            var p = a.getLocation(); // GeoJsonPoint (x=lng, y=lat)
            double meters = r.getDistance() == null ? 0 : r.getDistance().getValue() * 1000.0;
            return new NearItem(
                    a.getId(), a.getLabel(), type, p.getY(), p.getX(),
                    meters, dirPrefix + p.getY() + "," + p.getX(), null
            );
        }).toList();
    }

    private List<NearItem> nearParking(double lat, double lng, double rKm, int limit) {
        var res = geoNear(lat, lng, rKm, limit, new Query(), Parking.class);
        return res.stream().map(r -> {
            Parking p = r.getContent();
            var gp = p.getLocation();
            double meters = r.getDistance() == null ? 0 : r.getDistance().getValue() * 1000.0;
            return new NearItem(
                    p.getId(), p.getLabel(), "parking", gp.getY(), gp.getX(),
                    meters, dirPrefix + gp.getY() + "," + gp.getX(), p.getStatus()
            );
        }).toList();
    }

    private List<NearItem> nearTransit(double lat, double lng, double rKm, int limit, String type) {
        Query q = Query.query(Criteria.where("type").is(type));
        var res = geoNear(lat, lng, rKm, limit, q, Transit.class);
        return res.stream().map(r -> {
            Transit t = r.getContent();
            var gp = t.getLocation();
            double meters = r.getDistance() == null ? 0 : r.getDistance().getValue() * 1000.0;
            return new NearItem(
                    t.getId(), t.getLabel(), type, gp.getY(), gp.getX(),
                    meters, dirPrefix + gp.getY() + "," + gp.getX(), null
            );
        }).toList();
    }

    private <T> List<GeoResult<T>> geoNear(double lat, double lng, double rKm, int limit,
                                           Query filter, Class<T> clazz) {
        // (x=lng, y=lat)
        Point point = new Point(lng, lat);

        // Build NearQuery with distance + limit; attach any extra filters
        NearQuery near = NearQuery.near(point)
                .maxDistance(new Distance(rKm, Metrics.KILOMETERS))
                .limit(limit)
                .query(filter);

        // Use the 2-arg overload (lets @Document decide the collection name)
        return mongo.geoNear(near, clazz).getContent();
    }
}
