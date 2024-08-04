document.addEventListener('DOMContentLoaded', function() {
    const sortByElement = document.getElementById('sortBy');
    const sortOrderElement = document.getElementById('sortOrder');
    const clearFiltersButton = document.getElementById('clearFilters');
    const applyCategoryFilterButton = document.getElementById('applyCategoryFilter');
    const searchInputElement = document.getElementById('searchInput');

    let allProducts = [];
    let currentPage = 0;
    const pageSize = 12;
    let selectedCategory = "";
    let filteredProducts = [];

    if (sortByElement) {
        sortByElement.addEventListener('change', () => applyFilters());
    }

    if (sortOrderElement) {
        sortOrderElement.addEventListener('change', () => applyFilters());
    }

    if (clearFiltersButton) {
        clearFiltersButton.addEventListener('click', clearFilters);
    }

    if (applyCategoryFilterButton) {
        applyCategoryFilterButton.addEventListener('click', applyCategoryFilter);
    }

    if (searchInputElement) {
        searchInputElement.addEventListener('input', applyFilters);
    }

    if (document.querySelector(".products-list")) {
        fetchAllProducts();
        fetchCategories();
    }

    function fetchAllProducts() {
        fetch(`/api/public/products`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                allProducts = data.content;
                filteredProducts = allProducts;
                applyFilters();
            })
            .catch(error => console.error('Error fetching products:', error));
    }

    function fetchCategories() {
        fetch(`/api/public/categories`)
            .then(response => response.json())
            .then(data => {
                const categoryFilter = document.getElementById('categoryFilter');
                categoryFilter.innerHTML = '<option value="">All Categories</option>';
                data.content.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.categoryId;
                    option.textContent = category.categoryName;
                    categoryFilter.appendChild(option);
                });
            })
            .catch(error => console.error('Error fetching categories:', error));
    }

    function applyFilters() {
        const searchInput = searchInputElement.value.toLowerCase();
        const sortBy = sortByElement ? sortByElement.value : 'productName';
        const sortOrder = sortOrderElement ? sortOrderElement.value : 'asc';

        filteredProducts = allProducts.filter(product => {
            const matchesSearch = product.productName.toLowerCase().includes(searchInput) ||
                product.description.toLowerCase().includes(searchInput);
            const matchesCategory = selectedCategory === "" || product.category.categoryId == selectedCategory;
            return matchesSearch && matchesCategory;
        });

        filteredProducts.sort((a, b) => {
            let comparison = 0;
            if (a[sortBy] > b[sortBy]) {
                comparison = 1;
            } else if (a[sortBy] < b[sortBy]) {
                comparison = -1;
            }
            return sortOrder === 'asc' ? comparison : -comparison;
        });

        displayProducts();
    }

    function displayProducts() {
        const productsList = document.querySelector('.products-list');
        productsList.innerHTML = '';

        const start = currentPage * pageSize;
        const end = start + pageSize;
        const productsToDisplay = filteredProducts.slice(start, end);

        productsToDisplay.forEach(product => {
            const productItem = document.createElement('div');
            productItem.className = 'product-item';
            productItem.innerHTML = `
            <img src="${product.image}" alt="${product.productName}">
            <h3>${product.productName}</h3>
            <p class="price">${product.price}</p>
            <p class="special-price">${product.specialPrice}</p>
            <p>${product.description.split(' ').slice(0, 3).join(' ')}...</p>
            ${product.discount > 0 ? `<div class="discount-stamp">-${product.discount}%</div>` : ''}
        `;
            productItem.addEventListener('click', () => {
                localStorage.setItem('selectedProduct', JSON.stringify(product));
                window.location.href = '/products/product';
            });
            productsList.appendChild(productItem);
        });

        updatePagination();
    }

    function updatePagination() {
        const totalPages = Math.ceil(filteredProducts.length / pageSize);
        const pageInfo = document.getElementById('pageInfo');
        pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages}`;

        document.getElementById('prevPage').disabled = currentPage === 0;
        document.getElementById('nextPage').disabled = currentPage >= totalPages - 1;
    }

    window.prevPage = function() {
        if (currentPage > 0) {
            currentPage--;
            displayProducts();
        }
    }

    window.nextPage = function() {
        if (currentPage < Math.ceil(filteredProducts.length / pageSize) - 1) {
            currentPage++;
            displayProducts();
        }
    }

    function applyCategoryFilter() {
        const categoryFilter = document.getElementById('categoryFilter');
        selectedCategory = categoryFilter.value;
        fetch(`/api/public/categories/${selectedCategory}/products`)
            .then(response => response.json())
            .then(data => {
                filteredProducts = data.content;
                displayProducts();
            })
            .catch(error => console.error('Error fetching category products:', error));
    }

    function clearFilters() {
        document.getElementById('searchInput').value = '';
        sortByElement.value = 'productName';
        sortOrderElement.value = 'asc';
        selectedCategory = "";
        document.getElementById('categoryFilter').value = "";
        currentPage = 0;
        filteredProducts = allProducts;
        applyFilters(); // Ensure filters are applied
    }
});