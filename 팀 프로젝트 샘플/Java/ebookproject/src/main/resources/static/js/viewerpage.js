const viewChangeBtn = document.querySelector('.view-page-bar-btn');
const slideOrScroll = document.querySelectorAll('.slide');



viewChangeBtn.onclick = () => {
    for (let i = 0; i < slideOrScroll.length; i++) {
        if(slideOrScroll[i].classList.contains('slide')){
            slideOrScroll[i].classList.add('scroll');
            slideOrScroll[i].classList.remove('slide');
        }else if(slideOrScroll[i].classList.contains('scroll')){
            slideOrScroll[i].classList.add('slide');
            slideOrScroll[i].classList.remove('scroll');
        }
    }
}
