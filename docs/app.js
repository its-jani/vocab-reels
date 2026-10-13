/* ============ Vocab Reels — landing interactions ============ */
(() => {
  'use strict';

  /* ---- boot scrollcraft engine (docs/scrollcraft/scrollcraft.js) ----
     The engine owns all scroll-driven behaviour now:
       - flow reveals ([data-sc-in] + [data-sc-stagger])
       - cue opacity windows inside [data-sc-act]
       - parallax on hero orbs / floating frame
       - kinetic line split on the hero headline
       - background drift across section colours
       - scroll progress bar
       - pointer devices: spotlight on device frame, magnet on icon chips
       - entry counters ([data-sc-count] outside any act)
     It honours prefers-reduced-motion by default. */
  if (window.ScrollCraft && typeof window.ScrollCraft.mount === 'function') {
    window.ScrollCraft.mount(document.body, { lerp: 0.2 });
  }

  /* ---- sticky nav: add blur/border after scrolling ---- */
  const nav = document.getElementById('nav');
  const onScroll = () => {
    nav.classList.toggle('nav-scrolled', window.scrollY > 12);
  };
  window.addEventListener('scroll', onScroll, { passive: true });
  onScroll();

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
