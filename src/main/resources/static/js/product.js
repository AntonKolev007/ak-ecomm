document.addEventListener("DOMContentLoaded", function() {
    const product = JSON.parse(localStorage.getItem('selectedProduct'));
    if (!product) {
        alert("No product data found!");
        return;
    }

    const imagePath = `/images/${product.image}`;
    document.getElementById('productName').textContent = product.productName;
    document.getElementById('productImage').src = imagePath|| 'default.png';
    document.getElementById('productDescription').textContent = product.description;
    document.getElementById('productQuantity').textContent = product.quantity;

    if (product.discount > 0) {
        document.getElementById('productPrice').textContent = product.price;
        document.getElementById('productSpecialPrice').textContent = product.specialPrice;
        document.getElementById('youSave').style.display = 'block';
        document.getElementById('priceDifference').textContent = (product.price - product.specialPrice).toFixed(2);
    } else {
        document.getElementById('productPrice').textContent = product.price;
        document.getElementById('productSpecialPrice').style.display = 'none';
    }

    const quantitySlider = document.getElementById('quantitySlider');
    const quantityValue = document.getElementById('quantityValue');

    if (product.quantity > 0) {
        quantitySlider.max = product.quantity;
        quantitySlider.value = 1;
        quantityValue.textContent = quantitySlider.value;

        quantitySlider.addEventListener('input', function() {
            quantityValue.textContent = quantitySlider.value;
        });

        document.getElementById('addToCartButton').addEventListener('click', function() {
            addToCart(product.productId, quantitySlider.value);
        });
    } else {
        document.getElementById('outOfStock').style.display = 'block';
        quantitySlider.disabled = true;
        document.getElementById('addToCartButton').disabled = true;
        document.querySelector('label[for="quantitySlider"]').textContent = "Out of Stock";
    }

    document.getElementById('backButton').addEventListener('click', function() {
        window.history.back();
    });

    function addToCart(productId, quantity) {
        fetch(`/api/carts/products/${productId}/quantity/${quantity}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error(data.message || 'Error adding product to cart');
                    });
                }
                return response.json();
            })
            .then(data => {
                alert('Product added to cart successfully!');
            })
            .catch(error => {
                console.error('Error adding product to cart:', error);
                alert('Error adding product to cart. Please try again.');
            });
    }
});