document.addEventListener('DOMContentLoaded', function () {
    var swiper = new Swiper('.swiper-container', {
        loop: true,
        slidesPerView: 'auto',
        autoplay: {
            delay: 2500, // 2.5초마다 슬라이드 전환
            disableOnInteraction: false, // 사용자가 슬라이드를 조작해도 자동 전환이 멈추지 않음
        },
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
        scrollbar: {
            el: '.swiper-scrollbar',
            hide: true
        }
    });

});
