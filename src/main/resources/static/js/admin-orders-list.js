document.addEventListener('DOMContentLoaded', function() {
    const statusFilter = document.getElementById('statusFilter');
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const filterBtn = document.getElementById('filterBtn');
    const orderList = document.getElementById('orderList');

    // 필터 적용 버튼 클릭 시 서버에서 데이터를 가져오는 함수
    filterBtn.addEventListener('click', function() {
        const status = statusFilter.value === "전체" ? null : statusFilter.value; // "전체"일 때 null로 처리
        const startDate = startDateInput.value;
        const endDate = endDateInput.value;

        axios.get(`/api/admin/orders`, {
            params: {
                status: status,
                startDate: startDate,
                endDate: endDate
            }
        })
            .then(response => {
                const orders = response.data;
                orderList.innerHTML = ''; // 기존 주문 목록 초기화

                orders.forEach(order => {
                    const orderRow = document.createElement('tr');
                    orderRow.innerHTML = `
                        <td>${order.orderId}</td>
                        <td>${new Date(order.orderDate).toLocaleDateString()}</td>
                        <td>${order.customerName}</td>
                        <td>${order.orderStatusDescription}</td>
                        <td>${order.totalPrice.toLocaleString()}원</td>
                        <td id="action-buttons-${order.orderId}">
                            ${getOrderActionButtons(order)}
                        </td>
                        <td>
                            <button class="btn btn-info" onclick="location.href='/admin/orders/${order.orderId}'">상세 보기</button>
                        </td>
                    `;
                    orderList.appendChild(orderRow);
                });
            })
            .catch(error => {
                console.error('주문 목록을 가져오는 중 오류 발생:', error);
            });
    });

    // 주문 상태에 따른 주문 상태 변경 버튼 표시 로직
    function getOrderActionButtons(order) {
        if (order.orderStatusDescription === '결제 준비') {
            return `
                <button class="btn btn-danger btn-action" onclick="changeOrderStatus(${order.orderId}, 'CANCEL')">주문 취소</button>
            `;
        } else if (order.orderStatusDescription === '결제 완료') {
            return `
                <button class="btn btn-success btn-action" onclick="changeOrderStatus(${order.orderId}, 'COMPLETE')">주문 완료</button>
            `;
        } else {
            return ''; // 결제 준비 상태가 아닌 경우 버튼을 표시하지 않음
        }
    }

    // 주문 상태 변경 요청을 서버로 전송
    window.changeOrderStatus = function(orderId, status) {
        axios.patch(`/api/admin/orders/${orderId}/orderStatus`, { status: status })  // orderId를 사용하고, URL 경로를 수정
            .then(response => {
                alert('주문 상태가 변경되었습니다.');
                location.reload();
            })
            .catch(error => {
                alert('주문 상태 변경에 실패했습니다.');
                console.error(error);
            });
    };

    // 페이지 로드 시 초기 주문 목록 로드
    fetchOrderList();

    // 초기 주문 목록을 로드하는 함수
    function fetchOrderList() {
        axios.get('/api/admin/orders')
            .then(response => {
                const orders = response.data;
                orderList.innerHTML = ''; // 기존 주문 목록 초기화

                orders.forEach(order => {
                    const orderRow = document.createElement('tr');
                    orderRow.innerHTML = `
                        <td>${order.orderId}</td>
                        <td>${new Date(order.orderDate).toLocaleDateString()}</td>
                        <td>${order.customerName}</td>
                        <td>${order.orderStatusDescription}</td>
                        <td>${order.totalPrice.toLocaleString()}원</td>
                        <td id="action-buttons-${order.orderId}">
                            ${getOrderActionButtons(order)}
                        </td>
                        <td>
                            <button class="btn btn-info" onclick="location.href='/admin/orders/${order.orderId}'">상세 보기</button>
                        </td>
                    `;
                    orderList.appendChild(orderRow);
                });
            })
            .catch(error => {
                console.error('주문 목록을 가져오는 중 오류 발생:', error);
            });
    }
});
