document.addEventListener("DOMContentLoaded", () =>{
  const data = window.DASHBOARD_DATA || {};
  const revenueData = Array.isArray(data.monthlyRevenue) ? data.monthlyRevenue : [];
  const categoryLabels = Array.isArray(data.categoryLabels) ? data.categoryLabels : [];
  const categoryData = Array.isArray(data.categoryData) ? data.categoryData : [];

  const revCanvas = document.getElementById('revenueChart');
  const catCanvas = document.getElementById('categoryChart');
  if (typeof Chart === 'undefined' || !revCanvas || !catCanvas) {
    console.warn('Dashboard charts not initialized: missing Chart.js or canvas elements');
    return;
  }

  const monthLabels = revenueData.map((_, i) => `Tháng ${i+1}`);

  new Chart(revCanvas, {
    type: 'line',
    data: {
      labels: monthLabels,
      datasets: [{
        label: 'Doanh thu (nghìn đồng)',
        data: revenueData,
        borderColor: '#2563eb',
        backgroundColor: 'rgba(37,99,235,0.1)',
        tension: 0.4,
        fill: true
      }]
    },
    options: {
      responsive: true,
      scales: { y: { beginAtZero: true } }
    }
  });

  new Chart(catCanvas, {
    type: 'doughnut',
    data: {
      labels: categoryLabels,
      datasets: [{
        data: categoryData,
        backgroundColor: ['#60a5fa','#34d399','#facc15','#a78bfa','#f472b6','#f59e0b']
      }]
    },
    options: {
      responsive: true,
      plugins: { legend: { position: 'bottom' } }
    }
  });
});