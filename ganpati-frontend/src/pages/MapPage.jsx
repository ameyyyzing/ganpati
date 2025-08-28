// src/pages/MapPage.jsx
import { useEffect, useMemo, useRef, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import L from "leaflet";
import { api } from "../api/client";
import { toLatLng } from "../lib/geo";

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: new URL("leaflet/dist/images/marker-icon-2x.png", import.meta.url).toString(),
  iconUrl: new URL("leaflet/dist/images/marker-icon.png", import.meta.url).toString(),
  shadowUrl: new URL("leaflet/dist/images/marker-shadow.png", import.meta.url).toString(),
});

export default function MapPage() {
  const center = useMemo(() => [18.5196, 73.8553], []);
  const mapRef = useRef(null);
  const [radiusKm, setRadiusKm] = useState(3);

  const [mandals, setMandals] = useState([]);
  const [parking, setParking] = useState([]);
  const [amenities, setAmenities] = useState([]);
  const [transit, setTransit] = useState([]);
  const [show, setShow] = useState({
    mandals: true, parking: false, toilets: false, medical: false, metro: false
  });
  const [near, setNear] = useState(null);
  const [nearMeta, setNearMeta] = useState(null); // {lat,lng,radiusKm}

  // ----- helpers -----
  async function nearAt(lat, lng) {
    const data = await api
      .get(`/near?lat=${lat}&lng=${lng}&radiusKm=${radiusKm}&limit=3`)
      .then(r => r.data)
      .catch(() => null);
    setNear(data);
    setNearMeta({ lat, lng, radiusKm });
  }

  function handleNearMapCenter() {
    const c = mapRef.current ? mapRef.current.getCenter() : { lat: center[0], lng: center[1] };
    nearAt(c.lat, c.lng);
  }

  async function handleNearMe() {
    if (!navigator.geolocation) { alert("Geolocation not available"); return; }
    navigator.geolocation.getCurrentPosition(
      (pos) => nearAt(pos.coords.latitude, pos.coords.longitude),
      () => alert("Location permission denied or unavailable."),
      { enableHighAccuracy: true, timeout: 10000 }
    );
  }

  // ----- load data & preload near() for map center -----
  useEffect(() => {
    (async () => {
      const [m, p, a, t] = await Promise.all([
        api.get("/mandals").then(r => r.data).catch(()=>[]),
        api.get("/parking").then(r => r.data).catch(()=>[]),
        api.get("/amenities").then(r => r.data).catch(()=>[]),
        api.get("/transit").then(r => r.data).catch(()=>[]),
      ]);
      setMandals(m); setParking(p); setAmenities(a); setTransit(t);
      // show nearby for Pune center by default
      nearAt(center[0], center[1]);
    })();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const toilets = amenities.filter(a => (a.type||"").toLowerCase()==="toilet");
  const medical  = amenities.filter(a => (a.type||"").toLowerCase()==="medical");

  return (
    <div className="map-page">
      <div className="layer-toggle">
        <label><input type="checkbox" checked={show.mandals} onChange={e=>setShow(s=>({...s, mandals:e.target.checked}))}/> Mandals</label>
        <label><input type="checkbox" checked={show.parking} onChange={e=>setShow(s=>({...s, parking:e.target.checked}))}/> Parking</label>
        <label><input type="checkbox" checked={show.toilets} onChange={e=>setShow(s=>({...s, toilets:e.target.checked}))}/> Toilets</label>
        <label><input type="checkbox" checked={show.medical} onChange={e=>setShow(s=>({...s, medical:e.target.checked}))}/> Medical</label>
        <label><input type="checkbox" checked={show.metro} onChange={e=>setShow(s=>({...s, metro:e.target.checked}))}/> Metro</label>

        <span style={{marginLeft:12}}>
          Radius <input type="number" min="1" max="10" value={radiusKm}
                        onChange={e=>setRadiusKm(Number(e.target.value)||3)}
                        style={{width:50, marginLeft:6}} /> km
        </span>
        <button className="btn small" onClick={handleNearMe}>Near Me</button>
        <button className="btn small" onClick={handleNearMapCenter}>Near (Map Center)</button>
      </div>

      <div className="map-wrap">
        <MapContainer
          center={center}
          zoom={14}
          style={{height:"70vh", width:"100%"}}
          whenCreated={(map) => { mapRef.current = map; }}
        >
          <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

          {show.mandals && mandals.map(m => {
            const [lat, lng] = toLatLng(m.location);
            return (
              <Marker key={m.id || m._id || m.name} position={[lat, lng]}>
                <Popup>
                  <div>
                    <strong>{m.name}</strong>{m.rank ? ` (Manacha ${m.rank})` : ""}
                    <div style={{marginTop:6}}>
                      <a href={m.gmapsDirectionsUrl || `https://www.google.com/maps/dir/?api=1&destination=${lat},${lng}`} target="_blank" rel="noreferrer">
                        Open in Google Maps
                      </a>
                    </div>
                  </div>
                </Popup>
              </Marker>
            );
          })}

          {show.parking && parking.map(p => {
            const [lat, lng] = toLatLng(p.location);
            return (
              <Marker key={p.id || p._id || p.label} position={[lat, lng]}>
                <Popup>
                  <div>
                    <strong>{p.label}</strong> {p.status ? `• ${p.status}` : ""}
                    <div>{p.fee || ""} {p.hours ? `• ${p.hours}` : ""}</div>
                    <div style={{marginTop:6}}>
                      <a href={p.gmapsDirectionsUrl || `https://www.google.com/maps/dir/?api=1&destination=${lat},${lng}`} target="_blank" rel="noreferrer">
                        Directions
                      </a>
                    </div>
                  </div>
                </Popup>
              </Marker>
            );
          })}

          {show.toilets && toilets.map(a => {
            const [lat, lng] = toLatLng(a.location);
            return (
              <Marker key={a.id || a._id || a.label} position={[lat, lng]}>
                <Popup>
                  <div>
                    <strong>{a.label}</strong> • Toilet
                    <div>{a.hours || ""}</div>
                    <a href={`https://www.google.com/maps/dir/?api=1&destination=${lat},${lng}`} target="_blank" rel="noreferrer">Directions</a>
                  </div>
                </Popup>
              </Marker>
            );
          })}

          {show.medical && medical.map(a => {
            const [lat, lng] = toLatLng(a.location);
            return (
              <Marker key={a.id || a._id || a.label} position={[lat, lng]}>
                <Popup>
                  <div>
                    <strong>{a.label}</strong> • Medical
                    <div>{a.hours || ""}</div>
                    <a href={`https://www.google.com/maps/dir/?api=1&destination=${lat},${lng}`} target="_blank" rel="noreferrer">Directions</a>
                  </div>
                </Popup>
              </Marker>
            );
          })}

          {show.metro && transit.filter(t => (t.type||"").toLowerCase()==="metro").map(t => {
            const [lat, lng] = toLatLng(t.location);
            return (
              <Marker key={t.id || t._id || t.label} position={[lat, lng]}>
                <Popup>
                  <div>
                    <strong>{t.label}</strong> • Metro {t.line ? `(${t.line})` : ""}
                    <div>{t.firstTrain && t.lastTrain ? `First ${t.firstTrain} • Last ${t.lastTrain}` : ""}</div>
                    <a href={`https://www.google.com/maps/dir/?api=1&destination=${lat},${lng}`} target="_blank" rel="noreferrer">Directions</a>
                  </div>
                </Popup>
              </Marker>
            );
          })}
        </MapContainer>
      </div>

      {near ? (
        <div className="panel">
          <strong>Near you</strong>
          {nearMeta && (
            <div className="muted" style={{marginTop:4}}>
              lat {nearMeta.lat.toFixed(5)}, lng {nearMeta.lng.toFixed(5)} • {nearMeta.radiusKm} km
            </div>
          )}
          {["toilets","medical","parking","metro"].map(k => (
            <div key={k} className="panel-section">
              <div className="tag">{k}</div>
              {(near[k]||[]).map(item => (
                <div key={item.id} className="line">
                  {item.name} • {(item.distanceMeters/1000).toFixed(2)} km — <a href={item.directionsUrl} target="_blank" rel="noreferrer">Go</a>
                </div>
              ))}
              {(!near[k] || near[k].length===0) && <div className="muted">None nearby</div>}
            </div>
          ))}
        </div>
      ) : (
        <div className="panel muted">Click <b>Near Me</b> or <b>Near (Map Center)</b> to see nearby spots.</div>
      )}
    </div>
  );
}
