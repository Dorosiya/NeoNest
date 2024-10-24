document.addEventListener('DOMContentLoaded', function () {
    let selectedCartId = null;
    let selectedProductId = null;

    // 서버에서 장바구니 정보를 가져오는 함수
    function fetchCartItems() {
        axios.get('https://localhost:8080/api/cart')
            .then(response => {
                const cartItems = response.data;
                const cartContainer = document.querySelector('.col-md-8');

                // 기존 항목을 제거
                cartContainer.innerHTML = '';

                cartItems.forEach(item => {
                    // 각 장바구니 항목을 동적으로 생성
                    const cartItem = document.createElement('div');
                    cartItem.className = 'cart-item row align-items-center';
                    cartItem.dataset.cartId = item.cartId;
                    cartItem.dataset.productId = item.productId;
                    cartItem.dataset.price = item.price;
                    cartItem.dataset.quantity = item.quantity; // 현재 수량을 저장
                    cartItem.innerHTML = `
                        <div class="col-1">
                            <input type="checkbox" class="cart-checkbox">
                        </div>
                        <div class="col-2">
                            <img src="https://localhost:8080/api/products/images/${item.image}" alt="상품 이미지" class="img-fluid">
                        </div>
                        <div class="col-4">
                            <h5>${item.name}</h5>
                            <p>판매가: ${item.price.toLocaleString()}원</p>
                            <p>수량: ${item.quantity}</p>
                        </div>
                        <div class="col-3 text-right">
                            <button class="btn btn-primary btn-sm edit-btn">주문 수정</button>
                        </div>
                        <div class="col-2 text-right">
                            <button class="btn btn-danger btn-sm delete-btn">X</button>  <!-- 삭제 버튼 -->
                        </div>
                    `;

                    cartContainer.appendChild(cartItem);
                });

                attachEventListeners();
                updateTotal();
            })
            .catch(error => {
                console.error('Error fetching cart items:', error);
            });
    }

    // 체크박스 및 수량 변경 시 총액 계산에 대한 이벤트 리스너를 추가
    function attachEventListeners() {
        const checkboxes = document.querySelectorAll('.cart-checkbox');
        const editButtons = document.querySelectorAll('.edit-btn');
        const deleteButtons = document.querySelectorAll('.delete-btn');  // 삭제 버튼 이벤트 리스너 추가

        checkboxes.forEach(checkbox => {
            checkbox.addEventListener('change', updateTotal);
        });

        editButtons.forEach(button => {
            button.addEventListener('click', function () {
                const cartItem = button.closest('.cart-item');
                selectedCartId = cartItem.dataset.cartId;
                selectedProductId = cartItem.dataset.productId;
                const currentQuantity = parseInt(cartItem.dataset.quantity);
                const itemPrice = parseInt(cartItem.dataset.price);

                document.getElementById('modal-quantity').value = currentQuantity;
                document.getElementById('modal-total-price').textContent = (currentQuantity * itemPrice).toLocaleString() + '원';
                document.getElementById('current-quantity').textContent = currentQuantity;
                document.getElementById('modal-item-name').textContent = cartItem.querySelector('h5').textContent;
                document.getElementById('modal-image').src = cartItem.querySelector('img').src;

                $('#editModal').modal('show');
            });
        });

        // 삭제 버튼 이벤트 리스너 추가
        deleteButtons.forEach(button => {
            button.addEventListener('click', function () {
                const cartItem = button.closest('.cart-item');
                const cartId = cartItem.dataset.cartId;

                deleteCartItem(cartId);  // cartId만 넘겨 삭제 함수 호출
            });
        });
    }

    function updateTotal() {
        const checkboxes = document.querySelectorAll('.cart-checkbox');
        const totalAmount = document.getElementById('total-amount');
        const finalTotal = document.getElementById('final-total');

        let total = 0;

        checkboxes.forEach(checkbox => {
            if (checkbox.checked) {
                const item = checkbox.closest('.cart-item');
                const price = parseInt(item.querySelector('p').textContent.replace('판매가: ', '').replace('원', '').replace(/,/g, ''));
                const quantity = parseInt(item.dataset.quantity);
                total += price * quantity;
            }
        });

        totalAmount.textContent = total.toLocaleString() + '원';
        finalTotal.textContent = total.toLocaleString() + '원';
    }

    function saveChanges() {
        const newQuantity = parseInt(document.getElementById('modal-quantity').value);
        const memberId = getMemberIdFromToken(); // JWT에서 memberId 추출

        // 서버로 전송할 데이터
        const CartUpdateDto = {
            memberId: memberId, // memberId 추가
            cartId: selectedCartId,
            productId: selectedProductId,
            quantity: newQuantity
        };

        axios.patch('https://localhost:8080/api/cart', CartUpdateDto)
            .then(response => {
                $('#editModal').modal('hide');
                fetchCartItems(); // 업데이트 후 장바구니를 다시 로드하여 최신 상태로 갱신
            })
            .catch(error => {
                console.error('Error updating cart item:', error);
            });
    }

    // cartId만을 Path Variable로 전달하여 삭제 요청을 보냅니다.
    function deleteCartItem(cartId) {
        axios.delete(`https://localhost:8080/api/cart/${cartId}`)  // Path Variable로 cartId만 전송
            .then(response => {
                alert('장바구니에서 항목이 삭제되었습니다.');
                fetchCartItems();  // 삭제 후 장바구니를 다시 로드하여 최신 상태로 갱신
            })
            .catch(error => {
                console.error('Error deleting cart item:', error);
            });
    }

    function submitOrder() {
        const checkboxes = document.querySelectorAll('.cart-checkbox:checked'); // 선택된 체크박스들
        const orderItems = [];

        // 선택된 상품이 없으면 경고 메시지 표시 후 함수 종료
        if (checkboxes.length === 0) {
            alert('최소 1개의 상품을 선택해주세요.');
            return; // 주문 처리 중단
        }

        checkboxes.forEach(checkbox => {
            const item = checkbox.closest('.cart-item');
            const cartId = item.dataset.cartId;
            const productId = item.dataset.productId;
            const quantity = parseInt(item.dataset.quantity);

            orderItems.push({
                cartId: cartId,
                productId: productId,
                quantity: quantity
            });
        });

        const orderData = {
            memberId: parseJwt(document.cookie.split('=')[1]).memberId,
            orderItems: orderItems
        };

        axios.post('https://localhost:8080/api/orders', orderData)
            .then(response => {
                if (response.data) {
                    alert('주문이 성공적으로 완료되었습니다.');
                    window.location.href = `/order/${response.data}`;
                } else {
                    alert('주문 처리 중 오류가 발생했습니다.');
                }
            })
            .catch(error => {
                console.error('Error submitting order:', error);
            });
    }


    document.getElementById('submit-order').addEventListener('click', submitOrder);
    document.getElementById('saveChanges').addEventListener('click', saveChanges);

    fetchCartItems();

    // 쿠키에서 JWT를 가져오는 함수
    function getJwtTokenFromCookie() {
        const name = "access=";
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) === 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

    // JWT에서 memberId를 추출하는 함수
    function getMemberIdFromToken() {
        const token = getJwtTokenFromCookie();
        if (!token) return null;

        const payload = parseJwt(token);
        return payload.memberId; // 실제 토큰 구조에 맞게 수정 필요
    }

    // JWT를 파싱하는 함수
    function parseJwt(token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    }
});
