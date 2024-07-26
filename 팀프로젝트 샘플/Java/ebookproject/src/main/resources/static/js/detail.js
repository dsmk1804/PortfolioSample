const token = document.querySelector("meta[name=_csrf]").getAttribute('content');
// 구매 버튼 클릭 시 호출될 함수
function buy_chapter(chapterNo) {
    console.log("버튼 잘 되는지 확인용: ");

    fetch(`/user/chapter/${chapterNo}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': token
        }
    })
        .then(response => {
            console.log('Response status:', response.status); // 응답 상태 코드 출력
            return response.text().then(text => {
                if (response.status === 201) {
                    alert("구매에 성공하였습니다!") // 성공 메시지 출력
                    location.reload();
                }
                else if(response.status === 401){
                    alert("로그인 후 이용 가능합니다.");
                }
                else if(response.ok){
                    alert("캐시가 부족합니다. 캐시를 충전해주세요.") // 성공 메시지 출력
                    const options = 'width=600, height=700, top=50, left=50, scrollbars=yes';
                    window.open('/user/cashcharge','_blank',options);
                }
                else{
                    console.error('오류 발생:', error.message);
                }
            });
        });
}