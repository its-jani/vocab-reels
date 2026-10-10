/* ============ Vocab Reels — landing interactions ============ */
(() => {
  'use strict';

  /* ---- sticky nav: add blur/border after scrolling ---- */
  const nav = document.getElementById('nav');
  const onScroll = () => {
    nav.classList.toggle('nav-scrolled', window.scrollY > 12);
  };
  window.addEventListener('scroll', onScroll, { passive: true });
  onScroll();

  /* ---- scroll reveal for cards / panels ---- */
  const revealEls = document.querySelectorAll('.reveal');
  if ('IntersectionObserver' in window) {
    const io = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('is-visible');
            io.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.12 }
    );
    revealEls.forEach((el) => io.observe(el));
  } else {
    revealEls.forEach((el) => el.classList.add('is-visible'));
  }

  /* ---- live GitHub stars (graceful fallback to placeholder) ---- */
  const starsEl = document.getElementById('gh-stars');
  const REPO = 'its-jani/vocab-reels';
  fetch(`https://api.github.com/repos/${REPO}`, {
    headers: { Accept: 'application/vnd.github.v3+json' },
  })
    .then((r) => (r.ok ? r.json() : Promise.reject()))
    .then((data) => {
      if (starsEl && typeof data.stargazers_count === 'number') {
        starsEl.textContent = `★ ${data.stargazers_count.toLocaleString()}`;
      }
    })
    .catch(() => {
      /* keep placeholder — page works fully offline / when rate-limited */
    });

  /* ---- footer year ---- */
  const year = document.getElementById('year');
  if (year) year.textContent = new Date().getFullYear();
})();
