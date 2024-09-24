document.addEventListener('DOMContentLoaded', function () {
    const path = window.location.pathname;
    const parts = path.split('/');
    const productId = parts[parts.length - 1];

    fetchProductDetails(productId);

    function updateTotalPrice() {
        const price = parseInt(document.getElementById('product-price').dataset.price) || 0;  // 가격을 data-price 속성에서 가져옴
        const quantity = parseInt(document.getElementById('quantity').value) || 1;
        const totalPrice = price * quantity;
        document.getElementById('total-price').innerText = totalPrice.toLocaleString();
    }

    function updateQuantity(change) {
        const quantityInput = document.getElementById('quantity');
        let currentQuantity = parseInt(quantityInput.value);
        currentQuantity += change;
        if (currentQuantity < 1) currentQuantity = 1;
        quantityInput.value = currentQuantity;

        updateTotalPrice();
    }

    // 상품 상세 정보를 서버에서 가져오는 함수
    function fetchProductDetails(productId) {
        axios.get(`https://localhost:8080/api/products/${productId}`)
            .then(response => {
                const product = response.data;

                // 가져온 데이터를 DOM에 업데이트
                document.getElementById('product-image').src = `https://localhost:8080/api/products/images/${product.image || 'default-image.jpg'}`;
                document.getElementById('product-name').innerText = product.name || '상품명 정보 없음';
                document.getElementById('product-price').innerText = (product.price ? product.price.toLocaleString() + "원" : '가격 정보 없음');
                document.getElementById('product-price').dataset.price = product.price || 0;  // 데이터가 없으면 0으로 설정
                document.getElementById('product-original-price').innerText = (product.originalPrice ? `실제가격: ${product.originalPrice.toLocaleString()}원` : '원가 정보 없음');
                document.getElementById('product-rating').innerText = `평점: ★${product.rating || 'N/A'} 리뷰 ${product.reviews || 'N/A'}`;
                document.getElementById('product-description').innerText = product.description;  // 상품 설명 추가

                updateTotalPrice();
            })
            .catch(error => console.error('Error fetching product details:', error));
    }

    // 장바구니에 추가 버튼 클릭 이벤트 핸들러
    document.getElementById('add-to-cart').addEventListener('click', function () {
        const quantity = parseInt(document.getElementById('quantity').value) || 1;
        const price = parseInt(document.getElementById('product-price').dataset.price) || 0;

        // JWT 토큰에서 사용자 ID를 추출하는 로직이 필요합니다.
        const memberId = getMemberIdFromToken(); // 이 함수는 실제로 JWT에서 아이디를 추출해야 합니다.

        // 서버로 전송할 데이터
        const cartData = {
            memberId: memberId,
            productId: parseInt(productId),
            price: price,
            quantity: quantity
        };

        axios.post('https://localhost:8080/api/cart', cartData)
            .then(response => {
                alert('장바구니에 상품이 추가되었습니다.');
                console.log('Cart response:', response.data);
                window.location.href = '/cart';
            })
            .catch(error => {
                alert('장바구니 추가 중 오류가 발생했습니다.');
                console.error('Error adding to cart:', error);
            });
    });

    // 글로벌 함수로 등록 (HTML에서 호출 가능하게)
    window.updateQuantity = updateQuantity;

    // 쿠키에서 JWT를 가져오는 함수
    function getJwtTokenFromCookie() {
        const name = "access=";
        const decodedCookie = decodeURIComponent(document.cookie);
        const ca = decodedCookie.split(';');
        for(let i = 0; i < ca.length; i++) {
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
