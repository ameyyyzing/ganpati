import { useEffect, useState } from "react";
import { api } from "../api/client";

export default function ComfortFood(){
  const [items, setItems] = useState([]);

  useEffect(()=>{
    api.get("/amenities?type=food").then(r => setItems(r.data || [])).catch(()=>setItems([]));
  },[]);

  return (
    <div className="container" style={{maxWidth:900, margin:"16px auto"}}>
      <h2>🍽️ Comfort Food Near Mandals</h2>
      <p className="muted" style={{marginTop:4}}>
        Quick bites & sweets around the core mandal routes.
      </p>

      <div style={{display:"grid", gridTemplateColumns:"repeat(auto-fill,minmax(260px,1fr))", gap:12, marginTop:12}}>
        {items.map((f)=>(
          <div key={f.id || f._id || f.label} className="card" style={{display:"flex",flexDirection:"column",gap:6}}>
            <div style={{display:"flex",justifyContent:"space-between",alignItems:"center"}}>
              <div style={{fontWeight:900, color:"var(--maroon)"}}>{f.label}</div>
              {f.priceRange && <span className="badge">{f.priceRange}</span>}
            </div>
            {f.address && <div style={{fontSize:13, color:"var(--ink)"}}>{f.address}</div>}
            {f.hours && <div className="muted" style={{fontSize:12}}>Hours: {f.hours}</div>}
            {Array.isArray(f.menuItems) && f.menuItems.length>0 && (
              <div style={{display:"flex", flexWrap:"wrap", gap:6, marginTop:4}}>
                {f.menuItems.map((m,i)=>(
                  <span key={i} className="menu-chip">{m}</span>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
