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

    // Fetch user info
    fetch('/api/getMember', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token') // Adjust according to how you store the token
        }
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error('User not logged in');
            }
        })
        .then(data => {
            document.getElementById('login-link').style.display = 'none';
            document.getElementById('mypage-link').style.display = 'inline';
        })
        .catch(error => {
            document.getElementById('login-link').style.display = 'inline';
            document.getElementById('mypage-link').style.display = 'none';
        });
});
