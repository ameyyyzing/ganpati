// src/App.jsx
import { BrowserRouter, Routes, Route, Link, NavLink } from "react-router-dom";
import Home from "./pages/Home";
import MapPage from "./pages/MapPage";
import Mandals from "./pages/Mandals";
import Advisories from "./pages/Advisories";
import PlanPage from "./pages/PlanPage";
import ComfortFood from "./pages/ComfortFood";

export default function App() {
  return (
    <BrowserRouter>
      <div className="site">
        <Header />
        <main className="container">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/map" element={<MapPage />} />
            <Route path="/mandals" element={<Mandals />} />
            <Route path="/advisories" element={<Advisories />} />
            <Route path="/plan" element={<PlanPage />} />
            <Route path="/food" element={<ComfortFood />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </BrowserRouter>
  );
}

function Header() {
  return (
    <header className="topbar">
      <div className="container topbar-inner">
        <Link to="/" className="brand">Pune Ganpati Darshan</Link>
        <nav className="nav">
          <NavLink to="/map">Map</NavLink>
          <NavLink to="/mandals">Mandals</NavLink>
          <NavLink to="/advisories">Advisories</NavLink>
          <NavLink to="/plan">Plan</NavLink>
        </nav>
      </div>
    </header>
  );
}

function Footer() {
  return (
    <footer className="footer container">
      <div>Made with ❤️ in Pune • <a href="/map">Open Map</a></div>
    </footer>
  );
}

function NotFound() {
  return (
    <div className="pad">
      <h2>Page not found</h2>
      <p><Link to="/">Go home</Link></p>
    </div>
  );
}
