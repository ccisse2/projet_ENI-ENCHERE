document.addEventListener('DOMContentLoaded', () => {
    const track = document.querySelector('.carousel-track');
    const slides = Array.from(track.children);
    const slideWidth = slides[0].getBoundingClientRect().width;

    // Clone first and last slides
    const firstSlideClone = slides[0].cloneNode(true);
    const lastSlideClone = slides[slides.length - 1].cloneNode(true);

    track.appendChild(firstSlideClone);
    track.insertBefore(lastSlideClone, slides[0]);

    let currentIndex = 1; // Start at the first slide (after the clone)
    const totalSlides = slides.length + 2; // Include the clones

    track.style.transform = `translateX(${-slideWidth * currentIndex}px)`;

    function moveCarousel(direction) {
        track.style.transition = 'transform 0.5s ease-in-out';
        currentIndex += direction;

        if (currentIndex < 0) {
            track.style.transition = 'none';
            currentIndex = totalSlides - 2;
            track.style.transform = `translateX(${-slideWidth * currentIndex}px)`;
            setTimeout(() => {
                track.style.transition = 'transform 0.5s ease-in-out';
                moveCarousel(-1);
            }, 50);
        } else if (currentIndex >= totalSlides - 1) {
            track.style.transition = 'none';
            currentIndex = 1;
            track.style.transform = `translateX(${-slideWidth * currentIndex}px)`;
            setTimeout(() => {
                track.style.transition = 'transform 0.5s ease-in-out';
                moveCarousel(1);
            }, 50);
        } else {
            track.style.transform = `translateX(${-slideWidth * currentIndex}px)`;
        }
    }

    document.querySelector('.carousel-button.prev').addEventListener('click', () => moveCarousel(-1));
    document.querySelector('.carousel-button.next').addEventListener('click', () => moveCarousel(1));
});
