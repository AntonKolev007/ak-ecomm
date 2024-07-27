document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.querySelector("form[th\\:action='@{/login}']");
    const signupForm = document.querySelector("form[th\\:action='@{/signup}']");
    const signupLink = document.querySelector(".nav-links a[href='/signup']");

    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            event.preventDefault();
            handleLogin();
        });
    }

    if (signupForm) {
        signupForm.addEventListener("submit", function(event) {
            event.preventDefault();
            handleSignup();
        });
    }

    if (signupLink) {
        signupLink.addEventListener("click", function(event) {
            event.preventDefault();
            window.location.href = '/signup';
        });
    }
});

function handleLogin() {
    const username = document.querySelector("#username").value;
    const password = document.querySelector("#password").value;
    const redirectUrl = document.querySelector("input[name='redirectUrl']").value || "/home.html";

    fetch("/api/auth/signin", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, password })
    })
        .then(response => response.json())
        .then(data => {
            if (data && data.status === false) {
                displayError(data.message || "Login failed. Please try again.");
            } else {
                document.cookie = `accessToken=${data.accessToken}; path=/`;
                window.location.href = redirectUrl;
            }
        })
        .catch(error => {
            console.error("Error during login:", error);
            displayError("An error occurred. Please try again.");
        });
}

function handleSignup() {
    const username = document.querySelector("#username").value;
    const email = document.querySelector("#email").value;
    const password = document.querySelector("#password").value;
    const roles = Array.from(document.querySelectorAll("input[type=checkbox]:checked")).map(cb => cb.value);

    fetch("/api/auth/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ username, email, password, role: roles })
    })
        .then(response => response.json())
        .then(data => {
            if (data.message && data.message.startsWith("Error")) {
                displayError(data.message);
            } else {
                window.location.href = "/login";
            }
        })
        .catch(error => {
            console.error("Error during signup:", error);
            displayError("An error occurred. Please try again.");
        });
}

function displayError(message) {
    const errorDiv = document.querySelector(".error");
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = "block"; // Ensure the error message is visible
    }
}
