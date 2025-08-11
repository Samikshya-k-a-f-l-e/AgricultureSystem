const apiKey = "your_api_key";
const apiUrl = "https://api.openweathermap.org/data/2.5/weather?units=metric&q=";
const forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?units=metric&q=";
document.addEventListener("DOMContentLoaded", () => {
  // Mobile menu toggle
  const mobileMenu = document.getElementById("mobile-menu");
  const navMenu = document.querySelector(".nav-menu");

  if (mobileMenu) {
    mobileMenu.addEventListener("click", () => {
      mobileMenu.classList.toggle("active");
      navMenu.classList.toggle("active");
    });
  }

  // Location search functionality
  const locationSearch = document.getElementById("locationSearch");
  const searchButton = document.getElementById("searchButton");
  const useCurrentLocationButton = document.getElementById("useCurrentLocation");

  if (searchButton && locationSearch) {
    searchButton.addEventListener("click", () => {
      const location = locationSearch.value.trim();
      if (location) {
        loadWeatherData(location);
      } else {
        alert("Please enter a location");
      }
    });

    locationSearch.addEventListener("keypress", (e) => {
      if (e.key === "Enter") {
        const location = locationSearch.value.trim();
        if (location) {
          loadWeatherData(location);
        } else {
          alert("Please enter a location");
        }
      }
    });
  }

  if (useCurrentLocationButton) {
    useCurrentLocationButton.addEventListener("click", () => {
      getCurrentLocationWeather();
    });
  }

  // Load current location weather by default
  getCurrentLocationWeather();
});

// Function to get weather for current location
function getCurrentLocationWeather() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
        (position) => {
          const lat = position.coords.latitude;
          const lon = position.coords.longitude;
          loadWeatherByCoords(lat, lon);
        },
        (error) => {
          console.error("Error getting location:", error);
          // Fallback to default location if geolocation fails
          loadWeatherData("bhaktapur");
        }
    );
  } else {
    // Geolocation not supported, fallback to default location
    loadWeatherData("bhaktapur");
  }
}  // <-- Remove the "});" that was here and just keep this closing brace

// Load weather data by city name
async function loadWeatherData(location) {
  try {
    // Show loading state
    showLoadingState(location);

    // Fetch current weather
    const currentResponse = await fetch(`${apiUrl}${location}&appid=${apiKey}`);
    const currentData = await currentResponse.json();

    if (currentData.cod !== 200) {
      throw new Error(currentData.message || "Location not found");
    }

    // Fetch forecast
    const forecastResponse = await fetch(`${forecastUrl}${location}&appid=${apiKey}`);
    const forecastData = await forecastResponse.json();

    // Update UI with weather data
    updateCurrentWeatherUI(location, currentData);
    updateForecastUI(forecastData);
    loadAlerts(); // You can modify this to show real alerts if available

  } catch (error) {
    showErrorState(error.message);
    console.error("Error fetching weather data:", error);
  }
}

// Load weather data by coordinates
async function loadWeatherByCoords(lat, lon) {
  try {
    // Show loading state
    showLoadingState("your location");

    // Fetch current weather by coordinates
    const currentResponse = await fetch(`https://api.openweathermap.org/data/2.5/weather?units=metric&lat=${lat}&lon=${lon}&appid=${apiKey}`);
    const currentData = await currentResponse.json();

    // Get location name from reverse geocoding
    const locationName = await reverseGeocode(lat, lon);

    // Fetch forecast
    const forecastResponse = await fetch(`https://api.openweathermap.org/data/2.5/forecast?units=metric&lat=${lat}&lon=${lon}&appid=${apiKey}`);
    const forecastData = await forecastResponse.json();

    // Update UI
    updateCurrentWeatherUI(locationName || "Your Location", currentData);
    updateForecastUI(forecastData);
    loadAlerts();

  } catch (error) {
    showErrorState(error.message);
    console.error("Error fetching weather data:", error);
  }
}

// Reverse geocode coordinates to get location name
async function reverseGeocode(lat, lon) {
  try {
    const response = await fetch(`https://api.openweathermap.org/geo/1.0/reverse?lat=${lat}&lon=${lon}&limit=1&appid=${apiKey}`);
    const data = await response.json();
    if (data && data.length > 0) {
      return data[0].name + ", " + data[0].country;
    }
    return null;
  } catch (error) {
    console.error("Error in reverse geocoding:", error);
    return null;
  }
}

// Show loading state
function showLoadingState(location) {
  const currentWeatherCard = document.getElementById("currentWeatherCard");
  if (currentWeatherCard) {
    currentWeatherCard.innerHTML = `
      <div class="weather-loading">
        <i class="fas fa-spinner fa-pulse"></i>
        <p>Loading weather data for ${location}...</p>
      </div>
    `;
  }
}

// Show error state
function showErrorState(message) {
  const currentWeatherCard = document.getElementById("currentWeatherCard");
  if (currentWeatherCard) {
    currentWeatherCard.innerHTML = `
      <div class="weather-error">
        <i class="fas fa-exclamation-triangle"></i>
        <p>Error loading weather data: ${message}</p>
      </div>
    `;
  }
}

// Update current weather UI
function updateCurrentWeatherUI(location, data) {
  const currentWeatherCard = document.getElementById("currentWeatherCard");
  if (!currentWeatherCard) return;

  const weather = {
    temperature: Math.round(data.main.temp),
    feelsLike: Math.round(data.main.feels_like),
    description: data.weather[0].description,
    icon: getWeatherIcon(data.weather[0].main),
    humidity: data.main.humidity,
    windSpeed: Math.round(data.wind.speed * 3.6), // Convert m/s to km/h
    windDirection: getWindDirection(data.wind.deg),
    pressure: data.main.pressure,
    visibility: (data.visibility / 1000).toFixed(1), // Convert meters to km
    lastUpdate: new Date()
  };

  // Format last update time
  const formattedTime = weather.lastUpdate.toLocaleTimeString("en-US", {
    hour: "numeric",
    minute: "numeric",
    hour12: true
  });

  // Set current weather HTML
  currentWeatherCard.innerHTML = `
    <div class="current-weather-main">
      <div class="current-weather-icon">
        <i class="fas fa-${weather.icon}"></i>
      </div>
      <div class="current-weather-info">
        <h2>${weather.temperature}°C <span>Feels like ${weather.feelsLike}°C</span></h2>
        <div class="current-weather-location">
          <i class="fas fa-map-marker-alt"></i>
          <span>${location}</span>
        </div>
        <div class="current-weather-description">${weather.description}</div>
        <div class="current-weather-update">Last updated: ${formattedTime}</div>
      </div>
    </div>
    <div class="current-weather-details">
      <div class="weather-detail-item">
        <div class="weather-detail-icon">
          <i class="fas fa-tint"></i>
        </div>
        <div class="weather-detail-value">${weather.humidity}%</div>
        <div class="weather-detail-label">Humidity</div>
      </div>
      <div class="weather-detail-item">
        <div class="weather-detail-icon">
          <i class="fas fa-wind"></i>
        </div>
        <div class="weather-detail-value">${weather.windSpeed} km/h</div>
        <div class="weather-detail-label">Wind (${weather.windDirection})</div>
      </div>
      <div class="weather-detail-item">
        <div class="weather-detail-icon">
          <i class="fas fa-compress-alt"></i>
        </div>
        <div class="weather-detail-value">${weather.pressure} hPa</div>
        <div class="weather-detail-label">Pressure</div>
      </div>
      <div class="weather-detail-item">
        <div class="weather-detail-icon">
          <i class="fas fa-eye"></i>
        </div>
        <div class="weather-detail-value">${weather.visibility} km</div>
        <div class="weather-detail-label">Visibility</div>
      </div>
    </div>
  `;
}

// Update forecast UI
function updateForecastUI(forecastData) {
  const forecastContainer = document.getElementById("forecastContainer");
  if (!forecastContainer) return;

  // Process forecast data - group by day
  const dailyForecasts = {};
  forecastData.list.forEach(item => {
    const date = new Date(item.dt * 1000);
    const dateStr = date.toLocaleDateString();

    if (!dailyForecasts[dateStr]) {
      dailyForecasts[dateStr] = {
        date: date,
        temps: [],
        weather: item.weather[0],
        humidity: item.main.humidity,
        windSpeed: item.wind.speed * 3.6, // Convert to km/h
        precipitation: item.pop ? Math.round(item.pop * 100) : 0 // Probability of precipitation
      };
    }

    dailyForecasts[dateStr].temps.push(item.main.temp);
  });

  // Get the next 5 days
  const forecastDays = Object.values(dailyForecasts).slice(0, 5);

  // Clear container
  forecastContainer.innerHTML = "";

  // Create forecast cards
  forecastDays.forEach((day, index) => {
    const highTemp = Math.round(Math.max(...day.temps));
    const lowTemp = Math.round(Math.min(...day.temps));
    const dayName = index === 0 ? "Today" : day.date.toLocaleDateString("en-US", { weekday: "long" });
    const dateStr = day.date.toLocaleDateString("en-US", { month: "short", day: "numeric" });

    const forecastCard = document.createElement("div");
    forecastCard.className = "forecast-card";
    forecastCard.innerHTML = `
      <div class="forecast-day">${dayName}</div>
      <div class="forecast-date">${dateStr}</div>
      <div class="forecast-icon">
        <i class="fas fa-${getWeatherIcon(day.weather.main)}"></i>
      </div>
      <div class="forecast-temp">
        <div class="forecast-high">${highTemp}°</div>
        <div class="forecast-low">${lowTemp}°</div>
      </div>
      <div class="forecast-description">${day.weather.description}</div>
      <div class="forecast-details">
        <div class="forecast-detail">
          <span>Precipitation:</span>
          <span>${day.precipitation}%</span>
        </div>
        <div class="forecast-detail">
          <span>Humidity:</span>
          <span>${day.humidity}%</span>
        </div>
        <div class="forecast-detail">
          <span>Wind:</span>
          <span>${Math.round(day.windSpeed)} km/h</span>
        </div>
      </div>
    `;
    forecastContainer.appendChild(forecastCard);
  });

  // Prepare daily summary for impact analysis
  const impactForecastData = forecastDays.map(day => ({
    temp: { day: Math.round((Math.max(...day.temps) + Math.min(...day.temps)) / 2) },
    humidity: day.humidity
  }));

  // Call the impact analysis function
  renderImpactAnalysis(impactForecastData);

}

// Helper function to map OpenWeatherMap icons to Font Awesome icons
function getWeatherIcon(weatherMain) {
  const iconMap = {
    "Clear": "sun",
    "Clouds": "cloud",
    "Rain": "cloud-rain",
    "Drizzle": "cloud-rain",
    "Thunderstorm": "bolt",
    "Snow": "snowflake",
    "Mist": "smog",
    "Smoke": "smog",
    "Haze": "smog",
    "Dust": "smog",
    "Fog": "smog",
    "Sand": "smog",
    "Ash": "smog",
    "Squall": "wind",
    "Tornado": "wind"
  };
  return iconMap[weatherMain] || "cloud";
}

// Helper function to convert wind degrees to direction
function getWindDirection(degrees) {
  const directions = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
  const index = Math.round((degrees % 360) / 45);
  return directions[index % 8];
}

// Load alerts (sample implementation)
function loadAlerts() {
  const alertsContainer = document.getElementById("alertsContainer");
  if (!alertsContainer) return;

  // In a real app, you would fetch actual alerts from an API
  alertsContainer.innerHTML = `
    <div class="no-alerts">
      <i class="fas fa-check-circle"></i>
      <p>No active weather alerts for your area.</p>
    </div>
  `;
}

const tabs = document.querySelectorAll(".impact-tab");
tabs.forEach(tab => {
  tab.addEventListener("click", () => {
    // Remove active class from all tabs
    tabs.forEach(t => t.classList.remove("active"));
    tab.classList.add("active");

    const selectedCrop = tab.getAttribute("data-crop");
    const impactCards = document.querySelectorAll(".impact-card");

    impactCards.forEach(card => {
      const crops = card.getAttribute("data-crops").split(",");
      if (selectedCrop === "all" || crops.includes(selectedCrop)) {
        card.style.display = "flex";
      } else {
        card.style.display = "none";
      }
    });
  });
});

function renderImpactAnalysis(forecastData) {
  const impactContainer = document.getElementById("impactContainer");
  impactContainer.innerHTML = "";

  // Define thresholds for each crop
  const cropThresholds = {
    maize: { highTemp: 15, lowTemp: 10, highHumidity: 80 },
    wheat: { highTemp: 30, lowTemp: 5, highHumidity: 85 },
    potato: { highTemp: 28, lowTemp: 7, highHumidity: 90 },
    tomato: { highTemp: 32, lowTemp: 12, highHumidity: 75 },
    onion: { highTemp: 35, lowTemp: 10, highHumidity: 80 },
    all: { highTemp: 35, lowTemp: 5, highHumidity: 85 }
  };

  // Get selected crop from active tab
  const activeTab = document.querySelector(".impact-tab.active");
  const selectedCrop = activeTab?.getAttribute("data-crop") || "all";
  const { highTemp, lowTemp, highHumidity } = cropThresholds[selectedCrop] || cropThresholds["all"];

  let showImpact = false;

  for (const day of forecastData) {
    const temp = day.temp?.day || 0;
    const humidity = day.humidity || 0;

    if (temp > highTemp || temp < lowTemp || humidity > highHumidity) {
      showImpact = true;
      break;
    }
  }

  if (showImpact) {
    // Show only cards related to selected crop
    document.querySelectorAll(".impact-card").forEach(card => {
      const crops = card.getAttribute("data-crops").split(",");
      if (selectedCrop === "all" || crops.includes(selectedCrop)) {
        card.style.display = "flex";
        impactContainer.appendChild(card);
      }
    });
  } else {
    const safeMessage = document.createElement("div");
    safeMessage.className = "impact-safe-message";
    safeMessage.innerHTML = `
      <div class="safe-icon"><i class="fas fa-check-circle"></i></div>
      <div class="safe-text">Weather is favorable for <strong>${selectedCrop === "all" ? "all crops" : selectedCrop}</strong>. No significant issues expected.</div>
    `;
    impactContainer.appendChild(safeMessage);
  }
}

