const likeBtn = document.querySelectorAll('.like-btn > i');
console.log(likeBtn)
for (let i = 0; i < likeBtn.length; i++) {
    likeBtn[i].onclick = () => {
        if(likeBtn[i].className === "fa-regular fa-heart"){
            likeBtn[i].className = "fa-solid fa-heart";
        }else if(likeBtn[i].className === "fa-solid fa-heart"){
            likeBtn[i].className = "fa-regular fa-heart";
        }

    }
}
const chargeBtn = document.getElementById('charge-btn');

chargeBtn.onclick = () => {
    const options = 'width=600, height=700, top=50, left=50, scrollbars=yes';
    window.open('/user/cashcharge','_blank',options);
    console.log("캐시충전페이지 열려라");
}
