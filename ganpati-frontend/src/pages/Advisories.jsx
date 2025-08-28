// src/pages/Advisories.jsx
import { useEffect, useState } from "react";
import { api } from "../api/client";

export default function Advisories() {
  const [items, setItems] = useState([]);
  useEffect(() => { api.get("/advisories").then(r => setItems(r.data)).catch(()=>setItems([])); }, []);
  return (
    <div className="pad">
      <h2>Advisories</h2>
      {items.length === 0 && <div className="muted">No active advisories right now.</div>}
      <div className="list">
        {items.map(a => (
          <div className="list-item" key={a.id}>
            <div className="title">{a.title}</div>
            <div className="muted">{a.area} • {a.severity}</div>
            <p>{a.body}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
