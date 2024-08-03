document.addEventListener("DOMContentLoaded", function() {
    if (document.getElementById("profile-info")) {
        fetchProfile();
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