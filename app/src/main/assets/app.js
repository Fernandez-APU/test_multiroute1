// The ui folder (app/src/main/java/com/example/test1_multiroute/ui/) contains existing Android fragments
// and ViewModels for other parts of the app, but is not used in this WebView-based integration. 
// All web content is handled by the WebView in MainActivity.kt.


const tomtomKey = 'YOUR_TOMTOM_API_KEY';
let map;

async function fetchOptimizedRoute(waypoints) {
  const resp = await fetch('http://10.0.2.2:8080/getOptimizedRoute', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(waypoints)
  });
  return resp.json();
}

function initMap() {
  map = tt.map({
    key: tomtomKey,
    container: 'map',
    center: [101.6869, 3.139],
    zoom: 12
  });
}

async function loadRoute() {
  const waypoints = [
    [3.15785, 101.7123],
    [3.14777, 101.7111],
    [3.13900, 101.6869]
  ];

  const result = await fetchOptimizedRoute(waypoints);
  const coords = result.routes[0].legs
    .flatMap(leg => leg.points.map(p => [p.longitude, p.latitude]));

  map.on('load', () => {
    map.addSource('route', {
      type: 'geojson',
      data: { type: 'Feature', geometry: { type: 'LineString', coordinates: coords } }
    });
    map.addLayer({
      id: 'routeLine',
      type: 'line',
      source: 'route',
      paint: { 'line-width': 4 }
    });
    waypoints.forEach(pt => new tt.Marker().setLngLat(pt).addTo(map));
  });

  document.getElementById('sidebar').innerText =
    JSON.stringify(result.routes[0], null, 2);
}

// Initialize
initMap();
map.on('load', loadRoute);