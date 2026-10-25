# Release Notes

## v1.3.5 — New Brand Logo & Resizable Streak Widget

`versionCode` 8 → 9 · `versionName` 1.3.4 → 1.3.5

### 🎨 New app icon (matches the website / README logo)

- Replaced the legacy neon winged-book mark with the minimalist gradient
  "reels-box" logo used by the site favicon and the top of the README.
- Regenerated the full launcher set: adaptive-icon background (flat `#0a0a0b`),
  foreground, a dedicated monochrome layer for themed icons (Android 13+), all
  density mipmaps, the in-app / top-bar logo, the splash icon, and the widget
  picker preview.

### 📐 Resizable Streak & Goal widget

- The widget now resizes freely (added `minResizeWidth`/`minResizeHeight`,
  raised `maxResize` to 600dp) instead of being locked to one cell size.
- Content is **size-aware**: on every resize the card reads its live `DpSize`
  and scales the flame glyph, streak number, status pill, paddings and corner
  radius together, and drops the hint row when the card is too short — so the
  layout tracks the new size without overlapping or clipping.

## v1.3.4 — Home Screen Streak & Goal Tracker Widget

`versionCode` 7 → 8 · `versionName` 1.3.3 → 1.3.4

### ✨ New: Streak & Goal Tracker home screen widget

Your learning habit now lives on the home screen — lightweight, real-time, and
tap-to-resume (built with Jetpack Glance / AppWidgetProvider):

- 🔥 **Current streak** — consecutive days the daily goal was completed.
- ✅ **Today's goal status** — *Completed* or *Pending · x/y words today*, at a glance.
- 💥 **Auto zero-reset** — miss a day and the streak explicitly drops to 0.
- ⚡ **Quick action** — one tap anywhere on the widget opens the app directly on
  today's activity panel (streak, goal progress & daily stats dashboard).

Add it via **long-press home screen → Widgets → Vocab Reels → Streak & Daily Goal**.

### 🧠 Architecture

- **`StreakStateMachine`** — new pure, modular state machine owning all streak
  semantics (increment on completion, grace window while today is pending,
  explicit zero-reset on a missed day). It is now the **single source of truth**
  shared by the in-app dashboard and the widget.
- **Anti-spoofing date math** — all day comparisons run on UTC epoch-day
  boundaries derived from local calendar dates, so timezone changes and DST
  short/long days cannot corrupt or inflate a streak; future-dated completions
  are ignored.
- **State persistence** — streak counters, last completed date, and daily goals
  stay in the existing Room store (`daily_learning_stats` /
  `user_learning_goal`); new suspend snapshot queries feed the widget outside
  the app's Flow lifecycle.
- **Real-time sync** — marking words learned or editing the daily goal pushes an
  immediate widget refresh (`StreakWidgetRefresher`); a 30-minute system update
  cycle guarantees the midnight reset renders even when the app process is dead.

### 🔧 Changes

- `MainActivity` now launches with `launchMode="singleTop"` so widget taps
  resume the existing instance cleanly via `onNewIntent`.
- New dependency: `androidx.glance:glance` + `androidx.glance:glance-appwidget` 1.2.0.
- Streak calculation in `VocabViewModel` delegates to `StreakStateMachine`
  (replaces the previous inline loop; user-visible semantics unchanged).
- 14 new pure-JVM unit tests covering increment, completion, zero-reset,
  rollover, boundary dates, and spoofing guards.

### 📱 Native / PWA bridge notes

This app is **native Android**, so the widget ships as a real Glance
`AppWidgetProvider`. The state machine and its persistence contract are
deliberately platform-agnostic for future wrappers:

- **iOS (WidgetKit)**: mirror `StreakState` in App Groups `UserDefaults` —
  write `currentStreak`, `lastCompletedDate`, `dailyGoal`, `learnedToday` on
  every goal-relevant mutation, and reuse the same epoch-day diff logic.
- **PWA / web**: `StreakStateMachine` translates 1:1 to a JS module over
  `localStorage`/IndexedDB; expose the quick action through
  `app shortcuts` (Web App Manifest `shortcuts` entries) and re-render the
  state on `visibilitychange`.

## v1.3.3

Previous release — reels-style learning, offline dictionary, streaks,
daily goals, category progress, TTS pronunciation, import/export.
