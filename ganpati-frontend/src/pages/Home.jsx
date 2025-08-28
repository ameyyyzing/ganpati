// src/pages/Home.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/client";

function toLatLng(loc) {
  if (!loc) return [0, 0];
  if (Array.isArray(loc.coordinates)) return [loc.coordinates[1], loc.coordinates[0]];
  if (typeof loc.y === "number" && typeof loc.x === "number") return [loc.y, loc.x];
  return [0, 0];
}

export default function Home() {
  const [topMandals, setTopMandals] = useState([]);
  const [advisories, setAdvisories] = useState([]);

  useEffect(() => {
    api.get("/mandals?topOnly=true").then(r => setTopMandals(r.data || [])).catch(()=>setTopMandals([]));
    api.get("/advisories").then(r => setAdvisories(r.data || [])).catch(()=>setAdvisories([]));
  }, []);

  return (
    <div className="home">

      {/* HERO */}
      <section className="hero card">
        <div className="hero-inner">
          <div className="hero-left">
            <div className="hero-title">🪔 Pune Ganpati Darshan</div>
            <p className="hero-sub">
              Plan your darshan across <b>Manache 5</b>, <b>Dagdusheth</b> & heritage mandals.
              Smart route, timings & one-tap directions.
            </p>
            <div className="cta-row">
              <Link to="/plan" className="btn primary">Plan My Darshan</Link>
              <Link to="/map" className="btn ghost">Open Live Map</Link>
              <Link to="/advisories" className="btn ghost">
                Advisories {advisories.length ? `(${advisories.length})` : ""}
              </Link>
            </div>
          </div>
          <div className="hero-art">
            <img src="/ganpati-og.jpg" alt="Ganpati" />
          </div>
        </div>
        <div className="toran" aria-hidden />
      </section>

      {/* FEATURES */}
      <section className="section">
        <div className="section-title">🪔 Festival essentials</div>
        <div className="feature-grid">
          <div className="feature-card card">
            <div className="feature-icon">🗺️</div>
            <div className="feature-title">Live Map</div>
            <p>Mandals, parking, toilets & metro—on one map with directions.</p>
            <Link to="/map" className="btn ghost">Open Map →</Link>
          </div>
          <div className="feature-card card">
            <div className="feature-icon">📍</div>
            <div className="feature-title">Plan My Darshan</div>
            <p>Give your time budget; we’ll optimize the route & timings.</p>
            <Link to="/plan" className="btn ghost">Plan now →</Link>
          </div>
          <div className="feature-card card">
            <div className="feature-icon">⚠️</div>
            <div className="feature-title">Advisories</div>
            <p>Road closures & crowd alerts so you can avoid surprises.</p>
            <Link to="/advisories" className="btn ghost">
              View advisories {advisories.length ? `(${advisories.length})` : ""} →
            </Link>
          </div>
          <div className="feature-card card">
            <div className="feature-icon">🥤</div>
            <div className="feature-title">Comfort stops</div>
            <p>Nearest toilets & medical help—just a tap away during darshan.</p>
            <Link to="/food" className="btn ghost">Find nearby →</Link>
          </div>
        </div>
      </section>

      {/* TOP MANDALS */}
      <section className="section">
        <div className="section-title">🪔 Manache 5</div>
        <div className="mandal-grid">
          {topMandals.map(m => {
            const id = m.id || m._id;
            const [lat, lng] = toLatLng(m.location);
            return (
              <div key={id} className="mandal-card card">
                <div className="mandal-head">
                  <span className="rank-badge" title={`Manacha ${m.rank}`}>{m.rank}</span>
                  <div className="mandal-name">{m.name}</div>
                </div>
                <div className="mandal-area">{m.area}</div>
                <div className="mandal-actions">
                  <a className="btn ghost" href={m.gmapsDirectionsUrl || `https://www.google.com/maps/dir/?api=1&destination=${lat},${lng}`} target="_blank" rel="noreferrer">
                    Directions →
                  </a>
                </div>
              </div>
            );
          })}
        </div>
        <div style={{marginTop:12}}>
          <Link to="/mandals" className="btn">View all mandals</Link>
        </div>
      </section>

    </div>
  );
}
