document.addEventListener("DOMContentLoaded", function() {
    const addAddressBtn = document.getElementById("addAddressBtn");
    const updateAddressForm = document.getElementById("updateAddressForm");
    const addressModal = document.getElementById("addressModal");
    const closeModal = document.getElementById("closeModal");
    const errorMessage = document.getElementById("error-message");
    let selectedAddressId = null;

    fetchAddresses();

    if (addAddressBtn) {
        addAddressBtn.addEventListener("click", function () {
            window.location.href = "/addresses";
        });
    }

    if (updateAddressForm) {
        updateAddressForm.addEventListener("submit", handleUpdateAddressSubmit);
    }

    if (closeModal) {
        closeModal.addEventListener("click", function () {
            addressModal.style.display = "none";
        });
    }

    window.onclick = function(event) {
        if (event.target == addressModal) {
            addressModal.style.display = "none";
        }
    }

    function fetchAddresses() {
        fetch('/api/users/addresses', {
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
                displayAddresses(data);
            })
            .catch(error => {
                console.error('Error fetching addresses:', error);
                showError(error.message || 'An error occurred while fetching the addresses.');
            });
    }

    function displayAddresses(addresses) {
        const addressesList = document.querySelector(".addresses-list");
        addressesList.innerHTML = "";

        if (addresses.length === 0) {
            addressesList.innerHTML = "<p>You have no addresses.</p>";
            return;
        }

        addresses.forEach(address => {
            const addressElement = document.createElement("div");
            addressElement.classList.add("address-item");
            addressElement.innerHTML = `
                <p>
                    ${address.street}, ${address.buildingName}, ${address.city}, 
                    ${address.state}, ${address.country}, ${address.zipCode}
                </p>
                <div>
                    <button class="edit-btn" data-address-id="${address.addressId}">Edit</button>
                    <button class="delete-btn" data-address-id="${address.addressId}">Delete</button>
                </div>
            `;

            addressElement.querySelector(".edit-btn").addEventListener("click", function () {
                openEditModal(address);
            });

            addressElement.querySelector(".delete-btn").addEventListener("click", function () {
                deleteAddress(address.addressId);
            });

            addressesList.appendChild(addressElement);
        });
    }

    function openEditModal(address) {
        addressModal.style.display = "block";
        selectedAddressId = address.addressId;
        document.getElementById("addressId").value = address.addressId;
        document.getElementById("street").value = address.street;
        document.getElementById("buildingName").value = address.buildingName;
        document.getElementById("city").value = address.city;
        document.getElementById("state").value = address.state;
        document.getElementById("country").value = address.country;
        document.getElementById("zipCode").value = address.zipCode;
    }

    function handleUpdateAddressSubmit(event) {
        event.preventDefault();
        const addressId = document.getElementById("addressId").value;
        const addressRequest = {
            street: document.getElementById("street").value,
            buildingName: document.getElementById("buildingName").value,
            city: document.getElementById("city").value,
            state: document.getElementById("state").value,
            country: document.getElementById("country").value,
            zipCode: document.getElementById("zipCode").value
        };

        fetch(`/api/addresses/${addressId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            },
            body: JSON.stringify(addressRequest)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.message || 'An error occurred while updating the address.');
                    });
                }
                return response.json();
            })
            .then(data => {
                addressModal.style.display = "none";
                fetchAddresses(); // Refresh the address list
            })
            .catch(error => {
                console.error('Error updating address:', error);
                showError(error.message || 'An error occurred while updating the address.');
            });
    }

    function deleteAddress(addressId) {
        fetch(`/api/addresses/${addressId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            }
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.message || 'An error occurred while deleting the address.');
                    });
                }
                fetchAddresses(); // Refresh the address list
            })
            .catch(error => {
                console.error('Error deleting address:', error);
                showError(error.message || 'An error occurred while deleting the address.');
            });
    }

    function showError(message) {
        errorMessage.textContent = message;
        errorMessage.style.display = "block";
    }
});