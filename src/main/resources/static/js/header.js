document.addEventListener("DOMContentLoaded", function () {
    const signoutButton = document.querySelector(".nav-links a[href='/logout']");
    const languageSelect = document.getElementById("languageSelect");

    if (signoutButton) {
        signoutButton.addEventListener("click", handleSignout);
    }

    if (languageSelect) {
        languageSelect.addEventListener("change", handleLanguageChange);
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
});