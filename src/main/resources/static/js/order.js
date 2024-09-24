document.addEventListener('DOMContentLoaded', function () {
    const path = window.location.pathname;
    const parts = path.split('/');
    const orderUid = parts[parts.length - 1];

    let impCode = '';  // 초기 impCode는 빈 값으로 설정
    let orderItems = [];

    // 초기 로딩 시 서버에서 impCode 가져오기
    axios.get('/api/config/impCode')
        .then(response => {
            impCode = response.data.impCode;  // 서버로부터 impCode 가져오기
            initializePayment();  // impCode를 가져온 후 결제 초기화
        })
        .catch(error => {
            console.error('Error fetching impCode:', error);
        });

    function initializePayment() {
        // 서버에서 impCode를 가져온 후 초기화 및 주문 정보를 로드합니다.
        fetchOrderItems(orderUid);
        fetchOrderPaymentInfo(orderUid);
    }

    // 주문 상품 정보를 서버에서 가져오는 함수
    function fetchOrderItems(orderUid) {
        axios.get(`/api/orders/${orderUid}`)
            .then(response => {
                if (response.data.status === 'ING') {
                    alert('이미 결제된 주문입니다.');
                    window.location.href = `/payment/success/${orderUid}`;
                }

                orderItems = response.data.orderProductsDto;
                const orderList = document.getElementById('order-list');

                orderList.innerHTML = '';

                let total = 0;

                orderItems.forEach(item => {
                    const orderItem = document.createElement('div');
                    orderItem.className = 'order-item row align-items-center';
                    const itemTotalPrice = item.price * item.quantity;
                    orderItem.innerHTML = `
                        <div class="col-2">
                            <img src="/api/products/images/${item.image}" alt="상품 이미지" class="img-fluid">
                        </div>
                        <div class="col-6">
                            <h5>${item.description}</h5>
                            <p>수량: ${item.quantity}</p>
                        </div>
                        <div class="col-4 text-right">
                            <p>단가: ${item.price.toLocaleString()}원</p>
                            <p>총 가격: ${itemTotalPrice.toLocaleString()}원</p>
                        </div>
                    `;
                    orderList.appendChild(orderItem);
                    total += itemTotalPrice;
                });

                document.getElementById('total-amount').textContent = total.toLocaleString() + '원';
            })
            .catch(error => {
                console.error('Error fetching order items:', error);
            });
    }

    // 결제 정보를 가져오는 함수
    function fetchOrderPaymentInfo(orderUid) {
        axios.get(`/api/payment/${orderUid}`)
            .then(response => {
                const orderPaymentInfo = response.data;
                window.orderPaymentInfo = {
                    orderUid: orderPaymentInfo.orderUid,
                    itemName: orderPaymentInfo.orderName,
                    paymentPrice: orderPaymentInfo.paymentPrice,
                    buyerName: orderPaymentInfo.buyerName,
                    buyerEmail: orderPaymentInfo.buyerEmail,
                    buyerAddress: orderPaymentInfo.buyerAddress,
                    buyerPhone: orderPaymentInfo.buyerPhone,
                    buyerPostcode: orderPaymentInfo.postcode,
                    paymentItems: orderItems.map(item => ({
                        orderProductId: item.orderProductId,
                        productId: item.productId,
                        quantity: item.quantity,
                        price: item.price
                    }))
                };
            })
            .catch(error => {
                console.error('Error fetching order payment info:', error);
            });
    }

    // 결제 요청 함수
    function requestPay() {
        const { orderUid, itemName, paymentPrice, buyerName, buyerEmail, buyerPhone, buyerAddress, buyerPostcode, paymentItems } = window.orderPaymentInfo;

        const userName = document.getElementById('userName').value;
        const userPhone = document.getElementById('userPhone').value;
        const postcode = document.getElementById('postcode').value;
        const address = document.getElementById('address').value;
        const detailAddress = document.getElementById('detailAddress').value;
        const extraAddress = document.getElementById('extraAddress').value;
        const deliveryRequest = document.getElementById('delivery-request').value;

        const fullAddress = `${address} ${detailAddress} ${extraAddress}`;
        const payMethod = 'card';

        IMP.init(impCode);

        IMP.request_pay({
            pg: 'html5_inicis.INIpayTest',
            pay_method: payMethod,
            merchant_uid: orderUid,
            name: itemName,
            amount: paymentPrice,
            buyer_email: buyerEmail,
            buyer_name: buyerName,
            buyer_tel: buyerPhone,
            buyer_addr: buyerAddress,
            buyer_postcode: buyerPostcode
        }, function (rsp) {
            if (rsp.success) {
                jQuery.ajax({
                    url: "/api/payment",
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    data: JSON.stringify({
                        "paymentUid": rsp.imp_uid,
                        "orderUid": rsp.merchant_uid,
                        "payMethod": payMethod,
                        "recipientName": userName,
                        "phoneNumber": userPhone,
                        "postcode": postcode,
                        "address": fullAddress,
                        "deliveryRequest": deliveryRequest,
                        "paymentItems": paymentItems
                    })
                }).done(function (response) {
                    alert('결제 완료!' + rsp);
                    window.location.href = `/payment/success/${rsp.merchant_uid}`;
                });
            } else {
                alert('결제 실패!' + rsp);
                window.location.href = "/payment-fail";
            }
        });
    }

    document.querySelector('.btn-block').addEventListener('click', requestPay);

    function DaumPostcode() {
        new daum.Postcode({
            oncomplete: function (data) {
                let addr = '';
                let extraAddr = '';

                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }

                if (data.userSelectedType === 'R') {
                    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName !== '' && data.apartment === 'Y') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if (extraAddr !== '') {
                        extraAddr = ' (' + extraAddr + ')';
                    }
                }

                document.getElementById('postcode').value = data.zonecode;
                document.getElementById("address").value = addr;
                document.getElementById("extraAddress").value = extraAddr;
                document.getElementById("detailAddress").focus();
            }
        }).open();
    }

    window.DaumPostcode = DaumPostcode;
});
