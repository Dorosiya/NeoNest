document.addEventListener('DOMContentLoaded', function() {
    axios.get('/api/member', {
        withCredentials: true
    })
        .then(response => {
            const data = response.data;

            // 내 정보 부분에 데이터를 설정
            document.getElementById('member-memberName').innerText = data.memberName;
            document.getElementById('member-name').innerText = data.name;
            document.getElementById('member-email').innerText = data.email;
            document.getElementById('member-phone').innerText = data.phoneNumber;
            document.getElementById('member-address').innerText = data.address;

            // 수정 폼에도 데이터를 설정
            document.getElementById('memberName').value = data.memberName;
            document.getElementById('name').value = data.name;
            document.getElementById('email').value = data.email;
            document.getElementById('phone').value = data.phoneNumber;
            document.getElementById('address').value = data.address;
            document.getElementById('postcode').value = data.postcode;
        })
        .catch(error => {
            console.error('데이터 로딩 오류:', error);
            alert('데이터를 불러오는 중 문제가 발생했습니다.');
        });
});

document.getElementById('edit-button').addEventListener('click', function() {
    document.querySelector('.card').style.display = 'none';
    document.getElementById('edit-form').style.display = 'block';
});

document.getElementById('cancel-button').addEventListener('click', function() {
    document.getElementById('edit-form').style.display = 'none';
    document.querySelector('.card').style.display = 'block';
});

document.getElementById('save-button').addEventListener('click', function() {
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const postcode = document.getElementById('postcode').value;
    const address = document.getElementById('address').value;
    const detailAddress = document.getElementById('detailAddress').value;
    const extraAddress = document.getElementById('extraAddress').value;

    const fullAddress = `${address} ${detailAddress} ${extraAddress}`;

    axios.post('/api/member/update', {
        name: name,
        email: email,
        phone: phone,
        address: fullAddress,
        postcode: postcode
    })
        .then(response => {
            alert('정보가 성공적으로 저장되었습니다.');
            window.location.reload(); // 페이지 새로고침으로 정보 업데이트 반영
        })
        .catch(error => {
            console.error('오류 발생:', error);
            alert('정보 저장에 실패했습니다.');
        });
});

function DaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            let addr = '';
            let extraAddr = '';

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
