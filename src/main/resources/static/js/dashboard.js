document.addEventListener("DOMContentLoaded", () =>{
  const data = window.DASHBOARD_DATA || {};
  const revenueData = Array.isArray(data.monthlyRevenue) ? data.monthlyRevenue : [];
  const categoryLabels = Array.isArray(data.categoryLabels) ? data.categoryLabels : [];
  const categoryData = Array.isArray(data.categoryData) ? data.categoryData : [];

  const revCanvas = document.getElementById('revenueChart');
  const catCanvas = document.getElementById('categoryChart');
  if (typeof Chart === 'undefined' || !revCanvas || !catCanvas) {
    return;
  }

  const monthLabels = revenueData.map((_, i) => `Tháng ${i+1}`);

  const revenueInMillions = revenueData.map(value => Math.round((value / 1_000_000) * 100) / 100);

  new Chart(revCanvas, {
    type: 'line',
    data: {
      labels: monthLabels,
      datasets: [{
        label: 'Doanh thu (triệu đồng)',
        data: revenueInMillions,
        borderColor: '#2563eb',
        backgroundColor: 'rgba(37,99,235,0.1)',
        tension: 0.4,
        fill: true
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: function(value) {
              return value.toLocaleString('vi-VN') + ' tr';
            }
          }
        }
      },
      plugins: {
        tooltip: {
          callbacks: {
            label: function(context) {
              return context.dataset.label + ': ' +
                     context.parsed.y.toLocaleString('vi-VN') + ' triệu đồng';
            }
          }
        }
      }
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