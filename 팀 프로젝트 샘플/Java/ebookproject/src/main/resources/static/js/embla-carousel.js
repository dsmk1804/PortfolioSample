const emblaNodes = document.querySelectorAll('.embla');
emblaNodes.forEach(emblaNode => {
    embla_init(emblaNode);
})



function embla_init(emblaNode){
    const viewportNode = emblaNode.querySelector('.embla__viewport');
    const prevBtnNode = emblaNode.querySelector('.embla__button--prev');
    const nextBtnNode = emblaNode.querySelector('.embla__button--next');
    const dotsNode = emblaNode.querySelector('.embla__dots');
    let OPTIONS = {dragFree : true}
    if(emblaNode.querySelector('.sub-img-section') == null){
        OPTIONS = {};
    }
    const emblaApi = EmblaCarousel(viewportNode, OPTIONS);
    if(prevBtnNode != null && nextBtnNode != null){
        const removePrevNextBtnsClickHandlers = addPrevNextBtnsClickHandlers(
            emblaApi,
            prevBtnNode,
            nextBtnNode
        )

        emblaApi.on('destroy', removePrevNextBtnsClickHandlers)
    }
        const removeDotBtnsAndClickHandlers = addDotBtnsAndClickHandlers(
            emblaApi,
            dotsNode
        )
        emblaApi.on('destroy', removeDotBtnsAndClickHandlers)
}


function addTogglePrevNextBtnsActive(emblaApi, prevBtn, nextBtn) {
    const togglePrevNextBtnsState = () => {
        if (emblaApi.canScrollPrev()) prevBtn.removeAttribute('disabled')
        else prevBtn.setAttribute('disabled', 'disabled')

        if (emblaApi.canScrollNext()) nextBtn.removeAttribute('disabled')
        else nextBtn.setAttribute('disabled', 'disabled')
    }

    emblaApi
        .on('select', togglePrevNextBtnsState)
        .on('init', togglePrevNextBtnsState)
        .on('reInit', togglePrevNextBtnsState)

    return () => {
        prevBtn.removeAttribute('disabled')
        nextBtn.removeAttribute('disabled')
    }
}

function addPrevNextBtnsClickHandlers(emblaApi, prevBtn, nextBtn) {
    const scrollPrev = () => {
        emblaApi.scrollPrev();
    }
    const scrollNext = () => {
        emblaApi.scrollNext();
    }
    prevBtn.addEventListener('click', scrollPrev, false)
    nextBtn.addEventListener('click', scrollNext, false)

    const removeTogglePrevNextBtnsActive = addTogglePrevNextBtnsActive(
        emblaApi,
        prevBtn,
        nextBtn
    )

    return () => {
        removeTogglePrevNextBtnsActive()
        prevBtn.removeEventListener('click', scrollPrev, false)
        nextBtn.removeEventListener('click', scrollNext, false)
    }
}

function addDotBtnsAndClickHandlers (emblaApi, dotsNode) {
    let dotNodes = []

    const addDotBtnsWithClickHandlers = () => {
        dotsNode.innerHTML = emblaApi
            .scrollSnapList()
            .map(() => '<button class="embla__dot" type="button"></button>')
            .join('')

        const scrollTo = (index) => {
            emblaApi.scrollTo(index)
        }

        dotNodes = Array.from(dotsNode.querySelectorAll('.embla__dot'))
        dotNodes.forEach((dotNode, index) => {
            dotNode.addEventListener('click', () => scrollTo(index), false)
        })
    }

    const toggleDotBtnsActive = () => {
        const previous = emblaApi.previousScrollSnap()
        const selected = emblaApi.selectedScrollSnap()
        dotNodes[previous].classList.remove('embla__dot--selected')
        dotNodes[selected].classList.add('embla__dot--selected')
    }

    emblaApi
        .on('init', addDotBtnsWithClickHandlers)
        .on('reInit', addDotBtnsWithClickHandlers)
        .on('init', toggleDotBtnsActive)
        .on('reInit', toggleDotBtnsActive)
        .on('select', toggleDotBtnsActive)

    return () => {
        dotsNode.innerHTML = ''
    }
}
