document.addEventListener('DOMContentLoaded', function () {
    const imageInput = document.getElementById('image');
    const imagePreview = document.getElementById('imagePreview');
    const productForm = document.getElementById('productForm');

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

    productForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = new FormData();
        const fileInput = document.getElementById('image').files[0];
        const name = document.getElementById('name').value;
        const price = document.getElementById('price').value;
        const stockQuantity = document.getElementById('stockQuantity').value;
        const description = document.getElementById('description').value;
        const category = document.getElementById('category').value;  // 카테고리 추가

        const productData = {
            name: name,
            price: price,
            stockQuantity: stockQuantity,
            description: description,
            category: category  // 카테고리 추가
        };

        formData.append('image', fileInput);
        formData.append('product', new Blob([JSON.stringify(productData)], { type: "application/json" }));

        axios.post('https://localhost:8080/api/products', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
            .then(response => {
                if (response.data.success) {
                    alert('상품이 성공적으로 등록되었습니다.');
                    window.location.href = '/admin/main';
                } else {
                    alert('상품 등록에 실패했습니다: ' + response.data.message);
                }
            })
            .catch(error => {
                alert('오류가 발생했습니다: ' + error.message);
                console.error('Error:', error);
            });
    });
});
