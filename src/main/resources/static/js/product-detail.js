document.addEventListener('DOMContentLoaded', function () {
    const path = window.location.pathname;
    const parts = path.split('/');
    const productId = parts[parts.length - 1];

    const wishlistButton = document.getElementById('wishlist-button');
    const cartButton = document.getElementById('add-to-cart');
    const quantityInput = document.getElementById('quantity');

    fetchProductDetails(productId);
    fetchProductReviews(productId);

    function updateTotalPrice() {
        const price = parseInt(document.getElementById('product-price').dataset.price) || 0;
        const quantity = parseInt(quantityInput.value) || 1;
        const totalPrice = price * quantity;
        document.getElementById('total-price').innerText = totalPrice.toLocaleString();
    }

    function updateQuantity(change) {
        let currentQuantity = parseInt(quantityInput.value);
        currentQuantity += change;
        if (currentQuantity < 1) currentQuantity = 1;
        quantityInput.value = currentQuantity;

        updateTotalPrice();
    }

    // 상품 상세 정보를 서버에서 가져오는 함수
    function fetchProductDetails(productId) {
        axios.get(`/api/products/${productId}`)
            .then(response => {
                const product = response.data;

                // 가져온 데이터를 DOM에 업데이트
                document.getElementById('product-name').innerText = product.name || '상품명 정보 없음';
                document.getElementById('product-image').src = product.image ? `/api/products/images/${product.image}` : '/images/default-image.jpg';
                document.getElementById('product-price').innerText = product.price ? product.price.toLocaleString() + '원' : '가격 정보 없음';
                document.getElementById('product-price').dataset.price = product.price || 0;
                document.getElementById('product-original-price').innerText = product.originalPrice ? `실제가격: ${product.originalPrice.toLocaleString()}원` : '원가 정보 없음';
                document.getElementById('product-description').innerText = product.description || '설명 정보 없음';

                // 찜하기 버튼 초기 상태 설정
                checkWishlistStatus();
            })
            .catch(error => {
                console.error('상품 정보를 불러오는 중 오류 발생:', error);
            });
    }

    // 리뷰 정보를 서버에서 가져오는 함수
    function fetchProductReviews(productId) {
        axios.get(`/api/products/${productId}/reviews`)
            .then(response => {
                const { reviews, ratingCount, averageRating } = response.data;

                // 리뷰 정보가 배열이 아니면 빈 배열로 초기화
                const reviewsList = Array.isArray(reviews) ? reviews : [];

                // 평균 평점과 리뷰 수 업데이트 (두 개의 HTML 요소에 대해)
                document.getElementById('product-average-rating').innerText = `평균 평점: ★${averageRating || 'N/A'} (리뷰 ${ratingCount || 0}건)`;
                document.getElementById('average-rating').innerText = `평균 평점: ★${averageRating || 'N/A'} (리뷰 ${ratingCount || 0}건)`;

                const reviewsContainer = document.getElementById('reviews-container');
                reviewsContainer.innerHTML = '';

                reviewsList.forEach(review => {
                    const reviewElement = document.createElement('div');
                    reviewElement.classList.add('review-item');
                    reviewElement.innerHTML = `
                        <p class="review-author"><strong>${review.reviewerName}</strong></p>
                        <p>평점: ★${review.rating}</p>
                        <p class="review-content">${review.comment}</p>
                        <p class="review-date">작성일: ${review.reviewDate}</p>
                    `;
                    reviewsContainer.appendChild(reviewElement);
                });
            })
            .catch(error => {
                console.error('리뷰 정보를 불러오는 중 오류 발생:', error);
            });
    }

    // 찜 상태 확인 및 버튼 텍스트 업데이트 함수
    function checkWishlistStatus() {
        axios.get(`/api/wishlist/${productId}`)
            .then(response => {
                if (response.data.isInWishlist) {
                    wishlistButton.textContent = '찜 취소';
                    wishlistButton.dataset.inWishlist = "true";
                } else {
                    wishlistButton.textContent = '찜하기';
                    wishlistButton.dataset.inWishlist = "false";
                }
            })
            .catch(error => {
                console.error('찜 상태 확인 중 오류 발생:', error);
            });
    }

    // 찜하기 기능 서버 호출
    function toggleWishlist() {
        const isInWishlist = wishlistButton.dataset.inWishlist === "true";
        if (isInWishlist) {
            // 찜 목록에서 삭제
            axios.delete(`/api/wishlist/${productId}`)
                .then(response => {
                    wishlistButton.textContent = '찜하기';
                    wishlistButton.dataset.inWishlist = "false";
                    alert(response.data.message);
                })
                .catch(error => {
                    console.error('찜 목록 삭제 중 오류 발생:', error);
                    alert('찜 목록 삭제 중 오류가 발생했습니다. 다시 시도해주세요.');
                });
        } else {
            // 찜 목록에 추가
            axios.post('/api/wishlist', { productId: productId })
                .then(response => {
                    wishlistButton.textContent = '찜 취소';
                    wishlistButton.dataset.inWishlist = "true";
                    alert(response.data.message);
                })
                .catch(error => {
                    console.error('찜 목록 추가 중 오류 발생:', error);
                    alert('찜 목록 추가 중 오류가 발생했습니다. 다시 시도해주세요.');
                });
        }
    }

    // 장바구니에 추가 버튼 클릭 이벤트 핸들러
    function addToCart() {
        const quantity = parseInt(quantityInput.value) || 1;
        const price = parseInt(document.getElementById('product-price').dataset.price) || 0;

        /*const memberId = getMemberIdFromToken();*/

        const cartData = {
            /*memberId: memberId,*/
            productId: parseInt(productId),
            price: price,
            quantity: quantity
        };

        axios.post('/api/cart', cartData)
            .then(response => {
                alert('장바구니에 상품이 추가되었습니다.');
                window.location.href = '/cart';
            })
            .catch(error => {
                alert('장바구니 추가 중 오류가 발생했습니다. 다시 시도해주세요.');
                console.error('Error adding to cart:', error);
            });
    }

    // 찜하기 버튼 클릭 이벤트 추가
    if (wishlistButton) {
        wishlistButton.addEventListener('click', toggleWishlist);
    }

    // 장바구니 버튼 클릭 이벤트 추가
    if (cartButton) {
        cartButton.addEventListener('click', addToCart);
    }

    // 글로벌 함수로 등록
    window.updateQuantity = updateQuantity;

    // 쿠키에서 JWT를 가져오는 함수
    /*function getJwtTokenFromCookie() {
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
    }*/

    // JWT에서 memberId를 추출하는 함수
    /*function getMemberIdFromToken() {
        const token = getJwtTokenFromCookie();
        if (!token) return null;

        const payload = parseJwt(token);
        return payload.memberId;
    }*/

    // JWT를 파싱하는 함수
    /*function parseJwt(token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    }*/
});
