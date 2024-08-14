document.addEventListener("DOMContentLoaded", function() {
    const addressForm = document.getElementById("addressForm");

    if (addressForm) {
        addressForm.addEventListener("submit", handleAddressSubmit);
    }

    function handleAddressSubmit(event) {
        event.preventDefault();
        const formData = new FormData(addressForm);

        const addressRequest = {
            street: formData.get("street"),
            buildingName: formData.get("buildingName"),
            city: formData.get("city"),
            state: formData.get("state"),
            country: formData.get("country"),
            zipCode: formData.get("zipCode")
        };

        console.log(addressRequest); // Add this line for debugging

        fetch("/api/addresses", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(addressRequest)
        })
            .then(response => {
                if (response.ok) {
                    const redirectTo = new URLSearchParams(window.location.search).get('redirectTo') || '/profile';
                    window.location.href = redirectTo;
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "An error occurred while adding the address. Please try again.");
                    });
                }
            })
            .catch(error => {
                console.error("Error adding address:", error);
                showError(addressForm, error.message || "An error occurred while adding the address. Please try again.");
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