import { Chart } from "@/components/ui/chart"
// Wait for DOM to be fully loaded
document.addEventListener("DOMContentLoaded", () => {
  // Mobile menu toggle
  const mobileMenu = document.getElementById("mobile-menu")
  const navMenu = document.querySelector(".nav-menu")

  mobileMenu.addEventListener("click", () => {
    mobileMenu.classList.toggle("active")
    navMenu.classList.toggle("active")
  })

  // Smooth scrolling for navigation links
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault()

      // Close mobile menu if open
      if (navMenu.classList.contains("active")) {
        navMenu.classList.remove("active")
        mobileMenu.classList.remove("active")
      }

      const targetId = this.getAttribute("href")
      const targetElement = document.querySelector(targetId)

      if (targetElement) {
        window.scrollTo({
          top: targetElement.offsetTop - 70, // Adjust for fixed header
          behavior: "smooth",
        })

        // Update active link
        document.querySelectorAll(".nav-menu a").forEach((link) => {
          link.classList.remove("active")
        })
        this.classList.add("active")
      }
    })
  })

  // Initialize charts
  initCharts()

  // Load crop data
  loadCropData()

  // Load weather data
  loadWeatherData()

  // Load activity feed
  loadActivityFeed()

  // Modal functionality
  const modal = document.getElementById("cropModal")
  const addCropBtn = document.getElementById("addCropBtn")
  const closeModal = document.querySelector(".close-modal")

  addCropBtn.addEventListener("click", () => {
    modal.style.display = "flex"
  })

  closeModal.addEventListener("click", () => {
    modal.style.display = "none"
  })

  window.addEventListener("click", (e) => {
    if (e.target === modal) {
      modal.style.display = "none"
    }
  })

  // Form submissions
  const addCropForm = document.getElementById("addCropForm")
  const contactForm = document.getElementById("contactForm")

  addCropForm.addEventListener("submit", (e) => {
    e.preventDefault()

    // Get form data
    const cropName = document.getElementById("cropName").value
    const cropType = document.getElementById("cropType").value
    const plantingDate = document.getElementById("plantingDate").value
    const harvestDate = document.getElementById("harvestDate").value
    const fieldLocation = document.getElementById("fieldLocation").value
    const cropNotes = document.getElementById("cropNotes").value

    // Create new crop card
    const newCrop = {
      id: Date.now(),
      name: cropName,
      type: cropType,
      plantingDate: new Date(plantingDate),
      harvestDate: new Date(harvestDate),
      location: fieldLocation,
      notes: cropNotes,
      progress: 0, // Initial progress
    }

    // Add to crops array
    crops.push(newCrop)

    // Update crop cards
    renderCropCards(crops)

    // Close modal and reset form
    modal.style.display = "none"
    addCropForm.reset()

    // Add to activity feed
    addActivity(`Added new crop: ${cropName}`)
  })

  contactForm.addEventListener("submit", (e) => {
    e.preventDefault()

    // Get form data
    const name = document.getElementById("name").value
    const email = document.getElementById("email").value
    const message = document.getElementById("message").value

    // Simulate form submission
    alert(`Thank you, ${name}! Your message has been sent. We'll get back to you at ${email} soon.`)

    // Reset form
    contactForm.reset()
  })

  // Filter functionality for crops
  const filterButtons = document.querySelectorAll(".filter-btn")

  filterButtons.forEach((button) => {
    button.addEventListener("click", function () {
      const filter = this.getAttribute("data-filter")

      // Update active button
      filterButtons.forEach((btn) => btn.classList.remove("active"))
      this.classList.add("active")

      // Filter crops
      if (filter === "all") {
        renderCropCards(crops)
      } else {
        const filteredCrops = crops.filter((crop) => crop.type === filter)
        renderCropCards(filteredCrops)
      }
    })
  })
})

// Sample data
let crops = []
let activities = []

// Initialize charts
function initCharts() {
  // Crop Health Chart
  const cropHealthCtx = document.getElementById("cropHealthChart").getContext("2d")
  const cropHealthChart = new Chart(cropHealthCtx, {
    type: "doughnut",
    data: {
      labels: ["Healthy", "Moderate", "At Risk"],
      datasets: [
        {
          data: [70, 20, 10],
          backgroundColor: ["#4CAF50", "#FFC107", "#F44336"],
          borderWidth: 0,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: "bottom",
        },
      },
    },
  })

  // Yield Forecast Chart
  const yieldForecastCtx = document.getElementById("yieldForecastChart").getContext("2d")
  const yieldForecastChart = new Chart(yieldForecastCtx, {
    type: "bar",
    data: {
      labels: ["Corn", "Wheat", "Soybeans", "Rice"],
      datasets: [
        {
          label: "Expected Yield (tons)",
          data: [120, 80, 60, 40],
          backgroundColor: "#4CAF50",
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false,
        },
      },
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  })

  // Resource Usage Chart
  const resourceUsageCtx = document.getElementById("resourceUsageChart").getContext("2d")
  const resourceUsageChart = new Chart(resourceUsageCtx, {
    type: "line",
    data: {
      labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun"],
      datasets: [
        {
          label: "Water (kL)",
          data: [500, 600, 700, 800, 750, 650],
          borderColor: "#2196F3",
          backgroundColor: "rgba(33, 150, 243, 0.1)",
          fill: true,
        },
        {
          label: "Fertilizer (kg)",
          data: [300, 400, 350, 500, 450, 400],
          borderColor: "#4CAF50",
          backgroundColor: "rgba(76, 175, 80, 0.1)",
          fill: true,
        },
      ],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: "bottom",
        },
      },
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  })
}

// Load crop data
function loadCropData() {
  // Sample crop data
  crops = [
    {
      id: 1,
      name: "Corn",
      type: "grains",
      plantingDate: new Date("2025-03-15"),
      harvestDate: new Date("2025-08-20"),
      location: "Field A",
      notes: "Hybrid variety, drought resistant",
      progress: 35,
    },
    {
      id: 2,
      name: "Tomatoes",
      type: "vegetables",
      plantingDate: new Date("2025-04-01"),
      harvestDate: new Date("2025-07-15"),
      location: "Greenhouse 2",
      notes: "Cherry variety, organic",
      progress: 20,
    },
    {
      id: 3,
      name: "Wheat",
      type: "grains",
      plantingDate: new Date("2025-02-10"),
      harvestDate: new Date("2025-07-01"),
      location: "Field B",
      notes: "Winter wheat",
      progress: 45,
    },
    {
      id: 4,
      name: "Apples",
      type: "fruits",
      plantingDate: new Date("2024-03-20"),
      harvestDate: new Date("2025-09-15"),
      location: "Orchard C",
      notes: "Gala variety",
      progress: 15,
    },
    {
      id: 5,
      name: "Carrots",
      type: "vegetables",
      plantingDate: new Date("2025-03-25"),
      harvestDate: new Date("2025-06-10"),
      location: "Field D",
      notes: "Organic, no pesticides",
      progress: 30,
    },
    {
      id: 6,
      name: "Strawberries",
      type: "fruits",
      plantingDate: new Date("2025-04-05"),
      harvestDate: new Date("2025-06-20"),
      location: "Greenhouse 1",
      notes: "Hydroponic system",
      progress: 10,
    },
  ]

  // Render crop cards
  renderCropCards(crops)
}

// Render crop cards
function renderCropCards(cropsToRender) {
  const cropCardsContainer = document.getElementById("cropCards")
  cropCardsContainer.innerHTML = ""

  if (cropsToRender.length === 0) {
    cropCardsContainer.innerHTML = '<p class="no-crops">No crops found. Add a new crop to get started.</p>'
    return
  }

  cropsToRender.forEach((crop) => {
    // Calculate days remaining
    const today = new Date()
    const daysRemaining = Math.ceil((crop.harvestDate - today) / (1000 * 60 * 60 * 24))

    // Create crop card
    const cropCard = document.createElement("div")
    cropCard.className = "crop-card"
    cropCard.innerHTML = `
            <div class="crop-image" style="background-image: url('/placeholder.svg?height=180&width=280')"></div>
            <div class="crop-details">
                <h3>${crop.name}</h3>
                <span class="crop-type">${crop.type.charAt(0).toUpperCase() + crop.type.slice(1)}</span>
                <div class="crop-info">
                    <i class="fas fa-map-marker-alt"></i>
                    <span>${crop.location}</span>
                </div>
                <div class="crop-info">
                    <i class="fas fa-calendar-alt"></i>
                    <span>Planted: ${formatDate(crop.plantingDate)}</span>
                </div>
                <div class="crop-info">
                    <i class="fas fa-calendar-check"></i>
                    <span>Harvest: ${formatDate(crop.harvestDate)}</span>
                </div>
                <div class="crop-info">
                    <i class="fas fa-clock"></i>
                    <span>${daysRemaining} days remaining</span>
                </div>
                <div class="crop-progress">
                    <div class="progress-label">
                        <span>Growth Progress</span>
                        <span>${crop.progress}%</span>
                    </div>
                    <div class="progress-bar">
                        <div class="progress-fill" style="width: ${crop.progress}%"></div>
                    </div>
                </div>
            </div>
        `

    cropCardsContainer.appendChild(cropCard)
  })
}

// Load weather data
function loadWeatherData() {
  // Current weather
  const currentWeatherContainer = document.getElementById("currentWeather")
  currentWeatherContainer.innerHTML = `
        <div class="weather-main">
            <div class="weather-icon">
                <i class="fas fa-sun"></i>
            </div>
            <div class="weather-info">
                <h3>28°C</h3>
                <div class="weather-location">
                    <i class="fas fa-map-marker-alt"></i>
                    <span>Your Farm Location</span>
                </div>
                <p>Clear Sky</p>
            </div>
        </div>
        <div class="weather-details">
            <div class="weather-detail">
                <i class="fas fa-wind"></i>
                <span>Wind: 5 km/h</span>
            </div>
            <div class="weather-detail">
                <i class="fas fa-tint"></i>
                <span>Humidity: 65%</span>
            </div>
            <div class="weather-detail">
                <i class="fas fa-temperature-high"></i>
                <span>High: 30°C</span>
            </div>
            <div class="weather-detail">
                <i class="fas fa-temperature-low"></i>
                <span>Low: 22°C</span>
            </div>
        </div>
    `

  // Weather forecast
  const forecastContainer = document.getElementById("weatherForecast")

  // Sample forecast data
  const forecast = [
    { date: "Tomorrow", icon: "sun", temp: "29°C", desc: "Sunny" },
    { date: "Wed", icon: "cloud-sun", temp: "27°C", desc: "Partly Cloudy" },
    { date: "Thu", icon: "cloud", temp: "25°C", desc: "Cloudy" },
    { date: "Fri", icon: "cloud-rain", temp: "23°C", desc: "Light Rain" },
    { date: "Sat", icon: "sun", temp: "26°C", desc: "Sunny" },
  ]

  forecast.forEach((day) => {
    const forecastDay = document.createElement("div")
    forecastDay.className = "forecast-day"
    forecastDay.innerHTML = `
            <div class="forecast-date">${day.date}</div>
            <div class="forecast-icon">
                <i class="fas fa-${day.icon}"></i>
            </div>
            <div class="forecast-temp">${day.temp}</div>
            <div class="forecast-desc">${day.desc}</div>
        `

    forecastContainer.appendChild(forecastDay)
  })
}

// Load activity feed
function loadActivityFeed() {
  // Sample activities
  activities = [
    { text: "Irrigation system activated in Field A", time: new Date(Date.now() - 30 * 60000) },
    { text: "Fertilizer applied to Tomatoes in Greenhouse 2", time: new Date(Date.now() - 2 * 3600000) },
    { text: "Pest detection alert in Orchard C", time: new Date(Date.now() - 5 * 3600000) },
    { text: "Harvested 500kg of Carrots from Field D", time: new Date(Date.now() - 24 * 3600000) },
    { text: "Weather alert: Heavy rain expected tomorrow", time: new Date(Date.now() - 26 * 3600000) },
  ]

  renderActivityFeed()
}

// Add new activity
function addActivity(text) {
  activities.unshift({
    text: text,
    time: new Date(),
  })

  renderActivityFeed()
}

// Render activity feed
function renderActivityFeed() {
  const activityFeedContainer = document.getElementById("activityFeed")
  activityFeedContainer.innerHTML = ""

  activities.forEach((activity) => {
    const activityItem = document.createElement("div")
    activityItem.className = "activity-item"
    activityItem.innerHTML = `
            <p>${activity.text}</p>
            <span class="activity-time">${formatTime(activity.time)}</span>
        `

    activityFeedContainer.appendChild(activityItem)
  })
}

// Helper function to format date
function formatDate(date) {
  const options = { month: "short", day: "numeric", year: "numeric" }
  return date.toLocaleDateString("en-US", options)
}

// Helper function to format time
function formatTime(date) {
  const now = new Date()
  const diffMs = now - date
  const diffMins = Math.floor(diffMs / 60000)
  const diffHours = Math.floor(diffMins / 60)
  const diffDays = Math.floor(diffHours / 24)

  if (diffMins < 1) {
    return "Just now"
  } else if (diffMins < 60) {
    return `${diffMins} minute${diffMins > 1 ? "s" : ""} ago`
  } else if (diffHours < 24) {
    return `${diffHours} hour${diffHours > 1 ? "s" : ""} ago`
  } else {
    return `${diffDays} day${diffDays > 1 ? "s" : ""} ago`
  }
}

