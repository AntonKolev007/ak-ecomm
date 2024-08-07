document.addEventListener("DOMContentLoaded", function() {
    // Attach event listeners to the login and signup buttons
    const loginForm = document.getElementById("loginForm");
    const signupForm = document.getElementById("signupForm");

    if (loginForm) {
        loginForm.addEventListener("submit", handleLogin);
    }

    if (signupForm) {
        signupForm.addEventListener("submit", handleSignup);
    }

    // Function to handle login
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

    // Function to handle signup
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

    // Function to show error messages
    function showError(form, message) {
        let errorDiv = form.querySelector(".error-message");
        if (!errorDiv) {
            errorDiv = document.createElement("div");
            errorDiv.className = "error-message";
            form.prepend(errorDiv);
        }
        errorDiv.textContent = message;
    }
});