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

  // Smooth scrolling for navigation links
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault();

      // Close mobile menu if open
      if (navMenu.classList.contains("active")) {
        navMenu.classList.remove("active");
        mobileMenu.classList.remove("active");
      }

      const targetId = this.getAttribute("href");
      const targetElement = document.querySelector(targetId);

      if (targetElement) {
        window.scrollTo({
          top: targetElement.offsetTop - 70,
          behavior: "smooth",
        });
      }
    });
  });

  // Search functionality
  const searchInput = document.getElementById("guidesSearch");
  const searchButton = document.getElementById("searchButton");

  if (searchButton && searchInput) {
    searchButton.addEventListener("click", () => {
      performSearch(searchInput.value);
    });

    searchInput.addEventListener("keypress", (e) => {
      if (e.key === "Enter") {
        performSearch(searchInput.value);
      }
    });
  }

  // Filter functionality for crop guides
  const cropFilterButtons = document.querySelectorAll(".filter-btn[data-crop]");

  if (cropFilterButtons.length > 0) {
    cropFilterButtons.forEach((button) => {
      button.addEventListener("click", function() {
        const crop = this.getAttribute("data-crop");

        // Update active button
        document.querySelectorAll(".filter-btn").forEach(btn => {
          btn.classList.remove("active");
        });
        this.classList.add("active");

        // Get the parent section and its guides grid
        const section = this.closest(".guides-section");
        const guidesGrid = section.querySelector(".guides-grid");
        const guides = guidesGrid.querySelectorAll(".guide-card");

        // Filter guides
        guides.forEach((guide) => {
          if (crop === "all") {
            guide.style.display = "block";
          } else {
            const guideSubcategory = guide.getAttribute("data-subcategory");
            guide.style.display = guideSubcategory === crop ? "block" : "none";
          }
        });
      });
    });
  }

  // Newsletter form submission
  const newsletterForm = document.getElementById("newsletterForm");

  if (newsletterForm) {
    newsletterForm.addEventListener("submit", function (e) {
      e.preventDefault();

      // Get email
      const email = this.querySelector('input[type="email"]').value;

      // Simulate form submission
      alert(`Thank you for subscribing with ${email}! You'll now receive our latest farming guides and tips.`);

      // Reset form
      this.reset();
    });
  }
});

function performSearch(query) {
  if (!query.trim()) {
    alert("Please enter a search term");
    return;
  }

  // In a real application, this would search the database
  // For this demo, we'll just show an alert
  alert(`Searching for: ${query}`);

  // You could redirect to a search results page
  // window.location.href = `search-results.html?q=${encodeURIComponent(query)}`;
}