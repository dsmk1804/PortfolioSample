const myPageSectionSection = document.getElementById('my-page-section-section')
const myPageBtn = document.getElementById('user');
const logoutBtn = document.getElementById('logout-btn');
// 검색 폼, 검색 섹션, 검색 버튼 선택
const searchForm = document.querySelector('.search-form');
const searchSection = document.querySelector('.search-section');
const searchInput = document.querySelector('.search-label input');
const searchButton = document.querySelector('#search');

// 실행하자마자 검색창과 내정보 스위치는 off
let myPageBtnOnOFF = false;

/////////////////////////////////////////////////////////////////
// 검색 input 필드 선택
const searchInputValue = document.getElementById('search-text');
// 검색 결과 리스트 선택
const searchResultList = document.querySelector('.search-view');



// 내 정보창 토글 함수
myPageBtn.addEventListener('click', showHideMyPage, false);

// 로그아웃
if(logoutBtn !== null) {
    logoutBtn.onclick = (e) => {
        e.preventDefault();
        const form = document.forms.item(0);
        form.action = "/user/logout";
        form.method = 'POST';
        form.submit();
    };
}





function showHideMyPage(e) {
    e.preventDefault();
    e.stopPropagation();
    if (this.classList.contains('show')) {
        myPageBtnOnOFF = true;
        this.classList.remove('show');
        myPageSectionSection.style.display = 'none';
        myPageBtnOnOFF = false;
    } else{
        myPageBtnOnOFF = false;
        this.classList.add('show');
        myPageSectionSection.style.display = 'block';
        myPageBtnOnOFF = true;
    }
    if (document.onclick){
        myPageBtnOnOFF = false;
        this.classList.add('show');
        myPageSectionSection.style.display = 'block';
        myPageBtnOnOFF = true;
    }
}

document.querySelector('main').onclick = (e) => {
    myPageBtnOnOFF = true;
    myPageBtn.classList.remove('show');
    myPageSectionSection.style.display = 'none';
    myPageBtnOnOFF = false;
}

const cashCharge = document.getElementById('cash-charge');

cashCharge.onclick= () => {
    const options = 'width=600, height=700, top=50, left=50, scrollbars=yes';
    window.open('/user/cashcharge','_blank',options);
    console.log("캐시충전페이지 열려라");
}



