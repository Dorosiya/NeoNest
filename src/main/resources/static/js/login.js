document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const memberName = document.getElementById('memberName').value;
    const password = document.getElementById('password').value;

    axios.post('https://localhost:8080/login', {
        memberName: memberName,
        password: password
    })
        .then(response => {
            if (response.status === 200) {
                window.location.href = '/main';
            } else {
                alert('로그인 실패');
            }
        })
        .catch(error => {
            console.error('오류가 발생했습니다!', error);
        });
});
