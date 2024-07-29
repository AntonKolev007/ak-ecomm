document.addEventListener("DOMContentLoaded", function() {
    // Attach event listeners to the login and signup buttons
    const loginForm = document.getElementById("loginForm");
    const signupForm = document.getElementById("signupForm");
    const signoutButton = document.querySelector(".nav-links a[href='/logout']");

    if (loginForm) {
        loginForm.addEventListener("submit", handleLogin);
    }

    if (signupForm) {
        signupForm.addEventListener("submit", handleSignup);
    }

    if (signoutButton) {
        signoutButton.addEventListener("click", handleSignout);
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
                document.cookie = data.jwtToken;
                window.location.href = "/";
            })
            .catch(error => {
                console.error("Error during login:", error);
                showError(loginForm, error.message || "An error occurred during login. Please try again.");
                // Clear the password field
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
                    return response.json();
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "An error occurred during signup. Please try again.");
                    });
                }
            })
            .then(data => {
                window.location.href = "/login";
            })
            .catch(error => {
                console.error("Error during signup:", error);
                showError(signupForm, error.message || "An error occurred during signup. Please try again.");
                // Clear the password field
                signupForm.querySelector('input[type="password"]').value = '';
            });
    }

    // Function to handle signout
    function handleSignout(event) {
        event.preventDefault();
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
