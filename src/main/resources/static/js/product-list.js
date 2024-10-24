/* product-list.js */

document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category') || 'mouse';  // 기본값으로 'mouse' 설정
    fetchProducts(category, 'newest');  // 기본 정렬 방식 설정
    updateSortButtons('newest');
});

function fetchProducts(category, sort) {
    axios.get(`https://localhost:8080/api/products`, {
        params: { category: category, sort: sort }
    })
        .then(response => {
            const productList = document.getElementById('product-list');
            productList.innerHTML = '';
            response.data.forEach(product => {
                const productItem = document.createElement('div');
                productItem.className = 'col-md-4 product-item';

                productItem.innerHTML = `
                <a href="http://localhost:8080/products/${product.productId}" class="product-link">
                    <img src="https://localhost:8080/api/products/images/${product.image}" alt="${product.name}">
                    <h5>${product.name}</h5>
                    <p class="price">${product.price.toLocaleString()}원</p>
                    <p>${product.description}</p>
                </a>
                <p>평점: ${product.rating || 'N/A'} / 리뷰: ${product.reviews || 'N/A'}</p>
            `;
                productList.appendChild(productItem);
            });
        })
        .catch(error => console.error('Error fetching products:', error));
    updateSortButtons(sort);
}

function updateSortButtons(activeSort) {
    const buttons = document.querySelectorAll('.sort-button');
    buttons.forEach(button => {
        button.classList.remove('active');
    });
    const activeButton = document.querySelector(`.sort-button[onclick="fetchProducts('${activeSort}')"]`);
    if (activeButton) {
        activeButton.classList.add('active');
    }
}
