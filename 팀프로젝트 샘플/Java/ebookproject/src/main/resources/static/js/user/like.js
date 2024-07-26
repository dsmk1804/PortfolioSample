/**************** 좋아요 *****************/
const saveBtns = document.querySelectorAll('.save'); // 좋아요 버튼
saveBtns.forEach(saveBtn => {
    saveBtn.onclick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        if(saveBtn.hasAttribute('active')){
            book_like_request("DELETE", false, 'fa-regular fa-heart', saveBtn);

        }else{
            book_like_request("POST", true, 'fa-solid fa-heart', saveBtn);
        }
    }
})
function book_like_request(method, active, className, saveBtn){
    const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content');
    const bookId = saveBtn.getAttribute('data');
    const icon = saveBtn.querySelector('i');
    fetch(`/user/save/${bookId}`,{
        method: method,
        headers: {"X-CSRF-TOKEN": csrfToken }
    })
        .then(response => {
            if(response.ok) {
                saveBtn.toggleAttribute('active', active);
                icon.className = className;
                return response.text();
            }
            else if(response.status === 401){
                return response.text();
            }
            else{
                throw Error();
            }
        })
        .then(value => {
            alert(value);
            location.reload();
        }, alert);
}