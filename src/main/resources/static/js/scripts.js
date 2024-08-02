document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("loginForm");
    const signupForm = document.getElementById("signupForm");
    const signoutButton = document.querySelector(".nav-links a[href='/logout']");
    const clearFiltersButton = document.getElementById('clearFilters');
    const sortByElement = document.getElementById('sortBy');
    const sortOrderElement = document.getElementById('sortOrder');

    let allProducts = [];
    let currentPage = 0;
    const pageSize = 12;

    if (loginForm) {
        loginForm.addEventListener("submit", handleLogin);
    }

    if (signupForm) {
        signupForm.addEventListener("submit", handleSignup);
    }

    if (signoutButton) {
        signoutButton.addEventListener("click", handleSignout);
    }

    if (sortByElement) {
        sortByElement.addEventListener('change', () => displayProducts(0));
    }

    if (sortOrderElement) {
        sortOrderElement.addEventListener('change', () => displayProducts(0));
    }

    if (clearFiltersButton) {
        clearFiltersButton.addEventListener('click', clearFilters);
    }

    if (document.querySelector(".products-list")) {
        fetchAllProducts();
    }

    if (document.getElementById("profile-info")) {
        fetchProfile();
    }

    function handleLogin(event) {
        event.preventDefault();
        const formData = new FormData(loginForm);
        const loginRequest = {
            username: formData.get("username"),
            password: formData.get("password")
        };

        fetch("/api/auth/signin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(loginRequest)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "Invalid login credentials");
                    });
                }
            })
            .then(data => {
                sessionStorage.setItem('jwtToken', data.jwtToken);
                window.location.href = "/";
            })
            .catch(error => {
                console.error("Error during login:", error);
                showError(loginForm, error.message || "An error occurred during login. Please try again.");
                loginForm.querySelector('input[type="password"]').value = '';
            });
    }

    function handleSignup(event) {
        event.preventDefault();
        const formData = new FormData(signupForm);
        const roles = [];
        signupForm.querySelectorAll('input[name="role"]:checked').forEach((checkbox) => {
            roles.push(checkbox.value);
        });

        const signupRequest = {
            username: formData.get("username"),
            email: formData.get("email"),
            password: formData.get("password"),
            role: roles
        };

        fetch("/api/auth/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(signupRequest)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = "/login";
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "An error occurred during signup. Please try again.");
                    });
                }
            })
            .catch(error => {
                console.error("Error during signup:", error);
                showError(signupForm, error.message || "An error occurred during signup. Please try again.");
                signupForm.querySelector('input[type="password"]').value = '';
            });
    }

    function handleSignout(event) {
        event.preventDefault();
        sessionStorage.removeItem('jwtToken');
        fetch("/api/auth/signout", {
            method: "POST"
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = "/";
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "An error occurred during signout.");
                    });
                }
            })
            .catch(error => {
                console.error("Error during signout:", error);
                showError(document.body, error.message || "An error occurred during signout. Please try again.");
            });
    }

    function showError(form, message) {
        let errorDiv = form.querySelector(".error-message");
        if (!errorDiv) {
            errorDiv = document.createElement("div");
            errorDiv.className = "error-message";
            form.prepend(errorDiv);
        }
        errorDiv.textContent = message;
    }

    function fetchAllProducts() {
        fetch(`/api/public/products`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                allProducts = data.content;
                displayProducts(0);
            })
            .catch(error => console.error('Error fetching products:', error));
    }

    function displayProducts(page) {
        currentPage = page;
        const productsList = document.querySelector('.products-list');
        productsList.innerHTML = '';

        let sortedProducts = [...allProducts];

        const sortBy = sortByElement ? sortByElement.value : 'productName';
        const sortOrder = sortOrderElement ? sortOrderElement.value : 'asc';

        sortedProducts.sort((a, b) => {
            if (a[sortBy] < b[sortBy]) return sortOrder === 'asc' ? -1 : 1;
            if (a[sortBy] > b[sortBy]) return sortOrder === 'asc' ? 1 : -1;
            return 0;
        });

        const start = page * pageSize;
        const end = start + pageSize;
        const paginatedProducts = sortedProducts.slice(start, end);

        paginatedProducts.forEach(product => {
            const productItem = document.createElement('div');
            productItem.className = 'product-item';
            productItem.innerHTML = `
                <img src="${product.image}" alt="${product.productName}">
                <h3>${product.productName}</h3>
                <p class="price">${product.price}</p>
                <p class="special-price">${product.specialPrice}</p>
                <p>${product.description.split(' ').slice(0, 3).join(' ')}...</p>
                ${product.discount > 0 ? `<div class="discount-stamp">-${product.discount}</div>` : ''}
            `;
            productsList.appendChild(productItem);
        });

        updatePagination();
    }

    function updatePagination() {
        const pageInfo = document.getElementById('pageInfo');
        const totalPages = Math.ceil(allProducts.length / pageSize);
        pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages}`;

        document.querySelector('.pagination button[onclick="prevPage()"]').disabled = currentPage === 0;
        document.querySelector('.pagination button[onclick="nextPage()"]').disabled = currentPage >= totalPages - 1;
    }

    window.prevPage = function() {
        if (currentPage > 0) {
            displayProducts(currentPage - 1);
        }
    }

    window.nextPage = function() {
        const totalPages = Math.ceil(allProducts.length / pageSize);
        if (currentPage < totalPages - 1) {
            displayProducts(currentPage + 1);
        }
    }

    window.searchProducts = function() {
        const searchInput = document.getElementById('searchInput').value.toLowerCase();
        const filteredProducts = allProducts.filter(product =>
            product.productName.toLowerCase().includes(searchInput) ||
            product.description.toLowerCase().includes(searchInput)
        );
        displayFilteredProducts(filteredProducts);
    }

    function displayFilteredProducts(products) {
        const productsList = document.querySelector('.products-list');
        productsList.innerHTML = '';

        products.forEach(product => {
            const productItem = document.createElement('div');
            productItem.className = 'product-item';
            productItem.innerHTML = `
                <img src="${product.image}" alt="${product.productName}">
                <h3>${product.productName}</h3>
                <p class="price">${product.price}</p>
                <p class="special-price">${product.specialPrice}</p>
                <p>${product.description.split(' ').slice(0, 3).join(' ')}...</p>
                ${product.discount > 0 ? `<div class="discount-stamp">-${product.discount}</div>` : ''}
            `;
            productsList.appendChild(productItem);
        });

        updatePaginationForFiltered(products.length);
    }

    function updatePaginationForFiltered(totalElements) {
        const pageInfo = document.getElementById('pageInfo');
        const totalPages = Math.ceil(totalElements / pageSize);
        pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages}`;

        document.querySelector('.pagination button[onclick="prevPage()"]').disabled = currentPage === 0;
        document.querySelector('.pagination button[onclick="nextPage()"]').disabled = currentPage >= totalPages - 1;
    }

    function clearFilters() {
        document.getElementById('searchInput').value = '';
        sortByElement.value = 'productName';
        sortOrderElement.value = 'asc';
        fetchAllProducts();
    }

    function fetchProfile() {
        fetch('/api/auth/user', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                document.getElementById('user-id').textContent = data.id;
                document.getElementById('username').textContent = data.username;
                document.getElementById('roles').textContent = data.roles.join(', ');
            })
            .catch(error => {
                document.getElementById('error-message').textContent = error.message;
                document.getElementById('error-message').style.display = 'block';
            });
    }
});