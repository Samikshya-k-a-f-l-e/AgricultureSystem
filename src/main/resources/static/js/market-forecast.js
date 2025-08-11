document.addEventListener("DOMContentLoaded", () => {
    const predictionForm = document.getElementById("predictionForm")
    const predictionResult = document.getElementById("predictionResult")
    const loadingSpinner = document.getElementById("loadingSpinner")
    const predictBtn = document.getElementById("predictBtn")
    const Swal = window.Swal

    predictionForm.addEventListener("submit", (e) => {
        e.preventDefault()

        const isLoggedIn = document.getElementById("isLoggedIn").value === "true"
        if (!isLoggedIn) {
            Swal.fire({
                icon: "warning",
                title: "Hold on!",
                text: "Please login to predict prices.",
                confirmButtonText: "Login Now",
                confirmButtonColor: "#3085d6",
                footer: '<a href="/login">Click here to login</a>',
            })
            return
        }

        loadingSpinner.style.display = "flex"

        const formValues = {
            Commodity: document.getElementById("cropType").value,
            Year: document.getElementById("predictionYear").value,
            Month: document.getElementById("month").value,
            Location: "Kathmandu,NP"
        }

        const predictionData = {
            Commodity: formatName(formValues.Commodity),
            Year: Number.parseInt(formValues.Year),
            Month: Number.parseInt(formValues.Month),
            Location: formValues.Location,
        }

        console.log("Sending prediction request with current weather:", predictionData)

        const controller = new AbortController()
        const timeoutId = setTimeout(() => controller.abort(), 15000)

        fetch("/api/predict", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(predictionData),
            signal: controller.signal,
        })
            .then(handleResponse)
            .then((data) => displayPredictionResult(data.predictedPrice, predictionData.Commodity, data.message))
            .catch(handleError)
            .finally(() => {
                clearTimeout(timeoutId)
                loadingSpinner.style.display = "none"
            })
    })

    function formatName(str) {
        return str ? str.charAt(0).toUpperCase() + str.slice(1).toLowerCase() : ""
    }

    function handleResponse(response) {
        if (!response.ok) {
            return response.text().then((text) => {
                throw new Error(text || `Request failed with status ${response.status}`)
            })
        }
        return response.json()
    }

    function handleError(error) {
        const message =
            error.name === "AbortError"
                ? "Request timed out. Please try again."
                : error.message || "Failed to get prediction. Please try again."

        showError(message)
        console.error("Prediction error:", error)
    }

    function showError(message) {
        predictionResult.querySelector(".result-container").innerHTML = `
            <div class="error-message">
                <p>${message}</p>
                <p>Please check your inputs and try again.</p>
            </div>
        `
    }

    function displayPredictionResult(price, crop, message) {
        const resultContainer = predictionResult.querySelector(".result-container")
        const numericPrice = typeof price === "string" ? Number.parseFloat(price) : price

        if (isNaN(numericPrice)) {
            resultContainer.innerHTML = `
                <div class="error-message">
                    <h3>Prediction Unavailable</h3>
                    <p>We couldn't generate a prediction for these inputs.</p>
                    <p>Please try different parameters.</p>
                </div>
            `
            return
        }

        resultContainer.innerHTML = `
            <h3>${formatName(crop)} Price Prediction</h3>
            <div class="predicted-price">Rs ${numericPrice.toFixed(2)}/kg</div>
            <div class="price-range">(± Rs ${(numericPrice * 0.05).toFixed(2)})</div>
            <p class="prediction-note">
                ${message || "Based on current weather conditions and historical data."}<br>
                <small>Actual prices may vary based on market conditions.</small>
            </p>
            <div class="prediction-meta">
                <small>Prediction generated on ${new Date().toLocaleDateString()}</small>
            </div>
        `
    }
})
