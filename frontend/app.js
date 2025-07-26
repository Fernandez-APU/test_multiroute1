async function fetchOptimizedRoute(waypoints) {
  const response = await fetch('http://localhost:8080/getOptimizedRoute', {
    method: 'POST',
    headers: {'Content-Type': 'application/json'},
    body: JSON.stringify(waypoints)
  });

  const data = await response.json();
  console.log("Optimized Route:", data);
  
  // TODO: Display clearly on frontend map.
}

// Example usage clearly:
const sampleWaypoints = [
  [3.15785, 101.7123],
  [3.14777, 101.7111],
  [3.13900, 101.6869],
];

fetchOptimizedRoute(sampleWaypoints);
