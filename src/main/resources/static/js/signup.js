document.addEventListener("DOMContentLoaded", function() {
    const signupForm = document.getElementById("signupForm");

    if (signupForm) {
        signupForm.addEventListener("submit", handleSignup);
    }

    function handleSignup(event) {
        event.preventDefault();
        clearErrors();
        const formData = new FormData(signupForm);
        const roles = [];
        signupForm.querySelectorAll('input[name="role"]:checked').forEach((checkbox) => {
            roles.push(checkbox.getAttribute('data-api-value'));
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
                        handleErrors(data);
                    });
                }
            })
            .catch(error => {
                console.error("Error during signup:", error);
                showError(signupForm, error.message || "An error occurred during signup. Please try again.");
                signupForm.querySelector('input[type="password"]').value = '';
            });
    }

    function handleErrors(errors) {
        if (typeof errors === 'object') {
            Object.keys(errors).forEach(field => {
                const fieldElement = signupForm.querySelector(`[name="${field}"]`);
                if (fieldElement) {
                    const errorDiv = document.createElement("div");
                    errorDiv.className = "field-error";
                    errorDiv.textContent = errors[field];
                    fieldElement.parentElement.insertBefore(errorDiv, fieldElement);
                    fieldElement.classList.add('blink');
                    fieldElement.setAttribute('aria-invalid', 'true');
                }
            });
        } else {
            showError(signupForm, errors || "An error occurred during signup. Please try again.");
        }
    }

    function clearErrors() {
        const errorFields = signupForm.querySelectorAll('.field-error');
        errorFields.forEach(errorField => errorField.remove());
        const fields = signupForm.querySelectorAll('.blink');
        fields.forEach(field => {
            field.classList.remove('blink');
            field.removeAttribute('aria-invalid');
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