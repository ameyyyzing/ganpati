export function toLatLng(location) {
  if (!location) return [0,0];
  if (Array.isArray(location.coordinates)) return [location.coordinates[1], location.coordinates[0]];
  if (typeof location.y === "number" && typeof location.x === "number") return [location.y, location.x];
  if (typeof location.lat === "number" && typeof location.lng === "number") return [location.lat, location.lng];
  return [0,0];
}
