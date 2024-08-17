document.addEventListener("DOMContentLoaded", function () {
    const productForm = document.getElementById("productForm");
    const productList = document.getElementById("productList");
    const categoryList = document.getElementById("categoryList");
    const modalTitle = document.getElementById("modalTitle");
    const productModal = document.getElementById("productModal");
    const addProductBtn = document.getElementById("addProductBtn");
    const addCategoryBtn = document.getElementById("addCategoryBtn");
    let editingProductId = null;
    let productsData = [];
    let categoriesData = [];

    // Initially hide the Add Product and Add Category buttons
    addProductBtn.style.display = "none";
    addCategoryBtn.style.display = "none";
    const usersManagementBtn = document.getElementById("manageUsersBtn");
    usersManagementBtn.style.display = "none";

    // Load products when "Manage Products" is clicked
    document.getElementById("manageProductsBtn").addEventListener("click", function () {
        loadProducts();
        addProductBtn.style.display = "inline-block";
        addCategoryBtn.style.display = "none";
        productList.style.display = "block";
        categoryList.style.display = "none";
    });

    // Load categories when "Manage Categories" is clicked
    document.getElementById("manageCategoriesBtn").addEventListener("click", function () {
        loadCategories();
        addCategoryBtn.style.display = "inline-block";
        addProductBtn.style.display = "none";
        categoryList.style.display = "block";
        productList.style.display = "none";
    });

    function loadProducts() {
        fetch("/api/public/products")
            .then(response => response.json())
            .then(data => {
                productsData = data.content;
                displayProducts(productsData);
            })
            .catch(error => console.error("Error fetching products:", error));
    }

    function displayProducts(products) {
        productList.innerHTML = `
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${products.map(product => `
                        <tr>
                            <td>${product.productId}</td>
                            <td>${product.productName}</td>
                            <td>${product.description}</td>
                            <td>${product.price}</td>
                            <td>
                                <button class="edit-btn" data-id="${product.productId}">Edit</button>
                                <button class="delete-btn" data-id="${product.productId}">Delete</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        document.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', function () {
                const productId = this.dataset.id;
                loadProductForEdit(productId);
            });
        });

        document.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', function () {
                const productId = this.dataset.id;
                deleteProduct(productId);
            });
        });
    }

    function handleProductSubmit(event) {
        event.preventDefault();
        const formData = new FormData(productForm);

        const productRequest = {
            productName: formData.get("productName"),
            description: formData.get("description"),
            quantity: formData.get("quantity"),
            price: formData.get("price"),
            discount: formData.get("discount"),
            categoryId: 1 // Defaulting to category 1
        };

        if (editingProductId) {
            updateProduct(editingProductId, productRequest);
        } else {
            createProduct(productRequest);
        }
    }

    function createProduct(productRequest) {
        clearErrors(); // Clear previous errors before submitting
        fetch("/api/admin/categories/1/product", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${getJwtTokenFromCookie()}`
            },
            body: JSON.stringify(productRequest)
        })
            .then(response => {
                if (response.ok) {
                    closeModal();
                    loadProducts();
                } else {
                    return response.json().then(data => {
                        throw new Error(JSON.stringify(data));
                    });
                }
            })
            .catch(error => {
                console.error("Error adding product:", error);
                handleValidationErrors(JSON.parse(error.message));
            });
    }

    function loadProductForEdit(productId) {
        const product = productsData.find(p => p.productId === parseInt(productId));

        if (product) {
            editingProductId = product.productId;
            productForm.productName.value = product.productName || "";
            productForm.description.value = product.description || "";
            productForm.quantity.value = product.quantity || 0;
            productForm.price.value = product.price || 0;
            productForm.discount.value = product.discount || 0;

            modalTitle.textContent = "Edit Product";
            productModal.style.display = "block";
        } else {
            console.error("Product not found in local data.");
        }
    }

    function updateProduct(productId, productRequest) {
        clearErrors(); // Clear previous errors before submitting
        fetch(`/api/admin/products/${productId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${getJwtTokenFromCookie()}`
            },
            body: JSON.stringify(productRequest)
        })
            .then(response => {
                if (response.ok) {
                    closeModal();
                    loadProducts();
                } else {
                    return response.json().then(data => {
                        throw new Error(JSON.stringify(data));
                    });
                }
            })
            .catch(error => {
                console.error("Error updating product:", error);
                handleValidationErrors(JSON.parse(error.message));
            });
    }

    function deleteProduct(productId) {
        fetch(`/api/admin/products/${productId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${getJwtTokenFromCookie()}`
            }
        })
            .then(response => {
                if (response.ok) {
                    loadProducts();
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "An error occurred while deleting the product.");
                    });
                }
            })
            .catch(error => console.error("Error deleting product:", error));
    }

    function resetForm() {
        editingProductId = null;
        if (productForm) productForm.reset();
        modalTitle.textContent = "Add Product";
        clearErrors();
    }

    function closeModal() {
        productModal.style.display = "none";
        resetForm();
    }

    function showError(form, message) {
        clearErrors();
        const errorDiv = document.createElement("div");
        errorDiv.className = "error-message";
        errorDiv.textContent = message;
        form.prepend(errorDiv);
    }

    function clearErrors() {
        const errorMessages = document.querySelectorAll(".error-message");
        errorMessages.forEach(error => error.remove());
    }

    function handleValidationErrors(errors) {
        if (errors && errors.constraintViolations) {
            errors.constraintViolations.forEach(violation => {
                const field = violation.propertyPath;
                const message = violation.interpolatedMessage;

                const inputField = document.getElementById(field);
                if (inputField) {
                    const errorDiv = document.createElement("div");
                    errorDiv.className = "error-message";
                    errorDiv.textContent = message;
                    inputField.parentNode.insertBefore(errorDiv, inputField.nextSibling);
                }
            });
        }
    }

    function getJwtTokenFromCookie() {
        const cookieString = document.cookie;
        const cookies = cookieString.split('; ');
        const tokenCookie = cookies.find(cookie => cookie.startsWith('ecommerceCookieWithCashSmell='));
        if (tokenCookie) {
            return tokenCookie.split('=')[1];
        }
        return null;
    }

    // Show the modal for adding a new product
    if (addProductBtn) {
        addProductBtn.addEventListener('click', function () {
            resetForm();
            productModal.style.display = 'block';
        });
    }

    // Close modal when clicking the "x"
    if (document.querySelector('.close')) {
        document.querySelector('.close').addEventListener('click', closeModal);
    }

    // Category management
    function loadCategories() {
        fetch("/api/public/categories")
            .then(response => response.json())
            .then(data => {
                categoriesData = data.content;
                displayCategories(categoriesData);
            })
            .catch(error => console.error("Error fetching categories:", error));
    }

    function displayCategories(categories) {
        categoryList.innerHTML = `
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${categories.map(category => `
                        <tr>
                            <td>${category.categoryId}</td>
                            <td>${category.categoryName}</td>
                            <td>
                                <button class="edit-category" data-id="${category.categoryId}">Edit</button>
                                <button class="delete-category" data-id="${category.categoryId}">Delete</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        document.querySelectorAll('.edit-category').forEach(btn => {
            btn.addEventListener('click', function () {
                const categoryId = this.dataset.id;
                enterCategoryEditMode(categoryId);
            });
        });

        document.querySelectorAll('.delete-category').forEach(btn => {
            btn.addEventListener('click', function () {
                const categoryId = this.dataset.id;
                deleteCategory(categoryId);
            });
        });
    }

    function enterCategoryEditMode(categoryId) {
        const categoryRow = document.querySelector(`.edit-category[data-id="${categoryId}"]`).closest('tr');
        const categoryNameCell = categoryRow.querySelector('td:nth-child(2)'); // Assuming 2nd column has the category name

        const currentName = categoryNameCell.textContent.trim();
        categoryNameCell.innerHTML = `<input type="text" id="editCategoryName" value="${currentName}" />`;

        const editButton = categoryRow.querySelector('.edit-category');
        editButton.textContent = "Update";

        // Remove existing click event listeners to prevent duplicates
        editButton.replaceWith(editButton.cloneNode(true));
        const newEditButton = categoryRow.querySelector('.edit-category');

        newEditButton.addEventListener('click', function () {
            const updatedName = categoryNameCell.querySelector('input').value.trim();
            updateCategory(categoryId, updatedName);
        });
    }


    function updateCategory(categoryId, updatedName) {
        fetch(`/api/admin/categories/${categoryId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${getJwtTokenFromCookie()}`
            },
            body: JSON.stringify({ categoryName: updatedName })
        })
            .then(response => {
                if (response.ok) {
                    loadCategories();
                } else {
                    return response.json().then(data => {
                        throw new Error(JSON.stringify(data));
                    });
                }
            })
            .catch(error => {
                console.error("Error updating category:", error);
            });
    }

    function deleteCategory(categoryId) {
        fetch(`/api/admin/categories/${categoryId}`, {
            method: "DELETE",
            headers: {
                "Authorization": `Bearer ${getJwtTokenFromCookie()}`
            }
        })
            .then(response => {
                if (response.ok) {
                    loadCategories();
                } else {
                    return response.json().then(data => {
                        throw new Error(data.message || "An error occurred while deleting the category.");
                    });
                }
            })
            .catch(error => console.error("Error deleting category:", error));
    }

    // Show the modal for adding a new category
    if (addCategoryBtn) {
        addCategoryBtn.addEventListener('click', function () {
            const newCategoryName = prompt("Enter new category name:");
            if (newCategoryName && newCategoryName.trim()) {
                createCategory({ categoryName: newCategoryName.trim() });
            }
        });
    }

    function createCategory(categoryRequest) {
        fetch("/api/admin/categories", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${getJwtTokenFromCookie()}`
            },
            body: JSON.stringify(categoryRequest)
        })
            .then(response => {
                if (response.ok) {
                    loadCategories();
                } else {
                    return response.json().then(data => {
                        throw new Error(JSON.stringify(data));
                    });
                }
            })
            .catch(error => console.error("Error adding category:", error));
    }
});
