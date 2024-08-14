document.addEventListener("DOMContentLoaded", function () {
    const manageUsersBtn = document.getElementById("manageUsersBtn");
    const manageProductsBtn = document.getElementById("manageProductsBtn");
    const manageCategoriesBtn = document.getElementById("manageCategoriesBtn");
    const adminContent = document.getElementById("adminContent");

    if (manageUsersBtn) {
        manageUsersBtn.addEventListener("click", function () {
            loadUsers();
        });
    }

    if (manageProductsBtn) {
        manageProductsBtn.addEventListener("click", function () {
            loadProducts();
        });
    }

    if (manageCategoriesBtn) {
        manageCategoriesBtn.addEventListener("click", function () {
            loadCategories();
        });
    }

    function loadUsers() {
        const token = sessionStorage.getItem('jwtToken');

        fetch('/api/admin/users', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                displayUsers(data);
            })
            .catch(error => console.error('Error fetching users:', error));
    }

    function displayUsers(users) {
        adminContent.innerHTML = `
            <h2>Manage Users</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Roles</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${users.map(user => `
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.roles.join(', ')}</td>
                            <td>
                                <button class="promoteUserBtn" data-id="${user.id}">Promote</button>
                                <button class="demoteUserBtn" data-id="${user.id}">Demote</button>
                                <button class="deleteUserBtn" data-id="${user.id}">Delete</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        document.querySelectorAll('.promoteUserBtn').forEach(btn => {
            btn.addEventListener('click', function () {
                const userId = this.dataset.id;
                promoteUser(userId);
            });
        });

        document.querySelectorAll('.demoteUserBtn').forEach(btn => {
            btn.addEventListener('click', function () {
                const userId = this.dataset.id;
                demoteUser(userId);
            });
        });

        document.querySelectorAll('.deleteUserBtn').forEach(btn => {
            btn.addEventListener('click', function () {
                const userId = this.dataset.id;
                deleteUser(userId);
            });
        });
    }

    function promoteUser(userId) {
        // Implement promote user functionality
    }

    function demoteUser(userId) {
        // Implement demote user functionality
    }

    function deleteUser(userId) {
        const token = sessionStorage.getItem('jwtToken');

        fetch(`/api/admin/delete?userId=${userId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                loadUsers();
            })
            .catch(error => console.error('Error deleting user:', error));
    }

    function loadProducts() {
        const token = sessionStorage.getItem('jwtToken');

        fetch('/api/public/products', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                displayProducts(data.content);
            })
            .catch(error => console.error('Error fetching products:', error));
    }

    function displayProducts(products) {
        adminContent.innerHTML = `
            <h2>Manage Products</h2>
            <button id="addProductBtn">Add Product</button>
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
                                <button class="editProductBtn" data-id="${product.productId}">Edit</button>
                                <button class="deleteProductBtn" data-id="${product.productId}">Delete</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        document.getElementById('addProductBtn').addEventListener('click', function () {
            addProduct();
        });

        document.querySelectorAll('.editProductBtn').forEach(btn => {
            btn.addEventListener('click', function () {
                const productId = this.dataset.id;
                editProduct(productId);
            });
        });

        document.querySelectorAll('.deleteProductBtn').forEach(btn => {
            btn.addEventListener('click', function () {
                const productId = this.dataset.id;
                deleteProduct(productId);
            });
        });
    }

    function addProduct() {
        // Implement add product functionality
    }

    function editProduct(productId) {
        // Implement edit product functionality
    }

    function deleteProduct(productId) {
        const token = sessionStorage.getItem('jwtToken');

        fetch(`/api/admin/products/${productId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                loadProducts();
            })
            .catch(error => console.error('Error deleting product:', error));
    }

    function loadCategories() {
        const token = sessionStorage.getItem('jwtToken');

        fetch('/api/public/categories', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                displayCategories(data.content);
            })
            .catch(error => console.error('Error fetching categories:', error));
    }

    function displayCategories(categories) {
        adminContent.innerHTML = `
            <h2>Manage Categories</h2>
            <button id="addCategoryBtn">Add Category</button>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
<!--                        <th>Description</th>-->
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${categories.map(category => `
                        <tr>
                            <td>${category.categoryId}</td>
                            <td>${category.categoryName}</td>
<!--                            <td>${category.description}</td>-->
                            <td>
                                <button class="editCategoryBtn" data-id="${category.categoryId}">Edit</button>
                                <button class="deleteCategoryBtn" data-id="${category.categoryId}">Delete</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        document.getElementById('addCategoryBtn').addEventListener('click', function () {
            addCategory();
        });

        document.querySelectorAll('.editCategoryBtn').forEach(btn => {
            btn.addEventListener('click', function () {
                const categoryId = this.dataset.id;
                editCategory(categoryId);
            });
        });

        document.querySelectorAll('.deleteCategoryBtn').forEach(btn => {
            btn.addEventListener('click', function () {
                const categoryId = this.dataset.id;
                deleteCategory(categoryId);
            });
        });
    }

    function addCategory() {
        // Implement add category functionality
    }

    function editCategory(categoryId) {
        // Implement edit category functionality
    }

    function deleteCategory(categoryId) {
        const token = sessionStorage.getItem('jwtToken');

        fetch(`/api/admin/categories/${categoryId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                loadCategories();
            })
            .catch(error => console.error('Error deleting category:', error));
    }
});