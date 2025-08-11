document.addEventListener('DOMContentLoaded', function() {
    const predictionForm = document.getElementById('predictionForm');
    const predictionResult = document.getElementById('predictionResult');
    const loadingSpinner = document.getElementById('loadingSpinner');
    const chartContainer = document.getElementById('chartContainer');
    let forecastChart = null;

    predictionForm.addEventListener('submit', function(e) {
        e.preventDefault();

        // Show loading spinner
        loadingSpinner.style.display = 'flex';

        // Get form values
        const cropType = document.getElementById('cropType').value;
        const season = document.getElementById('season').value;
        const region = document.getElementById('region').value;
        const rainfall = document.getElementById('rainfall').value;
        const temperature = document.getElementById('temperature').value;
        const soilType = document.getElementById('soilType').value;

        // Prepare data for API request
        const predictionData = {
            cropType: cropType,
            season: season,
            region: region,
            rainfall: parseFloat(rainfall),
            temperature: parseFloat(temperature),
            soilType: soilType
        };

        // Make API request to Spring Boot backend
        fetch('http://localhost:8080/api/predict/forecast', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(predictionData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // Hide loading spinner
                loadingSpinner.style.display = 'none';

                // Display prediction result
                displayForecastResult(data, cropType);
            })
            .catch(error => {
                // Hide loading spinner
                loadingSpinner.style.display = 'none';

                // Display error message
                predictionResult.querySelector('.result-container').innerHTML = `
                <div class="error-message">
                    <p>Sorry, there was an error processing your request. Please try again later.</p>
                    <p>Error: ${error.message}</p>
                </div>
            `;
                chartContainer.style.display = 'none';
            });
    });

    function displayForecastResult(data, cropType) {
        const resultContainer = predictionResult.querySelector('.result-container');
        const cropName = cropType.charAt(0).toUpperCase() + cropType.slice(1);

        // Get the forecast data
        const forecast = data.forecast;
        const currentPrice = forecast[0].price.toFixed(2);

        // Calculate average price
        const totalPrice = forecast.reduce((sum, month) => sum + month.price, 0);
        const averagePrice = (totalPrice / forecast.length).toFixed(2);

        // Create HTML for the result
        let resultHTML = `
            <h3>3-Month Price Forecast for ${cropName}</h3>
            <div class="predicted-price">₹${averagePrice}/kg <span class="avg-label">(3-month average)</span></div>
            <p>Based on the provided information, our prediction model forecasts the following prices:</p>
            
            <table class="forecast-table">
                <thead>
                    <tr>
                        <th>Month</th>
                        <th>Predicted Price (₹/kg)</th>
                        <th>Trend</th>
                    </tr>
                </thead>
                <tbody>
        `;

        // Add rows for each month
        forecast.forEach((month, index) => {
            const trend = index > 0
                ? month.price > forecast[index-1].price
                    ? '<span class="price-trend trend-up">↑</span>'
                    : month.price < forecast[index-1].price
                        ? '<span class="price-trend trend-down">↓</span>'
                        : '<span class="price-trend trend-stable">→</span>'
                : '';

            resultHTML += `
                <tr>
                    <td>${month.month}</td>
                    <td>₹${month.price.toFixed(2)}</td>
                    <td>${trend}</td>
                </tr>
            `;
        });

        resultHTML += `
                </tbody>
            </table>
            <p class="prediction-note">Note: This forecast is based on historical data and market trends. Actual prices may vary due to unforeseen market conditions.</p>
        `;

        resultContainer.innerHTML = resultHTML;

        // Show chart container
        chartContainer.style.display = 'block';

        // Create chart
        createForecastChart(forecast, cropName);
    }

    function createForecastChart(forecast, cropName) {
        const ctx = document.getElementById('forecastChart').getContext('2d');

        // Destroy previous chart if it exists
        if (forecastChart) {
            forecastChart.destroy();
        }

        // Prepare data for chart
        const labels = forecast.map(item => item.month);
        const prices = forecast.map(item => item.price);

        // Create new chart
        forecastChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: `${cropName} Price (₹/kg)`,
                    data: prices,
                    backgroundColor: 'rgba(76, 175, 80, 0.2)',
                    borderColor: 'rgba(76, 175, 80, 1)',
                    borderWidth: 2,
                    tension: 0.3,
                    pointBackgroundColor: 'rgba(76, 175, 80, 1)',
                    pointRadius: 5,
                    pointHoverRadius: 7
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: false,
                        title: {
                            display: true,
                            text: 'Price (₹/kg)'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: 'Month'
                        }
                    }
                },
                plugins: {
                    title: {
                        display: true,
                        text: `3-Month Price Forecast for ${cropName}`,
                        font: {
                            size: 16
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return `Price: ₹${context.parsed.y.toFixed(2)}/kg`;
                            }
                        }
                    }
                }
            }
        });
    }
});
