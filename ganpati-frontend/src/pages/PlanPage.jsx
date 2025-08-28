// src/pages/PlanPage.jsx
import { useEffect, useState } from "react";
import { api } from "../api/client";

function toLocalInputValue(date = new Date()) {
  const pad = (n) => String(n).padStart(2, "0");
  const d = new Date(date);
  return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

export default function PlanPage(){
  const [mandals, setMandals] = useState([]);
  const [start, setStart] = useState({ lat: 18.5196, lng: 73.8553 });
  const [startTime, setStartTime] = useState(toLocalInputValue());
  const [mins, setMins] = useState(180);
  const [must, setMust] = useState(new Set());
  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(()=>{
    api.get("/mandals").then(r=>{
      const list = r.data || [];
      setMandals(list);
      const pre = new Set(list.filter(m=>{
        if (m.rank) return m.rank>=1 && m.rank<=5;
        const n=(m.name||"").toLowerCase();
        return n.includes("dagdusheth") || n.includes("rangari");
      }).map(m=>m.id||m._id));
      setMust(pre);
    }).catch(()=>setMandals([]));

    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition(
        (p)=> setStart({lat:p.coords.latitude, lng:p.coords.longitude}),
        ()=>{} // ignore
      );
    }
  },[]);

  const toggle = (id) => setMust(s => {
    const n = new Set(s);
    n.has(id) ? n.delete(id) : n.add(id);
    return n;
  });

  async function submit(){
    setLoading(true);
    try {
      const body = {
        startLat: start.lat,
        startLng: start.lng,
        startTime: new Date(startTime).toISOString(),
        durationMins: Number(mins),
        mustSeeIds: Array.from(must)
      };
      const res = await api.post("/plan", body).then(r=>r.data);
      setPlan(res);
      window.scrollTo({ top: 0, behavior: "smooth" });
    } finally { setLoading(false); }
  }

  return (
    <div className="plan-page container" style={{marginTop:16}}>
      {/* Header */}
      <div className="plan-hero">
        <div className="title">🪔 Plan My Darshan</div>
        <div className="subtitle">Cover Manache 5, Dagdusheth & heritage mandals — smart route, timings & one-tap directions.</div>
      </div>

      {/* Form */}
      <div className="card">
        <div className="section-title"><span className="diya">🪔</span> Your preferences</div>

        <div style={{display:"grid", gap:12, gridTemplateColumns:"1fr 1fr", alignItems:"center"}}>
          <div style={{display:"flex", gap:8, alignItems:"center", flexWrap:"wrap"}}>
            <div><b>Start (lat,lng)</b></div>
            <input className="input" value={start.lat} onChange={e=>setStart({...start, lat:Number(e.target.value)})}/>
            <input className="input" value={start.lng} onChange={e=>setStart({...start, lng:Number(e.target.value)})}/>
            <button className="btn ghost" onClick={()=>{
              if (!("geolocation" in navigator)) return alert("Geolocation not available");
              navigator.geolocation.getCurrentPosition(
                (p)=> setStart({lat:p.coords.latitude, lng:p.coords.longitude}),
                ()=> alert("Location permission denied")
              );
            }}>Use my location</button>
          </div>

          <div style={{display:"flex", gap:12, alignItems:"center", flexWrap:"wrap"}}>
            <div style={{display:"flex", flexDirection:"column"}}>
              <label><b>Start time</b></label>
              <input className="input" type="datetime-local" value={startTime} onChange={e=>setStartTime(e.target.value)} />
            </div>
            <div style={{display:"flex", flexDirection:"column"}}>
              <label><b>Time budget (mins)</b></label>
              <input className="input" type="number" min="30" step="15" value={mins} onChange={e=>setMins(e.target.value)} />
            </div>
          </div>
        </div>

        <div className="section-title" style={{marginTop:12}}><span className="diya">🪔</span> Must-see mandals</div>
        <div className="pills">
          {mandals.map(m => {
            const id = m.id || m._id;
            const checked = must.has(id);
            return (
              <label key={id} className={`pill mandal ${checked ? "is-checked":""}`}>
                <input type="checkbox" checked={checked} onChange={()=>toggle(id)} />
                {m.rank ? <span className="rank" title={`Manacha ${m.rank}`}>{m.rank}</span> : null}
                <span>{m.name}</span>
              </label>
            );
          })}
        </div>

        <div style={{marginTop:12}}>
          <button className="btn primary" onClick={submit} disabled={loading}>
            {loading ? "Planning…" : "Plan my darshan"}
          </button>
        </div>
      </div>

      {/* Results */}
      {plan && (
        <div className="card" style={{marginTop:14}}>
          <div className="section-title"><span className="diya">🪔</span> Your itinerary</div>
          <div className="timeline">
            {plan.stops.map((s,i)=>(
              <div key={s.id} className="step">
                <div className="dot" />
                <div className="headline">
                  <span className="badge">Stop {i+1}</span>
                  <span style={{fontWeight:900, color:"var(--maroon)"}}>{s.name}</span>
                </div>
                <div className="meta">
                  travel {s.legTravelMins} min / {(s.legDistanceMeters/1000).toFixed(2)} km • darshan {s.dwellMins} min •
                  {" "}arrive {new Date(s.arrivalIso).toLocaleTimeString()} • depart {new Date(s.departIso).toLocaleTimeString()}
                </div>
                <div className="actions">
                  <a className="btn ghost" href={`https://www.google.com/maps/dir/?api=1&destination=${s.lat},${s.lng}`} target="_blank" rel="noreferrer">Open in Google Maps →</a>
                </div>
              </div>
            ))}
          </div>
          <div className="meta" style={{marginTop:6}}>
            <b>Total:</b> travel {plan.totalTravelMins} + darshan {plan.totalDwellMins} = {plan.totalMins} min •
            finish {new Date(plan.finishIso).toLocaleTimeString()}
          </div>
          <div className="meta" style={{color:"var(--muted)"}}>{plan.note}</div>
        </div>
      )}
    </div>
  );
}
