document.addEventListener('DOMContentLoaded', function () {
    const imageInput = document.getElementById('image');
    const imagePreview = document.getElementById('imagePreview');
    const productEditForm = document.getElementById('productEditForm');

    // 페이지가 로드될 때 제품 데이터를 로드
    const path = window.location.pathname;
    const parts = path.split('/');
    const productId = parts[parts.length - 1];

    if (productId) {
        loadProductData(productId);
    }

    // 기존 상품 데이터를 로드하는 함수
    function loadProductData(productId) {
        axios.get(`https://localhost:8080/api/admin/products/${productId}`)
            .then(response => {
                const product = response.data;
                document.getElementById('name').value = product.productName; // 상품명 설정
                document.getElementById('description').value = product.description; // 설명 설정
                document.getElementById('price').value = product.price; // 가격 설정
                document.getElementById('stockQuantity').value = product.stockQuantity; // 재고 수량 설정
                document.getElementById('category').value = product.category; // 카테고리 설정

                // 이미지가 있으면 미리보기 설정
                if (product.storeFileName) {
                    imagePreview.src = `https://localhost:8080/api/products/images/${product.storeFileName}`;
                    imagePreview.style.display = 'block';
                }
            })
            .catch(error => {
                console.error('상품 데이터를 불러오는 중 오류 발생:', error);
            });
    }

    imageInput.addEventListener('change', function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                imagePreview.src = e.target.result;
                imagePreview.style.display = 'block';
            };
            reader.readAsDataURL(file);
        }
    });

    productEditForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = new FormData();
        const fileInput = document.getElementById('image').files[0];
        const name = document.getElementById('name').value;
        const price = document.getElementById('price').value;
        const stockQuantity = document.getElementById('stockQuantity').value;
        const description = document.getElementById('description').value;
        const category = document.getElementById('category').value;

        const productData = {
            productId: productId, // 추가: 상품 ID를 함께 전송
            name: name,
            price: price,
            stockQuantity: stockQuantity,
            description: description,
            category: category
        };

        if (fileInput) {
            formData.append('image', fileInput);
        }
        formData.append('product', new Blob([JSON.stringify(productData)], {type: "application/json"}));

        axios.patch(`https://localhost:8080/api/products/${productId}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(response => {
                if (response.data.success) {
                    alert('상품이 성공적으로 수정되었습니다.');
                    window.location.href = '/admin/products';
                } else {
                    alert('상품 수정에 실패했습니다: ' + response.data.message);
                }
            })
            .catch(error => {
                alert('오류가 발생했습니다: ' + error.message);
                console.error('Error:', error);
            });
    });
});
