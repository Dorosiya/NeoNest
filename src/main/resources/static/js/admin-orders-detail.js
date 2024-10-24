/* admin-orders-detail.js */

document.addEventListener('DOMContentLoaded', function() {
    const orderDetailContainer = document.getElementById('orderDetail');
    const orderInfoList = document.getElementById('orderInfo');
    const customerInfoList = document.getElementById('customerInfo');
    const productList = document.getElementById('productList');
    const deliveryInfoList = document.getElementById('deliveryInfo');
    const orderStatusContainer = document.getElementById('orderStatusContainer'); // 주문 상태 버튼을 담을 컨테이너

    const path = window.location.pathname;
    const parts = path.split('/');
    const orderId = parts[parts.length - 1];

    if (orderId) {
        loadOrderDetails(orderId);
    }

    // 주문 상세 정보 로드 함수
    function loadOrderDetails(orderId) {
        axios.get(`/api/admin/orders/${orderId}`)
            .then(response => {
                const order = response.data;
                populateOrderInfo(order);
                populateCustomerInfo(order);
                populateProductList(order.products); // 상품 목록 추가
                populateDeliveryInfo(order); // 배송 정보 추가 (order 객체 자체로 전달)
                setupOrderStatusButtons(order); // 주문 상태에 따라 버튼 설정
            })
            .catch(error => {
                console.error('주문 정보를 불러오는 중 오류 발생:', error);
            });
    }

    // 주문 정보 표시
    function populateOrderInfo(order) {
        orderInfoList.innerHTML = `
            <li class="list-group-item"><strong>주문 번호:</strong> ${order.orderId}</li>
            <li class="list-group-item"><strong>주문 날짜:</strong> ${new Date(order.orderDate).toLocaleString()}</li>
            <li class="list-group-item"><strong>주문 상태:</strong> ${order.orderStatusDescription}</li>
            <li class="list-group-item"><strong>결제 금액:</strong> ${order.totalPrice.toLocaleString()}원</li>
        `;
    }

    // 고객 정보 표시
    function populateCustomerInfo(order) {
        customerInfoList.innerHTML = `
            <li class="list-group-item"><strong>주문자 이름:</strong> ${order.customerName}</li>
            <li class="list-group-item"><strong>이메일:</strong> ${order.customerEmail}</li>
            <li class="list-group-item"><strong>전화번호:</strong> ${order.customerPhone}</li>
        `;
    }

    // 상품 정보 표시
    function populateProductList(products) {
        productList.innerHTML = ''; // 초기화
        products.forEach(product => {
            const productRow = document.createElement('tr');
            productRow.innerHTML = `
                <td><img src="/api/products/images/${product.storeFileName}" alt="${product.productName}" style="width: 50px; height: 50px;"></td>
                <td>${product.productName}</td>
                <td>${product.productPrice.toLocaleString()}원</td>
                <td>${product.quantity}</td>
                <td>${(product.productPrice * product.quantity).toLocaleString()}원</td>
            `;
            productList.appendChild(productRow);
        });
    }

    // 배송 정보 표시
    function populateDeliveryInfo(order) {
        deliveryInfoList.innerHTML = `
            <li class="list-group-item"><strong>수령인 이름:</strong> ${order.recipientName}</li>
            <li class="list-group-item"><strong>배송 상태:</strong> ${order.deliveryStatusDescription}</li>
            <li class="list-group-item"><strong>송장 번호:</strong> ${order.trackingNumber}</li>
            <li class="list-group-item"><strong>배송 요청 사항:</strong> ${order.deliveryRequest}</li>
            <li class="list-group-item"><strong>배송 주소:</strong> ${order.deliveryAddress}</li>
        `;
    }

    // 주문 상태에 따라 주문 상태 변경 버튼 설정
    function setupOrderStatusButtons(order) {
        orderStatusContainer.innerHTML = ''; // 버튼 초기화

        if (order.orderStatusDescription === '결제 준비') {
            // 결제 준비 상태일 때 '주문 취소' 버튼
            const cancelBtn = document.createElement('button');
            cancelBtn.className = 'btn btn-danger';
            cancelBtn.textContent = '주문 취소';
            cancelBtn.addEventListener('click', function() {
                changeOrderStatus(orderId, 'CANCEL');
            });
            orderStatusContainer.appendChild(cancelBtn);
        } else if (order.orderStatusDescription === '결제 완료') {
            // 결제 완료 상태일 때 '주문 완료' 버튼
            const completeBtn = document.createElement('button');
            completeBtn.className = 'btn btn-success';
            completeBtn.textContent = '주문 완료';
            completeBtn.addEventListener('click', function() {
                changeOrderStatus(orderId, 'COMPLETE');
            });
            orderStatusContainer.appendChild(completeBtn);
        }
    }

    // 주문 상태 변경 요청을 서버로 전송
    function changeOrderStatus(orderId, status) {
        axios.patch(`/api/admin/orders/${orderId}/orderStatus`, { status: status })
            .then(response => {
                alert('주문 상태가 변경되었습니다.');
                loadOrderDetails(orderId); // 변경된 정보 다시 로드
            })
            .catch(error => {
                alert('주문 상태 변경에 실패했습니다.');
                console.error('주문 상태 변경 중 오류 발생:', error);
            });
    }
});
