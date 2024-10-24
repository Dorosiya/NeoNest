/* singup.js */

document.getElementById('registerForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const memberName = document.getElementById('memberName').value;
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;
    const name = document.getElementById('name').value;
    const age = document.getElementById('age').value;
    const phoneNumber = document.getElementById('phoneNumber').value;
    const postcode = document.getElementById('postcode').value;
    const address = document.getElementById('address').value;
    const detailAddress = document.getElementById('detailAddress').value;
    const extraAddress = document.getElementById('extraAddress').value;

    /*const fullAddress = `${address} ${detailAddress} ${extraAddress}`;*/

    axios.post('https://localhost:8080/api/member', { // 올바른 서버 URL 설정
        memberName: memberName,
        password: password,
        email: email,
        name: name,
        age: age,
        phoneNumber: phoneNumber,
        address: address,
        detailAddress: detailAddress,
        extraAddress: extraAddress,
        postcode: postcode
    })
        .then(response => {
            if (response.data.success) {
                alert('회원가입 성공!');
                window.location.href = '/login';
            } else {
                alert('회원가입 실패: ' + response.data.message);
            }
        })
        .catch(error => {
            console.error('오류가 발생했습니다!', error);
        });
});

function DaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = '';
            var extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
            }

            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            document.getElementById("extraAddress").value = extraAddr;
            document.getElementById("detailAddress").focus();
        }
    }).open();
}
