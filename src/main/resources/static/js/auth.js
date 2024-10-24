/* auth.js */

// 사용자 정보를 저장 객체
window.user = {
    loginStatus: false,
    data: null
};

function fetchMemberInfo() {
    return axios.get('/api/member/info', {
        withCredentials: true
    })
        .then(response => {
            const data = response.data;
            window.user.loginStatus = data.loginStatus;
            window.user.data = data.loginStatus ? data : null;
        })
        .catch(error => {
            console.log('로그인 정보 없음', error);
            window.user.loginStatus = false;
            window.user.data = null;
        })
        .finally(() => {
            updateNavLinks();
        })
}

document.getElementById('logout-link').addEventListener('click', function(event) {
    event.preventDefault();

    axios.post('/logout', null, {
        withCredentials: true
    })
        .then(response => {
            window.location.href = '/main'; // 로그아웃 후 메인 페이지로 이동
        })
        .catch(error => {
            console.error('로그아웃 실패:', error);
        });
});

// 로그인 상태에 따라 네비게이션 바 업데이트
function updateNavLinks() {
    if (window.user.loginStatus) {
        document.getElementById('login-link').style.display = 'none';
        document.getElementById('signup-link').style.display = 'none';
        document.getElementById('mypage-link').style.display = 'inline';
        document.getElementById('logout-link').style.display = 'inline';
    } else {
        document.getElementById('login-link').style.display = 'inline';
        document.getElementById('signup-link').style.display = 'inline';
        document.getElementById('mypage-link').style.display = 'none';
        document.getElementById('logout-link').style.display = 'none';
    }
}

// 페이지 로드 시 사용자 정보 가져오기
document.addEventListener('DOMContentLoaded', fetchMemberInfo);
