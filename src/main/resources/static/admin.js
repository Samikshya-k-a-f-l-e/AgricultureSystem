// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
  // User Menu Dropdown Functionality
  const userMenuButton = document.getElementById('userMenuButton');
  const userDropdown = document.getElementById('userDropdown');

  if (userMenuButton && userDropdown) {
    // Toggle dropdown when clicking the button
    userMenuButton.addEventListener('click', function(e) {
      e.preventDefault();
      e.stopPropagation();
      userDropdown.style.display = userDropdown.style.display === 'block' ? 'none' : 'block';
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', function(event) {
      if (!userMenuButton.contains(event.target) && !userDropdown.contains(event.target)) {
        userDropdown.style.display = 'none';
      }
    });
  }

  // Sidebar Toggle Functionality
  const sidebarToggle = document.getElementById('sidebarToggle');
  const adminSidebar = document.getElementById('adminSidebar');

  if (sidebarToggle && adminSidebar) {
    sidebarToggle.addEventListener('click', function() {
      adminSidebar.classList.toggle('collapsed');
    });
  }

  // User Modal Functionality
  const addCategoryBtn = document.getElementById('addNewUser');
  const modal = document.getElementById('addUserModel');
  const closeButtons = document.querySelectorAll('.admin-modal-close');

  if (addCategoryBtn && modal) {
    addCategoryBtn.addEventListener('click', () => {
      modal.style.display = 'block';
    });

    closeButtons.forEach(button => {
      button.addEventListener('click', () => {
        modal.style.display = 'none';
      });
    });

    window.addEventListener('click', (event) => {
      if (event.target === modal) {
        modal.style.display = 'none';
      }
    });
  }

  // Alert Dismissal
  const alertCloseButtons = document.querySelectorAll('.btn-close');
  if (alertCloseButtons) {
    alertCloseButtons.forEach(button => {
      button.addEventListener('click', function() {
        const alert = this.closest('.alert');
        if (alert) {
          alert.remove();
        }
      });
    });
  }
});

// User Management Functions
function deleteUser(id) {
  if (confirm("Are you sure you want to delete this user?")) {
    fetch(`/deleteUser/${id}`, { method: 'DELETE' })
        .then(() => {
          location.reload();
        })
        .catch(err => alert("Delete failed: " + err));
  }
}

function editUser(id) {
  document.querySelector('#addUserModel h3').textContent = 'Update User';
  fetch(`/getUser/${id}`)
      .then(res => res.json())
      .then(data => {
        document.getElementById('name').value = data.name;
        document.getElementById('email').value = data.email;
        document.getElementById('password').value = data.password;
        document.getElementById('role').value = data.role?.toLowerCase();
        document.getElementById('editId').value = data.id;

        const form = document.getElementById('addUserForm');
        form.action = `/editUser/${id}`;

        document.getElementById('addUserModel').style.display = 'block';
      })
      .catch(err => alert("Failed to load user: " + err));
}

// Search Functionality
const searchInput = document.querySelector('.admin-search input');
if (searchInput) {
  searchInput.addEventListener('input', function(e) {
    const searchTerm = e.target.value.toLowerCase();
    const tableRows = document.querySelectorAll('.admin-table tbody tr');

    tableRows.forEach(row => {
      const text = row.textContent.toLowerCase();
      row.style.display = text.includes(searchTerm) ? '' : 'none';
    });
  });
}

// Prevent form submission on enter in search
document.querySelectorAll('.admin-search input').forEach(input => {
  input.addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
      e.preventDefault();
    }
  });
});

// Global chart references
let userActivityChart;
let forumActivityChart;

// Colors for charts
const chartColors = {
  registrations: {
    bg: 'rgba(75, 192, 192, 0.2)',
    border: 'rgba(75, 192, 192, 1)'
  },
  posts: {
    bg: 'rgba(54, 162, 235, 0.2)',
    border: 'rgba(54, 162, 235, 1)'
  },
  views: {
    bg: 'rgba(153, 102, 255, 0.2)',
    border: 'rgba(153, 102, 255, 1)'
  }
};

// Fetch activity data from backend
async function fetchActivityData(range = 'monthly') {
  try {
    const response = await fetch(`/api/user-activity?range=${range}`);
    if (!response.ok) throw new Error('Network response was not ok');
    return await response.json();
  } catch (error) {
    console.error('Error fetching activity data:', error);
    showErrorNotification('Failed to load activity data');
    return null;
  }
}

// Initialize charts when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
  initializeUserActivityChart();
  initializeForumActivityChart();
  setupEventListeners();
});

function initializeUserActivityChart() {
  const ctx = document.getElementById('userActivityChart').getContext('2d');

  userActivityChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: [],
      datasets: [{
        label: 'User Registrations',
        data: [],
        backgroundColor: chartColors.registrations.bg,
        borderColor: chartColors.registrations.border,
        borderWidth: 2,
        tension: 0.4,
        fill: true
      }]
    },
    options: getChartOptions('User Activity')
  });

  loadChartData(userActivityChart, 'monthly');
}

function initializeForumActivityChart() {
  const ctx = document.getElementById('forumActivityChart').getContext('2d');

  forumActivityChart = new Chart(ctx, {
    type: 'line',
    data: {
      labels: [],
      datasets: [{
        label: 'Forum Posts',
        data: [],
        backgroundColor: chartColors.posts.bg,
        borderColor: chartColors.posts.border,
        borderWidth: 2,
        tension: 0.4,
        fill: true
      }]
    },
    options: getChartOptions('Forum Activity')
  });

  loadChartData(forumActivityChart, 'monthly');
}

function getChartOptions(title) {
  return {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      tooltip: {
        callbacks: {
          afterBody: function(context) {
            const label = context[0].label;
            const value = context[0].raw;
            const datasetLabel = context[0].dataset.label;

            if (datasetLabel.includes('Registrations')) {
              return `${value} new user${value !== 1 ? 's' : ''} registered`;
            } else if (datasetLabel.includes('Posts')) {
              return `${value} forum post${value !== 1 ? 's' : ''} created`;
            } else {
              return `${value} guide view${value !== 1 ? 's' : ''}`;
            }
          }
        }
      },
      legend: {
        position: 'top',
      },
      title: {
        display: true,
        text: title
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          precision: 0
        }
      }
    }
  };
}
// Update the loadChartData function to handle the new date format
async function loadChartData(chart, range) {
  const data = await fetchActivityData(range);
  if (!data) return;

  // Format monthly labels differently
  const labels = data.labels.map(label => {
    if (range === 'monthly') {
      // For monthly data, we get "MMM yyyy" from backend
      return label;
    }
    return label; // Keep as-is for daily/weekly
  });

  if (chart === userActivityChart) {
    updateChartData(chart, labels, [
      {
        label: 'User Registrations',
        data: data.registrations,
        backgroundColor: chartColors.registrations.bg,
        borderColor: chartColors.registrations.border
      }
    ]);
  } else if (chart === forumActivityChart) {
    updateChartData(chart, labels, [
      {
        label: 'Forum Posts',
        data: data.posts,
        backgroundColor: chartColors.posts.bg,
        borderColor: chartColors.posts.border
      }
    ]);
  }
}

function updateChartData(chart, labels, datasets) {
  chart.data.labels = labels;
  chart.data.datasets = datasets;
  chart.update();
}

function setupEventListeners() {
  // Time range selectors for user activity
  document.querySelectorAll('#userActivityChart ~ .admin-card-actions .time-range-btn').forEach(btn => {
    btn.addEventListener('click', function() {
      document.querySelectorAll('#userActivityChart ~ .admin-card-actions .time-range-btn').forEach(b => {
        b.classList.remove('active');
      });
      this.classList.add('active');
      loadChartData(userActivityChart, this.dataset.range);
    });
  });

  // Time range selectors for forum activity
  document.querySelectorAll('#forumActivityChart ~ .admin-card-actions .time-range-btn').forEach(btn => {
    btn.addEventListener('click', function() {
      document.querySelectorAll('#forumActivityChart ~ .admin-card-actions .time-range-btn').forEach(b => {
        b.classList.remove('active');
      });
      this.classList.add('active');
      loadChartData(forumActivityChart, this.dataset.range);
    });
  });

  // Refresh buttons
  document.getElementById('refreshChart').addEventListener('click', function() {
    const range = document.querySelector('#userActivityChart ~ .admin-card-actions .time-range-btn.active').dataset.range;
    loadChartData(userActivityChart, range);
  });

  document.getElementById('refreshForumChart').addEventListener('click', function() {
    const range = document.querySelector('#forumActivityChart ~ .admin-card-actions .time-range-btn.active').dataset.range;
    loadChartData(forumActivityChart, range);
  });
}

function showErrorNotification(message) {
  // Implement your notification system here
  console.error(message);
  alert(message); // Simple fallback
}