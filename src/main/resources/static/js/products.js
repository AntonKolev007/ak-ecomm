document.addEventListener("DOMContentLoaded", function() {
    const sortByElement = document.getElementById('sortBy');
    const sortOrderElement = document.getElementById('sortOrder');
    const clearFiltersButton = document.getElementById('clearFilters');
    const applyCategoryFilterButton = document.getElementById('applyCategoryFilter');

    let allProducts = [];
    let currentPage = 0;
    const pageSize = 12;
    let selectedCategories = [];

    if (sortByElement) {
        sortByElement.addEventListener('change', () => displayProducts(0));
    }

    if (sortOrderElement) {
        sortOrderElement.addEventListener('change', () => displayProducts(0));
    }

    if (clearFiltersButton) {
        clearFiltersButton.addEventListener('click', clearFilters);
    }

    if (applyCategoryFilterButton) {
        applyCategoryFilterButton.addEventListener('click', applyCategoryFilter);
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
                displayProducts(0);
            })
            .catch(error => console.error('Error fetching products:', error));
    }

    function fetchCategories() {
        fetch(`/api/public/categories`)
            .then(response => response.json())
            .then(data => {
                const categoryList = document.getElementById('categoryList');
                categoryList.innerHTML = data.content.map(category => `
                    <div>
                        <label>
                            <input type="checkbox" value="${category.categoryId}" class="category-checkbox">
                            ${category.categoryName}
                        </label>
                    </div>
                `).join('');
            })
            .catch(error => console.error('Error fetching categories:', error));
    }

    function displayProducts(page) {
        currentPage = page;
        const productsList = document.querySelector('.products-list');
        productsList.innerHTML = '';

        let filteredProducts = [...allProducts];

        const sortBy = sortByElement ? sortByElement.value : 'productName';
        const sortOrder = sortOrderElement ? sortOrderElement.value : 'asc';

        filteredProducts = filteredProducts.filter(product => {
            if (selectedCategories.length === 0) return true;
            return selectedCategories.includes(product.category.categoryId);
        });

        filteredProducts.sort((a, b) => {
            if (a[sortBy] < b[sortBy]) return sortOrder === 'asc' ? -1 : 1;
            if (a[sortBy] > b[sortBy]) return sortOrder === 'asc' ? 1 : -1;
            return 0;
        });

        const start = page * pageSize;
        const end = start + pageSize;
        const paginatedProducts = filteredProducts.slice(start, end);

        paginatedProducts.forEach(product => {
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
            productsList.appendChild(productItem);
        });

        updatePagination(filteredProducts.length);
    }

    function updatePagination(totalItems) {
        const totalPages = Math.ceil(totalItems / pageSize);
        const pageInfo = document.getElementById('pageInfo');
        pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages}`;

        document.querySelector('.pagination button[onclick="prevPage()"]').disabled = currentPage === 0;
        document.querySelector('.pagination button[onclick="nextPage()"]').disabled = currentPage >= totalPages - 1;
    }

    window.prevPage = function() {
        if (currentPage > 0) {
            displayProducts(currentPage - 1);
        }
    }

    window.nextPage = function() {
        displayProducts(currentPage + 1);
    }

    window.searchProducts = function() {
        const searchInput = document.getElementById('searchInput').value.trim().toLowerCase();
        const filteredProducts = allProducts.filter(product => product.productName.toLowerCase().includes(searchInput));
        displayFilteredProducts(filteredProducts);
    }

    function displayFilteredProducts(filteredProducts) {
        const productsList = document.querySelector('.products-list');
        productsList.innerHTML = '';

        filteredProducts.forEach(product => {
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
            productsList.appendChild(productItem);
        });

        updatePagination(filteredProducts.length);
    }

    function applyCategoryFilter() {
        const categoryCheckboxes = document.querySelectorAll('.category-checkbox:checked');
        selectedCategories = Array.from(categoryCheckboxes).map(cb => parseInt(cb.value));
        displayProducts(0);
    }

    function clearFilters() {
        document.getElementById('searchInput').value = '';
        sortByElement.value = 'productName';
        sortOrderElement.value = 'asc';
        selectedCategories = [];
        const categoryCheckboxes = document.querySelectorAll('.category-checkbox');
        categoryCheckboxes.forEach(cb => cb.checked = false);
        displayProducts(0);
    }
});