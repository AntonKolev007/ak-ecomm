document.addEventListener("DOMContentLoaded", function () {
    const deleteProductButton = document.getElementById("deleteProductButton");
    const orderButton = document.getElementById("orderButton");
    let cartId = null;

    fetchCart();

    if (deleteProductButton) {
        deleteProductButton.addEventListener("click", deleteProduct);
    }

    if (orderButton) {
        orderButton.addEventListener("click", placeOrder);
    }

    function fetchCart() {
        fetch('/api/carts/users/cart', {
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
                cartId = data.cartId; // Save cartId for future operations
                displayCartProducts(data.products || []);
                updateTotalPrice(data.totalPrice || 0);
                updatePagination(data.totalPages || 1);
            })
            .catch(error => {
                console.error('Error fetching cart:', error);
                showError(error.message || 'An error occurred while fetching the cart.');
            });
    }

    function displayCartProducts(products) {
        const productsList = document.querySelector(".products-list");
        productsList.innerHTML = "";

        if (products.length === 0) {
            productsList.innerHTML = "<p>Your cart is empty.</p>";
            return;
        }

        products.forEach(product => {
            const productElement = document.createElement("div");
            productElement.classList.add("product-item");
            productElement.innerHTML = `
                <h3>${product.productName}</h3>
                <img src="/images/${product.image}" alt="${product.productName}">
                <p>${product.description}</p>
                <!--            <p class="price">${product.price}</p>-->
            ${product.discount > 0 ? `<p class="price">${product.price}</p>`
                : `<p class="price" style="color: black; text-decoration: none">${product.price}</p>` }
            ${product.discount >0 ?`<p class="special-price">${product.specialPrice}</p>` : ''}
                <div class="discount-stamp">${product.discount}%</div>
                <p>Quantity: ${product.quantity}</p>
                <div class="quantity-controls">
                    <button class="quantity-button" data-product-id="${product.productId}" data-operation="delete">-</button>
                    <button class="quantity-button" data-product-id="${product.productId}" data-operation="add">+</button>
                </div>
            `;
            productElement.addEventListener("click", () => {
                selectProduct(product);
            });
            productsList.appendChild(productElement);

            // Add event listeners for quantity buttons
            productElement.querySelectorAll(".quantity-button").forEach(button => {
                button.addEventListener("click", handleQuantityChange);
            });
        });
    }

    function selectProduct(product) {
        document.getElementById("deleteProductButton").dataset.productId = product.productId;

        // Highlight the selected product
        const selectedProductElement = document.querySelector(".product-item.selected");
        if (selectedProductElement) {
            selectedProductElement.classList.remove("selected");
        }
        const productElement = [...document.querySelectorAll(".product-item")].find(el => {
            return el.querySelector("h3").textContent === product.productName;
        });
        if (productElement) {
            productElement.classList.add("selected");
        }
    }

    function handleQuantityChange(event) {
        const button = event.target;
        const productId = button.dataset.productId;
        const operation = button.dataset.operation;

        fetch(`/api/cart/products/${productId}/quantity/${operation}`, {
            method: 'PUT',
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
                fetchCart(); // Refresh the cart
            })
            .catch(error => {
                console.error('Error updating product quantity:', error);
                showError(error.message || 'An error occurred while updating the product quantity.');
            });
    }

    function deleteProduct() {
        const productId = this.dataset.productId;

        fetch(`/api/carts/${cartId}/product/${productId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                fetchCart(); // Refresh the cart
            })
            .catch(error => {
                console.error('Error deleting product from cart:', error);
                showError(error.message || 'An error occurred while deleting the product from the cart.');
            });
    }

    function placeOrder() {
        // Implement order placement logic here
    }

    function updateTotalPrice(totalPrice) {
        document.getElementById("totalPrice").textContent = totalPrice.toFixed(2);
    }

    function showError(message) {
        const errorMessage = document.getElementById("error-message");
        if (errorMessage) {
            errorMessage.textContent = message;
            errorMessage.style.display = "block";
        }
    }

    function updatePagination(totalPages) {
        const prevButton = document.getElementById("prevPage");
        const nextButton = document.getElementById("nextPage");

        if (totalPages <= 1) {
            prevButton.disabled = true;
            nextButton.disabled = true;
        } else {
            prevButton.disabled = false;
            nextButton.disabled = false;
        }
    }
});