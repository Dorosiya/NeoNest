/* mypage.js */

document.addEventListener('DOMContentLoaded', function () {
    console.log("마이페이지 로드 완료");

    // 서버에서 모든 데이터를 한 번에 가져오는 함수
    axios.get('/api/mypage/summary', { withCredentials: true })
        .then(response => {
            const data = response.data;

            // 주문배송, 장바구니, 리뷰 작성 개수를 DOM에 설정
            document.querySelector('.summary-item:nth-child(1) .count').innerText = data.orderCount || 0;
            document.querySelector('.summary-item:nth-child(2) .count').innerText = data.cartCount || 0;
            document.querySelector('.summary-item:nth-child(3) .count').innerText = data.reviewCount || 0;
        })
        .catch(error => {
            console.error('데이터를 가져오는 중 오류 발생:', error);
        });

    // 네비게이션 링크 클릭 시 active 클래스 추가
    const navLinks = document.querySelectorAll('a.nav-link');
    navLinks.forEach(function (link) {
        link.addEventListener('click', function () {
            navLinks.forEach(function (link) {
                link.classList.remove('active');
            });
            this.classList.add('active');
        });
    });
});
