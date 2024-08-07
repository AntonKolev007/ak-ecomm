document.addEventListener("DOMContentLoaded", function() {
    const signupForm = document.getElementById("signupForm");

    if (signupForm) {
        signupForm.addEventListener("submit", handleSignup);
    }

    function handleSignup(event) {
        event.preventDefault();
        const formData = new FormData(signupForm);
        const roles = [];
        signupForm.querySelectorAll('input[name="role"]:checked').forEach((checkbox) => {
            roles.push(checkbox.getAttribute('data-api-value')); // Use data-api-value for API call
        });

        const signupRequest = {
            username: formData.get("username"),
            email: formData.get("email"),
            password: formData.get("password"),
            role: roles // Ensure the correct key is used
        };

        console.log(signupRequest); // Add this line for debugging

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
});