/* mypage.js */

document.addEventListener('DOMContentLoaded', function () {
    console.log("마이페이지 로드 완료");

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
