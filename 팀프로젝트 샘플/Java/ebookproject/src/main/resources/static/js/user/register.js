/*************** 비밀번호 공개,비공개 ****************************/
const passwordInput = document.getElementsByClassName('pw-input');
const passwordViewBtn = document.querySelectorAll('.pw-view-btn > i');

for (let i = 0; i < passwordInput.length; i++) {
    passwordViewBtn[i].onclick = () => {
        if(passwordInput[i].type === 'password'){
            passwordInput[i].type = 'text'
        }else if(passwordInput[i].type === 'text'){
            passwordInput[i].type = 'password'
        }
    }
}

/************************* 이메일 합치기 **************************/
const emailHead = document.querySelector('#email-head');
const emailTail = document.querySelector('#email-tail');
const emailSelectSection = document.querySelector('#email-select-section');
const fullEmail = document.querySelector('#full-email');




/******************* 본인 인증하기 ***************************/
const certBtn = document.getElementById('cert-btn');
const impUidInput = document.getElementById('imp-uid');
const certValidCheck = document.querySelector('.valid-check.certification');
IMP.init("imp26750511");

certBtn.onclick = () => {
    // IMP.certification(param, callback) 호출
    IMP.certification(
        {
            // param
            pg: "inicis_unified.MIIiasTest", //본인인증 설정이 2개이상 되어 있는 경우 필수
        },
        function (rsp) {
            if (rsp.success) {
                impUidInput.value = rsp["imp_uid"];
                certBtn.textContent = '본인인증완료';
                certBtn.disabled = true;
            } else {
                certValidCheck.toggleAttribute('active', true);
                certValidCheck.textContent = "본인인증 실패하였습니다.";
                // 인증 실패 시 로직
            }
        }
    );
}

/************ 취소 버튼 *********************/
const cancelBtn = document.getElementById('cancel-btn');

cancelBtn.onclick = () => {
    location.href = "/user/login";
}

/************ 아이디 중복 **********************/
const idDuplicationBtn = document.getElementById('id-duplication-btn');
const idInput = document.getElementById('id');
const IdValidCheck = document.querySelector('.valid-check.id');

idDuplicationBtn.onclick = () => {
    const idValue = idInput.value;
    fetch(`/user/find?id=${idValue}`)
        .then(response => {
            return response.json();
        }).then(result => {
            if(result){
                if(idValue.length >=4 && idValue.length <= 16 && idValue !== ""){
                    IdValidCheck.toggleAttribute('active', true);
                    IdValidCheck.textContent = "회원가입이 가능한 아이디입니다."
                }else{
                    IdValidCheck.toggleAttribute('active', true);
                    IdValidCheck.textContent = "아이디 조건에 맞지 않습니다."
                }
            }else{
                IdValidCheck.toggleAttribute('active', true);
                IdValidCheck.textContent = "현재 아이디가 존재합니다"
            }
        });
}

/********************** 비밀번호 확인 ***********************/
const pw1ValidCheck = document.querySelector('.valid-check.pw1');
const pw2ValidCheck = document.querySelector('.valid-check.pw2');
const pwInput = document.querySelectorAll('.pw-input');
const regPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z\d!@#$%^&*]+$/;



pwInput.item(0).addEventListener('focusout', () => {
    const firstPwInputValue = pwInput.item(0).value;
    const result = regPattern.test(firstPwInputValue);

    if(result){
        pw1ValidCheck.toggleAttribute('active', true);
        pw1ValidCheck.textContent = "올바른 비밀번호 양식입니다."
    }else{
        pw1ValidCheck.toggleAttribute('active', true);
        pw1ValidCheck.textContent = "올바르지 않은 비밀번호 양식입니다."
    }
})



pwInput.item(1).addEventListener('focusout', () => {
    if(pwInput.item(0).value !== "" && pwInput.item(1).value === pwInput.item(0).value){
        pw2ValidCheck.toggleAttribute('active', false);
    }else{
        pw2ValidCheck.toggleAttribute('active', true);
        pw2ValidCheck.textContent = "비밀번호가 일치하지 않습니다.";
    }
})

emailSelectSection.onchange = () => {
    emailTail.value = "";
    emailTail.value = emailSelectSection.value;
}

const registerBtn = document.getElementById('register-btn');
registerBtn.onclick = () => {
    fullEmail.value = emailHead.value + "@" + emailTail.value;


}