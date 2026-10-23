<div align="center">

# 妖怪 YOKAI

**A manhwa reader with its own design language — built on Komikku, and built to stay mergeable with it.**

`app.yokai` · Android 8.0+ · GPLv3 · no account · no tracking

[العربية](README.ar.md) · [Design language](YOKAI.md#5-design-system) · [Upstream policy](YOKAI.md#4-merging-upstream)

</div>

---

## What this is

Yokai is a fork of [Komikku](https://github.com/komikku-app/komikku) (itself built on
[Mihon](https://github.com/mihonapp/mihon) and TachiyomiSY) that reads manhwa the way
manhwa is actually drawn: as one continuous vertical strip.

It is **not** a reskin with a new logo. Three things make it a different product:

| | Komikku | Yokai |
|---|---|---|
| Type | Material default | Space Grotesk · Manrope · JetBrains Mono, bundled in the APK |
| Geometry | Material shapes | one token scale: 12 / 14 / 22 / 28dp, derived, never literal |
| Colour | theme accent everywhere | the theme owns *accent only*; state colours are fixed and contrast-checked |
| Reading | page number hidden in the menu | a HUD that surfaces chapter and position while you scroll, then gets out of the way |
| First run | settings checklist | a welcome screen that says what the app is before asking for anything |
| Package | `app.komikku` | `app.yokai` — installs **beside** Komikku, does not replace it |

Everything else — sources, extensions, downloads, tracking, backups, library
management — is Komikku's, unchanged and maintained upstream. That is the point: the
hard part is already solved, and solving it again would be a worse app.

## The rules this project follows

1. **Upstream stays mergeable.** Our code lives in `yokai-design/` and
   `app/src/main/java/yokai/` — directories upstream has never seen. Reaching into a
   Komikku file costs a one-line hook wrapped in a `// KMK` marker, and the complete
   list of those hooks is [eight entries long](YOKAI.md#3-the-only-upstream-files-we-are-expected-to-touch).
2. **One design language.** Spacing, radii, opacity, motion and tone come from
   `yokai-design`. A screen that writes `16.dp` for a gutter is a bug.
3. **State is never colour alone.** Every tone carries a label (and usually an icon),
   so a warning survives greyscale, colour blindness and high-contrast mode.
4. **Nothing longer than 360ms** unless it is ambient light. Motion explains a
   change; anything else is decoration.
5. **A number that is unknown renders as `—`**, never as a plausible zero.

The full contract, including how to merge upstream without archaeology, is in
[YOKAI.md](YOKAI.md).

## The design language

Ported from [MaxManager](https://github.com/) — a performance console whose language
is a measuring instrument, not a marketing site — and relicensed here under GPLv3 by
its author.

- **Tokens**: [`YokaiTokens.kt`](yokai-design/src/main/java/yokai/design/YokaiTokens.kt)
- **Type**: [`YokaiType.kt`](yokai-design/src/main/java/yokai/design/YokaiType.kt)
- **Components**: card, section header, metric readout, status chip, tab strip,
  ambient surface — [`components/`](yokai-design/src/main/java/yokai/design/components)

Fonts are bundled as static instances (SIL Open Font License) instead of being fetched
through Play Services, so the `foss` build and a device without Google services render
identically.

## Reading

Yokai keeps Komikku's reader — paged, vertical, continuous vertical, webtoon — with its
crop, zoom, tap-zone and chapter-transition behaviour intact, and adds the reading HUD:
chapter name and position, in mono type, for a moment after the page changes, then gone.

## Build

```bash
./gradlew spotlessCheck          # format gate
./gradlew :app:compileDebugKotlin
./gradlew assembleDebug          # debug APK, all ABIs + universal
```

CI runs the same commands on every push (`.github/workflows/yokai-build.yml`) and
uploads the APK as an artifact. No secrets are required for a debug build.

## Install

Grab the APK from the latest CI run, or build it yourself. It installs alongside
Komikku; a backup from Komikku restores into Yokai.

## Licence

Yokai is a derivative of Komikku and therefore **GPLv3**. Komikku is a derivative of
Mihon and TachiyomiSY; their copyright notices stay where they are. The design files
ported from MaxManager are relicensed under GPLv3 by their author.

Yokai ships no content and no sources. It is a reader; what you read with it is your
business.
