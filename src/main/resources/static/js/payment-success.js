document.addEventListener('DOMContentLoaded', function () {
    // URL 경로에서 orderUid를 추출
    const pathParts = window.location.pathname.split('/');
    const orderUid = pathParts[pathParts.length - 1];

    // 서버로부터 결제 및 주문 정보를 가져와서 화면에 표시
    axios.get(`/api/payment/success/${orderUid}`)  // 실제 API 엔드포인트에 맞게 수정
        .then(response => {
            const data = response.data;

            // 주문 번호, 결제 금액, 배송지 등 정보를 화면에 표시
            document.getElementById('order-number').textContent = data.orderId;
            document.getElementById('payment-amount').textContent = data.paymentPrice.toLocaleString() + '원';
            document.getElementById('delivery-address').textContent = data.deliveryAddress;

            // 추가 정보 표시
            document.getElementById('order-date').textContent = new Date(data.orderDate).toLocaleString();  // 주문 날짜 표시
            document.getElementById('delivery-status').textContent = data.deliveryStatus;  // 배송 상태 표시
            document.getElementById('payment-method').textContent = data.paymentMethod;  // 결제 수단 표시

            // 상품 정보는 배열일 경우, 각 상품 정보를 텍스트로 변환하여 표시
            const productDetailsElement = document.getElementById('product-details');
            productDetailsElement.innerHTML = '';  // 기존 내용을 초기화

            if (Array.isArray(data.productDetails)) {
                data.productDetails.forEach(product => {
                    const productInfo = document.createElement('p');
                    productInfo.textContent = `${product.productName} - 수량: ${product.quantity}, 가격: ${product.price.toLocaleString()}원`;
                    productDetailsElement.appendChild(productInfo);
                });
            } else {
                productDetailsElement.textContent = data.productDetails;
            }
        })
        .catch(error => console.error('Error fetching payment success details:', error));
});
