const menuTitles = document.querySelectorAll('.menu-title');

menuTitles.forEach(title => {
    title.addEventListener('click', function (e) {
        const majorMenu = this.parentElement; // 상위 li
        majorMenu.classList.toggle('open');
        e.stopPropagation();
    });
});