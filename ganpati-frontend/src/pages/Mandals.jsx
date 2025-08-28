import { useEffect, useMemo, useState } from "react";
import { api } from "../api/client";
import { Link } from "react-router-dom";

function toLatLng(m) {
  const c = m?.location?.coordinates;
  return Array.isArray(c) ? [c[1], c[0]] : [0,0]; // [lat,lng]
}
function haversineKm(aLat,aLng,bLat,bLng){
  const toRad = d => d*Math.PI/180;
  const R=6371;
  const dLat=toRad(bLat-aLat), dLng=toRad(bLng-aLng);
  const s1=Math.sin(dLat/2)**2, s2=Math.cos(toRad(aLat))*Math.cos(toRad(bLat))*Math.sin(dLng/2)**2;
  return 2*R*Math.asin(Math.sqrt(s1+s2));
}

export default function Mandals(){
  const [rows,setRows]=useState([]);
  const [q,setQ]=useState("");
  const [topOnly,setTopOnly]=useState(true);
  const [sortBy,setSortBy]=useState("rank"); // rank|distance|name
  const [me,setMe]=useState(null);

  useEffect(()=>{ api.get(`/mandals?topOnly=${topOnly}`).then(r=>setRows(r.data)).catch(()=>setRows([])); },[topOnly]);
  useEffect(()=>{ if(navigator.geolocation){ navigator.geolocation.getCurrentPosition(p=>setMe({lat:p.coords.latitude,lng:p.coords.longitude}),()=>{});} },[]);

  const list = useMemo(()=>{
    let out = rows.filter(m =>
      (m.name||"").toLowerCase().includes(q.toLowerCase()) ||
      (m.area||"").toLowerCase().includes(q.toLowerCase())
    );
    if (sortBy==="distance" && me){
      out = out.map(m=>{
        const [lat,lng]=toLatLng(m);
        return {...m, _dist: haversineKm(me.lat,me.lng,lat,lng)};
      }).sort((a,b)=> (a._dist??1e9)-(b._dist??1e9));
    } else if (sortBy==="name"){
      out = out.sort((a,b)=> (a.name||"").localeCompare(b.name||""));
    } else {
      out = out.sort((a,b)=> (a.rank??99)-(b.rank??99) || (a.name||"").localeCompare(b.name||""));
    }
    return out;
  },[rows,q,sortBy,me]);

  return (
    <div className="pad">
      <h2>Top Mandals</h2>

      <div className="controls" style={{display:"flex",gap:10,flexWrap:"wrap",marginBottom:12}}>
        <input value={q} onChange={e=>setQ(e.target.value)} placeholder="Search by name or area" style={{padding:"8px 10px",border:"1px solid #ddd",borderRadius:8,flex:"1 1 240px"}} />
        <label style={{display:"flex",alignItems:"center",gap:6}}>
          <input type="checkbox" checked={topOnly} onChange={e=>setTopOnly(e.target.checked)} /> Top 5 only
        </label>
        <select value={sortBy} onChange={e=>setSortBy(e.target.value)} style={{padding:"8px 10px",border:"1px solid #ddd",borderRadius:8}}>
          <option value="rank">Sort: Rank</option>
          <option value="name">Sort: Name</option>
          <option value="distance" disabled={!me}>Sort: Distance {me ? "" : "(enable location)"}</option>
        </select>
        <Link className="btn" to="/map">Open Map</Link>
      </div>

      <div className="list">
        {list.map(m=>{
          const [lat,lng]=toLatLng(m);
          const dist = me ? haversineKm(me.lat,me.lng,lat,lng) : null;
          return (
            <div className="list-item" key={m.id || m._id || m.name}>
              <div className="title">
                {m.name}
                {m.rank ? <span className="badge">Manacha {m.rank}</span> : null}
              </div>
              <div className="muted">{m.area || ""}{dist!=null ? ` • ${dist.toFixed(2)} km away` : ""}</div>
              {m.about && <p style={{margin:"6px 0"}}>{m.about}</p>}
              <div style={{display:"flex",gap:8,flexWrap:"wrap"}}>
                <a className="btn" target="_blank" rel="noreferrer" href={m.gmapsDirectionsUrl || `https://www.google.com/maps/dir/?api=1&destination=${lat},${lng}`}>Directions →</a>
                <Link className="btn" to={`/map?lat=${lat}&lng=${lng}&z=16`}>View on Map</Link>
              </div>
            </div>
          );
        })}
        {list.length===0 && <div className="muted">No mandals found.</div>}
      </div>
    </div>
  );
}
