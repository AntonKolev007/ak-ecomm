document.addEventListener("DOMContentLoaded", function () {
    const orderForm = document.getElementById("orderForm");

    if (orderForm) {
        orderForm.addEventListener("submit", handleOrderSubmit);
    }

    function handleOrderSubmit(event) {
        event.preventDefault();
        const formData = new FormData(orderForm);

        const orderRequest = {
            addressId: formData.get("addressId"),
            pgName: formData.get("pgName"),
            pgPaymentId: formData.get("pgPaymentId"),
            pgStatus: formData.get("pgStatus"),
            pgResponseMessage: formData.get("pgResponseMessage")
        };

        console.log(orderRequest); // Add this line for debugging

        fetch("/api/order/users/payments/" + orderRequest.pgName, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            },
            body: JSON.stringify(orderRequest)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = "/confirmation";
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "An error occurred while placing the order. Please try again.");
                    });
                }
            })
            .catch(error => {
                console.error("Error placing order:", error);
                showError(orderForm, error.message || "An error occurred while placing the order. Please try again.");
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