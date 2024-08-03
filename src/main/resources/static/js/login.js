document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", handleLogin);
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