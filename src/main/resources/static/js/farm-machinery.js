// Wait for DOM to be fully loaded
document.addEventListener("DOMContentLoaded", () => {
    // Mobile menu toggle
    const mobileMenu = document.getElementById("mobile-menu")
    const navMenu = document.querySelector(".nav-menu")
  
    if (mobileMenu) {
      mobileMenu.addEventListener("click", () => {
        mobileMenu.classList.toggle("active")
        navMenu.classList.toggle("active")
      })
    }
  
    // Filter functionality
    const applyFiltersBtn = document.getElementById("applyFilters")
    const resetFiltersBtn = document.getElementById("resetFilters")
  
    if (applyFiltersBtn) {
      applyFiltersBtn.addEventListener("click", applyFilters)
    }
  
    if (resetFiltersBtn) {
      resetFiltersBtn.addEventListener("click", resetFilters)
    }
  
    // Search functionality
    const searchButton = document.getElementById("searchButton")
    const machinerySearch = document.getElementById("machinerySearch")
  
    if (searchButton && machinerySearch) {
      searchButton.addEventListener("click", () => {
        searchMachinery(machinerySearch.value)
      })
  
      machinerySearch.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
          searchMachinery(machinerySearch.value)
        }
      })
    }
  
    // Smooth scrolling for category links
    document.querySelectorAll(".category-card").forEach((link) => {
      link.addEventListener("click", function (e) {
        e.preventDefault()
  
        const targetId = this.getAttribute("href")
        const targetElement = document.querySelector(targetId)
  
        if (targetElement) {
          window.scrollTo({
            top: targetElement.offsetTop - 100,
            behavior: "smooth",
          })
        }
      })
    })
  
    // Investment calculator
    const calculateBtn = document.getElementById("calculateBtn")
    if (calculateBtn) {
      calculateBtn.addEventListener("click", calculateInvestment)
    }
  
    // Dealer locator
    const findDealersBtn = document.getElementById("findDealersBtn")
    if (findDealersBtn) {
      findDealersBtn.addEventListener("click", findDealers)
    }
  })
  
  // Apply filters to machinery cards
  function applyFilters() {
    const equipmentType = document.getElementById("equipmentType").value
    const priceRange = document.getElementById("priceRange").value
    const manufacturer = document.getElementById("manufacturer").value
    const condition = document.getElementById("condition").value
  
    // Get all machinery cards
    const machineryCards = document.querySelectorAll(".machinery-card")
  
    // Loop through each card and check if it matches the filters
    machineryCards.forEach((card) => {
      const cardType = card.getAttribute("data-type")
      const cardPrice = card.getAttribute("data-price")
      const cardManufacturer = card.getAttribute("data-manufacturer")
      const cardCondition = card.getAttribute("data-condition")
  
      // Check if card matches all selected filters
      const typeMatch = equipmentType === "all" || cardType === equipmentType
      const priceMatch = priceRange === "all" || cardPrice === priceRange
      const manufacturerMatch = manufacturer === "all" || cardManufacturer === manufacturer
      const conditionMatch = condition === "all" || cardCondition === condition
  
      // Show or hide card based on filter matches
      if (typeMatch && priceMatch && manufacturerMatch && conditionMatch) {
        card.style.display = "flex"
      } else {
        card.style.display = "none"
      }
    })
  
    // Scroll to first visible section
    const visibleSection = document.querySelector(".machinery-section:not([style*='display: none'])")
    if (visibleSection) {
      visibleSection.scrollIntoView({ behavior: "smooth", block: "start" })
    }
  }
  
  // Reset all filters
  function resetFilters() {
    // Reset select elements to default values
    document.getElementById("equipmentType").value = "all"
    document.getElementById("priceRange").value = "all"
    document.getElementById("manufacturer").value = "all"
    document.getElementById("condition").value = "all"
  
    // Show all machinery cards
    const machineryCards = document.querySelectorAll(".machinery-card")
    machineryCards.forEach((card) => {
      card.style.display = "flex"
    })
  }
  
  // Search machinery by keyword
  function searchMachinery(keyword) {
    if (!keyword.trim()) {
      alert("Please enter a search term")
      return
    }
  
    keyword = keyword.toLowerCase()
  
    // Get all machinery cards
    const machineryCards = document.querySelectorAll(".machinery-card")
    let matchFound = false
  
    // Loop through each card and check if it contains the keyword
    machineryCards.forEach((card) => {
      const cardText = card.textContent.toLowerCase()
  
      if (cardText.includes(keyword)) {
        card.style.display = "flex"
        matchFound = true
  
        // Highlight the matching text (simplified version)
        highlightText(card, keyword)
      } else {
        card.style.display = "none"
      }
    })
  
    if (!matchFound) {
      alert(`No results found for "${keyword}". Please try a different search term.`)
      resetFilters()
    } else {
      // Scroll to first matching card
      const firstMatch = document.querySelector(".machinery-card[style*='display: flex']")
      if (firstMatch) {
        firstMatch.scrollIntoView({ behavior: "smooth", block: "start" })
      }
    }
  }
  
  // Highlight matching text in search results
  function highlightText(element, keyword) {
    // This is a simplified version - in a real app, you would use a more sophisticated approach
    // that preserves HTML structure and only highlights text nodes
    const headings = element.querySelectorAll("h3")
    headings.forEach((heading) => {
      const text = heading.textContent
      const lowerText = text.toLowerCase()
      const index = lowerText.indexOf(keyword)
  
      if (index >= 0) {
        const before = text.substring(0, index)
        const match = text.substring(index, index + keyword.length)
        const after = text.substring(index + keyword.length)
  
        heading.innerHTML = `${before}<span class="highlight">${match}</span>${after}`
      }
    })
  }
  
  // Calculate machinery investment
  function calculateInvestment() {
    // Get input values
    const equipmentCost = Number.parseFloat(document.getElementById("equipmentCost").value)
    const downPaymentPercent = Number.parseFloat(document.getElementById("downPayment").value)
    const interestRate = Number.parseFloat(document.getElementById("interestRate").value)
    const loanTerm = Number.parseInt(document.getElementById("loanTerm").value)
    const annualUsage = Number.parseInt(document.getElementById("annualUsage").value)
    const fuelCost = Number.parseFloat(document.getElementById("fuelCost").value)
    const maintenancePercent = Number.parseFloat(document.getElementById("maintenanceCost").value)
    const usefulLife = Number.parseInt(document.getElementById("usefulLife").value)
  
    // Calculate loan details
    const downPayment = equipmentCost * (downPaymentPercent / 100)
    const loanAmount = equipmentCost - downPayment
    const monthlyInterestRate = interestRate / 100 / 12
    const numberOfPayments = loanTerm * 12
    const monthlyPayment =
      (loanAmount * (monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))) /
      (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1)
    const totalInterest = monthlyPayment * numberOfPayments - loanAmount
  
    // Calculate operating costs
    const annualMaintenanceCost = equipmentCost * (maintenancePercent / 100)
    const estimatedFuelUsage = annualUsage * 5 // Assuming 5 gallons per hour on average
    const annualFuelCost = estimatedFuelUsage * fuelCost
    const annualOperatingCost = annualMaintenanceCost + annualFuelCost
  
    // Calculate depreciation
    const salvageValue = equipmentCost * 0.2 // Assuming 20% salvage value
    const annualDepreciation = (equipmentCost - salvageValue) / usefulLife
  
    // Calculate total cost of ownership
    const totalCostOfOwnership = equipmentCost + totalInterest + annualOperatingCost * usefulLife
    const costPerHour = totalCostOfOwnership / (annualUsage * usefulLife)
  
    // Format currency
    const formatCurrency = (value) => {
      return new Intl.NumberFormat("en-US", { style: "currency", currency: "USD" }).format(value)
    }
  
    // Display results
    const calculatorResults = document.getElementById("calculatorResults")
    if (calculatorResults) {
      calculatorResults.innerHTML = `
      <div class="result-section">
        <h4>Financing Summary</h4>
        <div class="result-item">
          <span class="result-label">Down Payment:</span>
          <span class="result-value">${formatCurrency(downPayment)}</span>
        </div>
        <div class="result-item">
          <span class="result-label">Loan Amount:</span>
          <span class="result-value">${formatCurrency(loanAmount)}</span>
        </div>
        <div class="result-item">
          <span class="result-label">Monthly Payment:</span>
          <span class="result-value">${formatCurrency(monthlyPayment)}</span>
        </div>
        <div class="result-item">
          <span class="result-label">Total Interest:</span>
          <span class="result-value">${formatCurrency(totalInterest)}</span>
        </div>
      </div>
      
      <div class="result-section">
        <h4>Annual Operating Costs</h4>
        <div class="result-item">
          <span class="result-label">Maintenance:</span>
          <span class="result-value">${formatCurrency(annualMaintenanceCost)}</span>
        </div>
        <div class="result-item">
          <span class="result-label">Fuel (est.):</span>
          <span class="result-value">${formatCurrency(annualFuelCost)}</span>
        </div>
        <div class="result-item">
          <span class="result-label">Total Annual Operating:</span>
          <span class="result-value">${formatCurrency(annualOperatingCost)}</span>
        </div>
      </div>
      
      <div class="result-section">
        <h4>Ownership Analysis</h4>
        <div class="result-item">
          <span class="result-label">Annual Depreciation:</span>
          <span class="result-value">${formatCurrency(annualDepreciation)}</span>
        </div>
        <div class="result-item">
          <span class="result-label">Total Cost of Ownership:</span>
          <span class="result-value">${formatCurrency(totalCostOfOwnership)}</span>
        </div>
        <div class="result-item">
          <span class="result-label">Cost Per Hour:</span>
          <span class="result-value">${formatCurrency(costPerHour)}</span>
        </div>
      </div>
      
      <div class="result-section">
        <h4>Recommendation</h4>
        <p>${getInvestmentRecommendation(costPerHour, annualUsage)}</p>
      </div>
    `
    }
  }
  
  // Generate investment recommendation
  function getInvestmentRecommendation(costPerHour, annualUsage) {
    if (costPerHour < 50) {
      return "Based on your inputs, this equipment appears to be a cost-effective investment with a reasonable cost per hour. The total cost of ownership is well-balanced with the expected usage."
    } else if (costPerHour < 100) {
      return "This equipment has a moderate cost per hour. Consider increasing annual usage to improve cost efficiency, or explore financing options with lower interest rates."
    } else {
      if (annualUsage < 300) {
        return "The cost per hour is relatively high, primarily due to low annual usage. Consider equipment rental or custom hiring for this operation unless you expect usage to increase significantly."
      } else {
        return "The cost per hour is high despite reasonable usage. Explore less expensive equipment alternatives or consider used equipment options to reduce your initial investment."
      }
    }
  }
  
  // Find equipment dealers
  function findDealers() {
    const zipCode = document.getElementById("dealerZip").value
    const radius = document.getElementById("dealerRadius").value
    const brand = document.getElementById("dealerBrand").value
  
    if (!zipCode) {
      alert("Please enter a ZIP code or city")
      return
    }
  
    // In a real app, this would make an API call to find dealers
    // For this demo, we'll just show an alert
    alert(`Searching for ${brand === "all" ? "all brands" : brand} dealers within ${radius} miles of ${zipCode}...`)
  
    // Simulate loading a map with dealer locations
    const dealerMap = document.getElementById("dealerMap")
    if (dealerMap) {
      dealerMap.innerHTML = `
      <div style="background-color: #f5f5f5; height: 100%; display: flex; align-items: center; justify-content: center; text-align: center; padding: 20px;">
        <div>
          <i class="fas fa-map-marker-alt" style="font-size: 48px; color: var(--machinery-primary); margin-bottom: 20px;"></i>
          <h3>Dealer Locations</h3>
          <p>5 dealers found within ${radius} miles of ${zipCode}</p>
          <p style="font-style: italic; margin-top: 20px; color: #666;">In a real application, this would display an interactive map with dealer locations.</p>
        </div>
      </div>
    `
    }
  }
  
  