document.addEventListener('DOMContentLoaded', function() {
    const categoriesList = document.querySelector('.categories-list');
    const productsList = document.querySelector('.products-list');
    const prevCategoriesButton = document.getElementById('prevCategories');
    const nextCategoriesButton = document.getElementById('nextCategories');

    let allCategories = [];
    let currentCategoryPage = 0;
    const categoriesPerPage = 4;

    if (categoriesList && productsList) {
        fetchCategories();
    }

    prevCategoriesButton.addEventListener('click', () => {
        if (currentCategoryPage > 0) {
            currentCategoryPage--;
            displayCategories();
        }
    });

    nextCategoriesButton.addEventListener('click', () => {
        if ((currentCategoryPage + 1) * categoriesPerPage < allCategories.length) {
            currentCategoryPage++;
            displayCategories();
        }
    });

    function fetchCategories() {
        fetch(`/api/public/categories`)
            .then(response => response.json())
            .then(data => {
                allCategories = data.content;
                displayCategories();
            })
            .catch(error => console.error('Error fetching categories:', error));
    }

    function displayCategories() {
        categoriesList.innerHTML = '';

        const start = currentCategoryPage * categoriesPerPage;
        const end = start + categoriesPerPage;
        const categoriesToDisplay = allCategories.slice(start, end);

        categoriesToDisplay.forEach(category => {
            const categoryItem = document.createElement('div');
            categoryItem.className = 'category-item';
            categoryItem.textContent = category.categoryName;
            categoryItem.addEventListener('click', () => fetchProductsByCategory(category.categoryId));
            categoriesList.appendChild(categoryItem);
        });

        prevCategoriesButton.disabled = currentCategoryPage === 0;
        nextCategoriesButton.disabled = (currentCategoryPage + 1) * categoriesPerPage >= allCategories.length;
    }

    function fetchProductsByCategory(categoryId) {
        fetch(`/api/public/categories/${categoryId}/products`)
            .then(response => response.json())
            .then(data => {
                displayProducts(data.content);
            })
            .catch(error => console.error('Error fetching products:', error));
    }

    function displayProducts(products) {
        productsList.innerHTML = '';

        products.forEach(product => {
            const productItem = document.createElement('div');
            productItem.className = 'product-item';
            productItem.innerHTML = `
                <img src="/images/${product.image}" alt="${product.productName}">
                <h3>${product.productName}</h3>
                ${product.discount > 0 ? `<p class="price">${product.price}</p>`
                : `<p class="price" style="color: black; text-decoration: none">${product.price}</p>` }
                ${product.discount >0 ?`<p class="special-price">${product.specialPrice}</p>` : ''}
                <p>${product.description.split(' ').slice(0, 3).join(' ')}...</p>
                ${product.discount > 0 ? `<div class="discount-stamp">-${product.discount}%</div>` : ''}
            `;
            productItem.addEventListener('click', () => {
                localStorage.setItem('selectedProduct', JSON.stringify(product));
                window.location.href = '/products/product';
            });
            productsList.appendChild(productItem);
        });
    }
});
