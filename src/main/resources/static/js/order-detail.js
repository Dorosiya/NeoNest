document.addEventListener('DOMContentLoaded', function () {
    const pathParts = window.location.pathname.split('/');
    const orderUid = pathParts[pathParts.length - 1]; // orderUid는 서버 요청에만 사용

    // 서버에서 주문 상세 정보를 가져오는 함수
    function fetchOrderDetails() {
        axios.get(`/api/mypage/orders/${orderUid}`)  // API 엔드포인트에 맞게 수정
            .then(response => {
                const orderDataList = response.data; // orderData는 배열로 받음

                const orderDetails = document.getElementById('order-details');
                const productList = document.getElementById('product-list');

                // 데이터가 존재하는지 확인하고 첫 번째 주문의 공통 정보를 표시
                if (orderDataList.length > 0) {
                    const firstOrder = orderDataList[0]; // 첫 번째 항목에서 공통 정보 사용

                    // 주문 정보 표시 (orderUid와 orderId는 제외)
                    const orderInfo = `
                        <div>
                            <p>주문 날짜: ${new Date(firstOrder.paidAt).toLocaleString()}</p>
                            <p>주문 상태: ${firstOrder.status}</p>
                        </div>
                    `;
                    orderDetails.innerHTML = orderInfo;
                }

                // 주문에 포함된 상품 정보 표시
                orderDataList.forEach(product => {
                    const productItem = document.createElement('div');
                    productItem.className = 'list-group-item';

                    const productDetails = `
                        <div class="product-details">
                            <h5>상품명: ${product.productName}</h5>
                            <img src="https://localhost:8080/api/products/images/${product.productImage}" alt="상품 이미지" class="img-fluid" style="max-width: 150px; margin-top: 10px;"> <!-- 이미지 추가 -->
                            <p>상품 가격: ${product.price.toLocaleString()}원</p>
                            <p>수량: ${product.quantity}</p>
                        </div>
                    `;

                    productItem.innerHTML = productDetails;
                    productList.appendChild(productItem);
                });
            })
            .catch(error => {
                console.error('Error fetching order details:', error);
            });
    }

    fetchOrderDetails();  // 주문 상세 정보 가져오기
});
