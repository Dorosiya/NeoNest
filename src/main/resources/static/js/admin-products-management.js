document.addEventListener('DOMContentLoaded', function () {
    // 상품 목록을 서버에서 가져오는 함수
    function fetchProductList() {
        axios.get('/api/admin/products')
            .then(response => {
                const products = response.data;  // 서버로부터 상품 목록 데이터를 받음
                const productList = document.getElementById('product-list');
                productList.innerHTML = '';  // 기존 목록을 비움

                // 상품 목록을 순회하며 각 상품을 테이블에 추가
                products.forEach(product => {
                    const productRow = document.createElement('tr');

                    const productDetails = `
                        <td>${product.productId}</td>
                        <td>${product.productName}</td>
                        <td>${parseInt(product.productPrice).toLocaleString()}원</td>
                        <td>${product.productStockQuantity}</td>
                        <td><img src="https://localhost:8080/api/products/images/${product.productImage}" alt="${product.productName}" style="width: 50px; height: auto;"></td>
                        <td><button class="btn btn-primary btn-sm edit-btn" data-product-id="${product.productId}">수정</button></td>
                    `;
                    productRow.innerHTML = productDetails;
                    productList.appendChild(productRow);
                });

                // 수정 버튼 이벤트 리스너 추가
                attachEditButtonListeners();
            })
            .catch(error => {
                console.error('Error fetching product list:', error);
            });
    }

    // 수정 버튼 클릭 시 상품 수정 페이지로 이동
    function attachEditButtonListeners() {
        const editButtons = document.querySelectorAll('.edit-btn');

        editButtons.forEach(button => {
            button.addEventListener('click', function () {
                const productId = button.getAttribute('data-product-id');
                window.location.href = `/admin/products/edit/${productId}`;  // 상품 수정 페이지로 이동
            });
        });
    }

    fetchProductList();  // 페이지 로딩 시 상품 목록 가져오기
});
