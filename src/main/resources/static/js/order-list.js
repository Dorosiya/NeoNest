document.addEventListener('DOMContentLoaded', function () {
    // 서버에서 주문 목록을 가져오는 함수
    function fetchOrderList() {
        axios.get('/api/mypage/orders')
            .then(response => {
                const orders = response.data.orderListPageDtoList;  // orderListPageDtoList 배열 가져오기
                const orderList = document.getElementById('order-list');
                orderList.innerHTML = ''; // 기존 항목 제거

                // 주문 목록을 순회하며 각 주문 항목을 화면에 추가
                orders.forEach(order => {
                    const orderItem = document.createElement('div');
                    orderItem.className = 'list-group-item';

                    // 주문 상세 정보와 이미지 추가
                    const orderDetails = `
                        <div class="order-details">
                            <h5>상품명: ${order.productName}</h5>
                            <img src="https://localhost:8080/api/products/images/${order.productImage}" alt="상품 이미지" class="img-fluid" style="max-width: 150px; margin-top: 10px;"> <!-- 이미지 추가 -->
                            <p>주문 날짜: ${new Date(order.orderDate).toLocaleString()}</p>
                            <p>상품 가격: ${order.productPrice.toLocaleString()}원</p>
                            <p>배송 상태: ${order.status}</p>
                        </div>
                    `;

                    // 주문 상세 보기 버튼 (상세 페이지로 이동하도록 설정)
                    const detailButton = `<button class="btn-detail" onclick="location.href='/mypage/orders/${response.data.orderUid}'">상세 보기</button>`;

                    orderItem.innerHTML = orderDetails + detailButton;
                    orderList.appendChild(orderItem);
                });
            })
            .catch(error => {
                console.error('Error fetching order list:', error);
            });
    }

    fetchOrderList();  // 주문 목록 가져오기
});
