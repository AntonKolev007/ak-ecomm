document.addEventListener("DOMContentLoaded", function() {

    const changeUsernameBtn = document.getElementById("changeUsernameBtn");
    const changePasswordBtn = document.getElementById("changePasswordBtn");
    const addressesBtn = document.getElementById("addressesBtn");
    const adminPanelBtn = document.getElementById("adminPanelBtn");

    if (changeUsernameBtn) {
        changeUsernameBtn.addEventListener("click", function () {
            showUsernamePrompt();
        });
    }

    if (changePasswordBtn) {
        changePasswordBtn.addEventListener("click", function () {
            showPasswordPrompt();
        });
    }

    if (addressesBtn) {
        addressesBtn.addEventListener("click", function () {
            window.location.href = "/addresses-view";
        });
    }

    if (adminPanelBtn) {
        adminPanelBtn.addEventListener("click", function () {
            window.location.href = "/admin-panel";
        });
    }

    fetchProfile();

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

                if (data.roles.includes('ROLE_ADMIN')) {
                    adminPanelBtn.style.display = 'inline-block';
                } else {
                    adminPanelBtn.style.display = 'none';
                }
            })
            .catch(error => {
                console.error('Error fetching profile:', error);
                document.getElementById('error-message').textContent = error.message;
                document.getElementById('error-message').style.display = 'block';
            });
    }

    function showUsernamePrompt() {
        const modal = createModal("Enter your new username:");
        const input = createInput("text", "Enter your new username");
        const submitBtn = createSubmitButton("Submit");

        submitBtn.addEventListener("click", function() {
            const newUsername = input.value;
            if (newUsername) {
                updateUsername(newUsername);
                document.body.removeChild(modal);
            } else {
                alert("Username cannot be empty.");
            }
        });

        modal.appendChild(input);
        modal.appendChild(submitBtn);
        document.body.appendChild(modal);
    }

    function showPasswordPrompt() {
        const modal = createModal("Enter your new password:");
        const input = createInput("password", "Enter your new password");
        const submitBtn = createSubmitButton("Submit");

        submitBtn.addEventListener("click", function() {
            const newPassword = input.value;
            if (newPassword) {
                updatePassword(newPassword);
                document.body.removeChild(modal);
            } else {
                alert("Password cannot be empty.");
            }
        });

        modal.appendChild(input);
        modal.appendChild(submitBtn);
        document.body.appendChild(modal);
    }

    function updateUsername(newUsername) {
        fetch("/api/auth/updateUsername", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + sessionStorage.getItem('jwtToken')
            },
            body: JSON.stringify({ username: newUsername })
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "Username updated successfully!") {
                    alert(data.message);
                    sessionStorage.removeItem('jwtToken');
                    window.location.href = "/login"; // Redirect to login page
                } else {
                    alert(data.message);
                }
            })
            .catch(error => console.error("Error updating username:", error));
    }

    function updatePassword(newPassword) {
        fetch("/api/auth/updatePassword", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + sessionStorage.getItem('jwtToken')
            },
            body: JSON.stringify({ password: newPassword })
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "Password updated successfully!") {
                    alert(data.message);
                } else {
                    alert(data.message);
                }
            })
            .catch(error => console.error("Error updating password:", error));
    }

    function createModal(message) {
        const modal = document.createElement("div");
        modal.style.position = "fixed";
        modal.style.top = "50%";
        modal.style.left = "50%";
        modal.style.transform = "translate(-50%, 50%)";
        modal.style.backgroundColor = "#fff";
        modal.style.padding = "20px";
        modal.style.boxShadow = "0 0 10px rgba(0,0,0,0.1)";
        modal.style.zIndex = "1000";

        const messageElement = document.createElement("p");
        messageElement.textContent = message;
        messageElement.style.marginBottom = "10px";
        modal.appendChild(messageElement);

        return modal;
    }

    function createInput(type, placeholder) {
        const input = document.createElement("input");
        input.type = type;
        input.placeholder = placeholder;
        input.style.display = "block";
        input.style.marginBottom = "10px";
        input.style.width = "90%";
        input.style.padding = "10px";
        input.style.border = "1px solid #ccc";
        input.style.borderRadius = "4px";
        input.style.marginTop="20px";

        return input;
    }

    function createSubmitButton(text) {
        const button = document.createElement("button");
        button.textContent = text;
        button.style.backgroundColor = "#009688";
        button.style.color = "#fff";
        button.style.border = "none";
        button.style.padding = "10px 20px";
        button.style.cursor = "pointer";
        button.style.borderRadius = "4px";

        return button;
    }
});