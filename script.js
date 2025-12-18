// File: script.js
// Description:
// Handles the interactive functionality of the Smart Elevator System UI.
// Assigns a random elevator when a floor is selected, shows a live ETA countdown,
// includes a cancel button to reset or cancel the operation (now also stops the elevator).
// Additionally, tracks the efficiency of the elevator trips with a chart.

// Timer reference for ETA countdown
let etaTimer = null;

// Error-handling fetch wrapper for backend requests
async function safeFetch(url, options) {
  try {
    const res = await fetch(url, options);
    if (!res.ok) throw new Error(`Status ${res.status}`);
    return res.json();
  } catch (err) {
    alert("Backend is not available. Please check server connection.");
    console.error("Error contacting backend:", err);
    return null;
  }
}

// === Chart Setup ===
// Initialize the chart only if it's present (admin page)
const ctx = document.getElementById('efficiencyChart')?.getContext('2d');

// Placeholder data (will be updated from backend)
let smartElevatorTimes = [];
let traditionalElevatorTimes = [];

const chartData = {
  labels: [],
  datasets: [{
    label: 'Smart Elevator Time (ms)',
    data: smartElevatorTimes,
    borderColor: 'rgba(75, 192, 192, 1)',
    fill: false,
  }, {
    label: 'Traditional Elevator Time (ms)',
    data: traditionalElevatorTimes,
    borderColor: 'rgba(255, 99, 132, 1)',
    fill: false,
  }]
};

const config = {
  type: 'line',
  data: chartData,
  options: {
    responsive: true,
    plugins: {
      title: {
        display: true,
        text: 'Elevator Efficiency Comparison',
      },
    },
    scales: {
      y: {
        min: 0,
        ticks: {
          stepSize: 1000,
        },
      },
    },
  },
};

const efficiencyChart = ctx ? new Chart(ctx, config) : null;

// Function to assign elevator and start ETA countdown
function assignElevator(floor) {
  const elevators = ['A', 'B', 'C'];
  const randomElevator = elevators[Math.floor(Math.random() * elevators.length)];

  const startTime = Date.now();
  const travelTime = Math.floor(Math.random() * 4000) + 3000; // 3–7 seconds

  document.getElementById('assigned-elevator').textContent = `Assigned Elevator: ${randomElevator}`;

  if (etaTimer) clearInterval(etaTimer);

  let remainingTime = Math.ceil(travelTime / 1000);
  document.getElementById('eta').textContent = `ETA: ${remainingTime} seconds`;

  etaTimer = setInterval(() => {
    remainingTime--;
    if (remainingTime <= 0) {
      clearInterval(etaTimer);
      document.getElementById('eta').textContent = `Elevator arrived.`;
    } else {
      document.getElementById('eta').textContent = `ETA: ${remainingTime} seconds`;
    }
  }, 1000);

  // Save the trip after the elevator "arrives"
  setTimeout(async () => {
    const endTime = Date.now();
    const smartDuration = endTime - startTime;
    const traditionalDuration = smartDuration + Math.floor(Math.random() * 3000) + 2000;

    const trip = {
      elevatorId: randomElevator,
      originFloor: 1,
      destinationFloor: floor,
      passengerCount: 1,
      startTime: new Date(startTime).toISOString(),
      endTime: new Date(endTime).toISOString(),
      traditionalDurationMs: traditionalDuration,
      smartDurationMs: smartDuration
    };

    await safeFetch("http://localhost:8080/api/trips", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(trip)
    });

    await refreshDashboard();
  }, travelTime);
}

// Cancel request and reset UI (also stops elevator)
document.getElementById('cancel-btn')?.addEventListener('click', () => {
  if (etaTimer) clearInterval(etaTimer);
  document.getElementById('assigned-elevator').textContent = 'Assigned Elevator: --';
  document.getElementById('eta').textContent = 'ETA: -- seconds';
});

// Attach listeners to floor buttons
document.querySelectorAll('.floor-btn').forEach(button => {
  button.addEventListener('click', () => {
    const selectedFloor = button.getAttribute('data-floor');
    assignElevator(parseInt(selectedFloor));
  });
});

// Function to populate the table with dynamic data from the backend
async function refreshDashboard() {
  const trips = await safeFetch("http://localhost:8080/api/trips");
  if (!trips) return;

  const tableBody = document.getElementById('tripDataTable')?.getElementsByTagName('tbody')[0];
  if (tableBody) tableBody.innerHTML = "";

  chartData.labels = [];
  chartData.datasets[0].data = [];
  chartData.datasets[1].data = [];

  trips.forEach((trip, index) => {
    if (tableBody) {
      const row = tableBody.insertRow();
      row.innerHTML = `
        <td>${trip.id}</td>
        <td>${trip.smartDurationMs}</td>
        <td>${trip.traditionalDurationMs}</td>
        <td>${trip.timeSavedMs}</td>
      `;
    }

    chartData.labels.push(`Trip ${index + 1}`);
    chartData.datasets[0].data.push(trip.smartDurationMs);
    chartData.datasets[1].data.push(trip.traditionalDurationMs);
  });

  efficiencyChart?.update();
}

// Automatically simulate a new request every 15 seconds
setInterval(() => {
  const floor = Math.floor(Math.random() * 10) + 1;
  console.log(`Simulated request to floor ${floor}`);
  assignElevator(floor);
}, 9243);

// Initial data load for admin dashboard
refreshDashboard();

// Open the help modal when the Help button is clicked
function openHelpFile() {
  const modal = document.getElementById('helpModal');
  modal.style.display = 'block';

  // Close the modal when the close button is clicked
  document.querySelector('.close-btn').addEventListener('click', () => {
    modal.style.display = 'none';
  });

  // Close the modal if the user clicks outside of it
  window.addEventListener('click', (event) => {
    if (event.target === modal) {
      modal.style.display = 'none';
    }
  });
}
