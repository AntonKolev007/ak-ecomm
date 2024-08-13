document.addEventListener("DOMContentLoaded", function () {
    const signoutButton = document.querySelector(".nav-links a[href='/logout']");
    const languageSelect = document.getElementById("languageSelect");
    const loginForm = document.getElementById("loginForm");
    const signupForm = document.getElementById("signupForm");

    if (signoutButton) {
        signoutButton.addEventListener("click", handleSignout);
    }

    if (languageSelect) {
        languageSelect.addEventListener("change", handleLanguageChange);
    }

    if (loginForm) {
        loginForm.addEventListener("submit", handleLogin);
    }

    if (signupForm) {
        signupForm.addEventListener("submit", handleSignup);
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

    function handleLanguageChange(event) {
        const selectedLanguage = event.target.value;
        const currentUrl = window.location.href;
        const baseUrl = currentUrl.split('?')[0];
        const newUrl = baseUrl + "?lang=" + selectedLanguage;
        window.location.href = newUrl;
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

    function showError(form, message) {
        let errorDiv = form.querySelector(".error-message");
        if (!errorDiv) {
            errorDiv = document.createElement("div");
            errorDiv.className = "error-message";
            form.prepend(errorDiv);
        }
        errorDiv.textContent = message;
    }

    // Fetch and display the cart count
    function fetchCartCount() {
        fetch('/api/carts/users/cart', {
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
                const cartCountElement = document.getElementById('cartCount');
                cartCountElement.textContent = data.products.length;
            })
            .catch(error => {
                console.error('Error fetching cart count:', error);
            });
    }

    // Only fetch cart count if the user is logged in
    if (document.querySelector(".nav-links .welcome-message")) {
        fetchCartCount();
    }

    // Listen for custom event to update cart count dynamically
    document.addEventListener('productAddedToCart', fetchCartCount);
});