/* review-create.js */

document.addEventListener('DOMContentLoaded', function () {
    const pathParts = window.location.pathname.split('/');
    const orderUid = pathParts[pathParts.length - 2];
    const productId = pathParts[pathParts.length - 1];

    const productNameInput = document.getElementById('productName');
    const productImage = document.getElementById('productImage');
    const reviewForm = document.getElementById('reviewForm');
    const ratingInput = document.getElementById('rating');
    const commentInput = document.getElementById('comment');

    // 서버에서 리뷰 및 상품 정보를 가져와서 분기 처리
    function fetchReviewDetails() {
        axios.get(`/api/reviews/${orderUid}/${productId}`)
            .then(response => {
                const review = response.data;

                // 제품 정보 설정 (리뷰 여부와 관계없이 항상 설정)
                productNameInput.value = review.productName;
                productImage.src = `/api/products/images/${review.productImage}`;
                productImage.alt = review.productName;

                if (review.isReviewed) {
                    // 리뷰가 존재할 경우 작성된 리뷰 보여주기
                    ratingInput.value = review.rating;
                    commentInput.value = review.comment;

                    // 작성된 리뷰는 수정 불가능하게 폼 비활성화
                    ratingInput.disabled = true;
                    commentInput.disabled = true;
                    reviewForm.querySelector('button[type="submit"]').disabled = true;

                    // 리뷰 보여주기 위한 메시지 추가
                    const reviewStatus = document.createElement('p');
                    reviewStatus.textContent = "이미 작성된 리뷰입니다.";
                    reviewForm.appendChild(reviewStatus);
                } else {
                    // 리뷰가 존재하지 않으면 작성할 수 있도록 폼 활성화
                    ratingInput.disabled = false;
                    commentInput.disabled = false;
                    reviewForm.querySelector('button[type="submit"]').disabled = false;
                }
            })
            .catch(error => {
                console.error('리뷰 정보를 불러오는 중 오류 발생:', error);
                if (error.response && error.response.status === 404) {
                    // 리뷰가 존재하지 않는 경우에도 제품 정보를 설정
                    console.log('리뷰가 존재하지 않습니다. 작성 가능한 상태입니다.');
                    ratingInput.disabled = false;
                    commentInput.disabled = false;
                    reviewForm.querySelector('button[type="submit"]').disabled = false;
                } else {
                    alert('리뷰 정보를 가져오는 도중 문제가 발생했습니다.');
                }
            });
    }

    // 리뷰 작성 폼 제출 시 서버로 리뷰 정보 전송
    reviewForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const rating = ratingInput.value;
        const comment = commentInput.value;

        const reviewData = {
            orderUid: orderUid,
            productId: productId,
            rating: rating,
            comment: comment
        };

        axios.post('/api/reviews', reviewData)
            .then(response => {
                alert('리뷰가 성공적으로 작성되었습니다.');
                window.location.href = `/mypage/orders`; // 리뷰 작성 후 주문 확인 페이지로 이동
            })
            .catch(error => {
                console.error('리뷰 작성 중 오류 발생:', error);
                alert('리뷰 작성에 실패했습니다. 다시 시도해주세요.');
            });
    });

    fetchReviewDetails(); // 리뷰 정보 및 제품 정보 가져오기
});
