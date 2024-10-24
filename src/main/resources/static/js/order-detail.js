/* order-detail.js */

document.addEventListener('DOMContentLoaded', function () {
    const pathParts = window.location.pathname.split('/');
    const orderUid = pathParts[pathParts.length - 1]; // orderUid는 서버 요청에만 사용

    const orderInfoList = document.getElementById('orderInfo');
    const productList = document.getElementById('productList');
    const deliveryInfoList = document.getElementById('deliveryInfo');

    // 서버에서 주문 상세 정보를 가져오는 함수
    function fetchOrderDetails() {
        axios.get(`/api/mypage/orders/${orderUid}`)  // API 엔드포인트에 맞게 수정
            .then(response => {
                const orderDataList = response.data;

                if (!Array.isArray(orderDataList) || orderDataList.length === 0) {
                    console.error('주문 정보가 유효하지 않습니다:', orderDataList);
                    return;
                }

                const order = orderDataList[0];

                // 주문 정보 표시
                orderInfoList.innerHTML = `
                    <li class="list-group-item"><strong>주문 날짜:</strong> ${new Date(order.paidAt).toLocaleString()}</li>
                    <li class="list-group-item"><strong>주문 상태:</strong> ${order.orderStatusDescription}</li>
                `;

                // 상품 정보 표시
                productList.innerHTML = ''; // 초기화
                orderDataList.forEach(product => {
                    const isReviewed = product.isReviewed; // 서버에서 받은 리뷰 여부
                    const productRow = document.createElement('tr');
                    productRow.innerHTML = `
                        <td><img src="/api/products/images/${product.productImage}" alt="${product.productName}" style="width: 50px; height: 50px;"></td>
                        <td>${product.productName}</td>
                        <td>${product.price ? product.price.toLocaleString() : 'N/A'}원</td>
                        <td>${product.quantity}</td>
                        <td>${product.price && product.quantity ? (product.price * product.quantity).toLocaleString() : 'N/A'}원</td>
                        <td>
                            <button class="btn ${isReviewed ? 'btn-secondary' : 'btn-primary'} review-btn" data-product-id="${product.productId}" data-order-uid="${orderUid}">
                                ${isReviewed ? '리뷰 보기' : '리뷰 작성'}
                            </button>
                        </td>
                    `;
                    productList.appendChild(productRow);
                });

                // 리뷰 작성 버튼 클릭 이벤트 추가
                document.querySelectorAll('.review-btn').forEach(button => {
                    button.addEventListener('click', function () {
                        const productId = this.getAttribute('data-product-id');
                        const orderUid = this.getAttribute('data-order-uid');
                        window.location.href = `/mypage/reviews/${orderUid}/${productId}`; // 리뷰 작성 또는 보기 페이지로 이동
                    });
                });

                // 배송 정보 표시
                deliveryInfoList.innerHTML = `
                    <li class="list-group-item"><strong>수령인 이름:</strong> ${order.recipientName || 'N/A'}</li>
                    <li class="list-group-item"><strong>배송 상태:</strong> ${order.deliveryStatusDescription || 'N/A'}</li>
                    <li class="list-group-item"><strong>송장 번호:</strong> ${order.trackingNumber || 'N/A'}</li>
                    <li class="list-group-item"><strong>배송 주소:</strong> ${order.deliveryAddress || 'N/A'}</li>
                    <li class="list-group-item"><strong>배송 요청 사항:</strong> ${order.deliveryRequest || 'N/A'}</li>
                `;
            })
            .catch(error => {
                console.error('주문 정보를 불러오는 중 오류 발생:', error);
            });
    }

    fetchOrderDetails();  // 주문 상세 정보 가져오기
});
