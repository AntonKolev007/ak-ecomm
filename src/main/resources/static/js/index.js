document.addEventListener("DOMContentLoaded", function () {
    // Fetch and display the featured products
    function fetchFeaturedProducts() {
        fetch('/api/public/products/featured', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const featuredProductsContainer = document.querySelector('.featured-products');
                featuredProductsContainer.innerHTML = ''; // Clear existing content

                data.forEach(product => {
                    const productDiv = document.createElement('div');
                    productDiv.className = 'product';
                    productDiv.innerHTML = `
                    <img src="/images/${product.image}" alt="Product Image">
                    <h3>${product.productName}</h3>
                    <p>${product.description}</p>
                    <a href="/products/${product.id}" class="btn">View Product</a>
                `;
                    featuredProductsContainer.appendChild(productDiv);
                });
            })
            .catch(error => {
                console.error('Error fetching featured products:', error);
            });
    }

    // Fetch and display the testimonials as a slideshow
    function fetchTestimonials() {
        fetch('/api/testimonials', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const testimonialsContainer = document.querySelector('.testimonials');
                testimonialsContainer.innerHTML = ''; // Clear existing content

                data.forEach((testimonial, index) => {
                    const testimonialDiv = document.createElement('div');
                    testimonialDiv.className = 'testimonial';
                    if (index === 0) {
                        testimonialDiv.classList.add('active');
                    }
                    testimonialDiv.innerHTML = `
                    <blockquote>${testimonial.text}</blockquote>
                    <cite>${testimonial.author}</cite>
                `;
                    testimonialsContainer.appendChild(testimonialDiv);
                });

                startTestimonialSlideshow();
            })
            .catch(error => {
                console.error('Error fetching testimonials:', error);
            });
    }

    // Function to start the testimonial slideshow
    function startTestimonialSlideshow() {
        const testimonials = document.querySelectorAll('.testimonial');
        let currentIndex = 0;

        setInterval(() => {
            testimonials[currentIndex].classList.remove('active');
            currentIndex = (currentIndex + 1) % testimonials.length;
            testimonials[currentIndex].classList.add('active');
        }, 10000); // Change slide every 5 seconds
    }

    // Initialize fetching of data
    fetchFeaturedProducts();
    fetchTestimonials();
});