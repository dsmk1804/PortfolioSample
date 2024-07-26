const loginBtn = document.querySelector('#login-btn');

/**************** 비밀번호 공개 / 비공개 ********************/
const passwordInput = document.querySelector('#password > input');
const passwordViewIcon = document.querySelector('#password-view-btn');

passwordViewIcon.onclick = () => {
    if(passwordInput.type === "password"){
        passwordInput.type = "text";
    }else if(passwordInput.type === "text"){
        passwordInput.type = "password";
    }
}

/********************* 아이디 저장 **************************/
const idInput = document.querySelector('#id input');
const idSaveBtn = document.querySelector('#login-id-remember input');

loginBtn.onclick = () => {
    if(idSaveBtn.checked){
        const date = new Date();
        date.setDate(date.getDate() + 365);
        document.cookie = `id=${idInput.value}; expires=${date.toUTCString()}`;
    }else{
        document.cookie = `id=false`;
    }

}

window.onload = () => {
    if(document.cookie.includes("id")){
        const cookieName = document.cookie.replaceAll('=','').replaceAll('id','');
        if(cookieName !== 'false'){
            idSaveBtn.checked = true;
            idInput.value = cookieName;
        }
    }
}




