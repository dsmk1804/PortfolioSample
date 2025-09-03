const csrfToken = document.querySelector('input[name=_csrf]').value;

// 이메일 입력
const emailInput = document.getElementById('email-input');

// 닉네임 입력
const nicknameInput = document.getElementById('nickname-input');

// 인증번호 발송 버튼
const emailAuthBtn = document.getElementById('email-auth-btn');

// 인증번호 입력
const emailAuthInput = document.getElementById('email-auth-input');

// 회원가입 버튼
const submitBtn = document.querySelector('button[type=submit]');

// 인증번호 발송 버튼 클릭 시
emailAuthBtn.onclick = () => {
    const email = emailInput.value.trim();
    if(email.length === 0){
        alert('이메일을 입력해주세요');
        return;
    }

    alert('이메일에 인증번호를 발송했습니다!');
    fetch(`/user/auth`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": csrfToken
        },
         // ✅ JSON 객체로 변환해서 전송
        body: JSON.stringify({email})
    }).then(async response => {
          const text = await response.text();
          console.log("status:", response.status);
          console.log("body:", text);

          if (response.status === 200) {
              emailAuthBtn.disabled = true;
          } else {
              alert('이메일 전송 중 오류가 발생했습니다.');
          }
      })
}

console.log(emailInput)
// 이메일 입력 시 이메일 검사
emailInput.onchange = () => {
    const emailDuplicateMessageDiv = document.getElementById('email-duplicate-message');
    const email = emailInput.value.trim();
    fetch(`/user/check?email=${email}`)
        .then(response => {
            // 이메일/닉네임 중복 메시지 display 조절
            // (emailDuplicateMessageDiv.toggleAttribute('active', true); 이 null값 받으면 에러 일으켜서;
            if (emailDuplicateMessageDiv) {
                   emailDuplicateMessageDiv.style.display = response.status === 409 ? 'block' : 'none';
               }else{
                emailDuplicateMessageDiv.toggleAttribute('active', false);
            }
        });
}
// 닉네임 입력 시 검사
nicknameInput.onchange = () => {
    const nicknameDuplicateMessageDiv = document.getElementById('nickname-duplicate-message');
    const nickname = nicknameInput.value.trim();
    fetch(`/user/check?nickname=${nickname}`)
        .then(response => {
            if (response.status === 409) {
                nicknameDuplicateMessageDiv.toggleAttribute('active', true);
            }else{
                nicknameDuplicateMessageDiv.toggleAttribute('active', false);
            }
        });
}