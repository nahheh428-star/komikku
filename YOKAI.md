# Yokai

Yokai is a manhwa reader built on **Komikku** (which is itself built on Mihon and
TachiyomiSY), with a design language ported from **MaxManager**.

The single most important rule in this repository: **the upstream project must
stay mergeable**. Everything below exists to keep `git merge upstream/master`
cheap — ideally a clean merge with no conflict at all.

---

## 1. Where our code lives

| Path | Owner | Merge risk |
|------|-------|-----------|
| `yokai-design/` | us | none — upstream never touches it |
| `app/src/main/java/yokai/` | us | none |
| `i18n-kmk/` | Komikku + us | low (Komikku's own module) |
| everything else | upstream | see §3 |

New screens, new components and the whole design system go in the two
directories at the top. A file that upstream has never seen can never conflict.

## 2. Markers

Edits to upstream files are wrapped, exactly as Komikku does for its own fork:

```kotlin
// KMK -->
// our code
// KMK <--
```

XML and Gradle files use the same marker as a comment (`<!-- KMK -->`, `// KMK -->`).
Do not add new `// SY` blocks — those belong to TachiyomiSY.

## 3. The only upstream files we are expected to touch

Keep this list short; every line here is a line that has to be re-checked on
every upstream merge.

| File | Change | Why |
|------|--------|-----|
| `settings.gradle.kts` | `include(":yokai-design")` | register the module |
| `app/build.gradle.kts` | `applicationId`, one dependency line | identity + design module |
| `app/src/main/java/eu/kanade/presentation/theme/TachiyomiTheme.kt` | wrap content in `YokaiDesign { }` | one choke point for type and shape |
| `i18n/src/commonMain/moko-resources/base/strings.xml` | `app_name` | display name |

## 4. Merging upstream

```bash
bash tools/yokai-sync-upstream.sh          # fetch + create a merge branch
bash tools/yokai-sync-upstream.sh --check  # only report what upstream changed
```

The script never merges into `master` directly and never pushes. Resolve the
conflicts in the four files above — they are one-liners — then run
`./gradlew spotlessApply :app:compileDebugKotlin`.

### If a merge conflict is not a one-liner

That is the signal that a change was made in an upstream file when it should
have been made in `yokai/`. Move it, and add the hook in §3 instead.

## 5. Design system

`yokai-design` is the shared language, ported from MaxManager's `nd.max.ui.design`
and `nd.max.ui.theme`, relicensed under GPLv3 (see the header of each file).
It contains **tokens, type, shape and a small set of components** — never
screens, never feature logic.

Rules that keep it coherent:

- A screen body reads spacing from `YokaiSpace`, radii from `YokaiRadius`, and
  never writes `dp` literals for layout.
- State colours come from `YokaiTone`, which is contrast-checked against both
  light and dark surfaces and is independent of the user's colour scheme.
  Accent is the only tone that follows the theme.
- A tone is never the only carrier of meaning: a component that shows tone also
  shows a label (and usually an icon).
- Nothing animates longer than `YokaiMotion.deliberate` (360ms) unless it is
  ambient light — a transition that takes longer is decoration.

Fonts (Space Grotesk, Manrope, JetBrains Mono) are bundled as static instances
in `yokai-design/src/main/res/font`, all under the SIL Open Font License. They
are bundled rather than fetched through Play Services so the `foss` build and a
device without Google services render identically.

## 6. Licensing

Komikku is GPLv3; Yokai is a derivative and stays GPLv3. The design files
carried over from MaxManager were written by the same author and are relicensed
here under GPLv3 — that permission has to come from the copyright holder, not
from us. Do not copy files from another proprietary project into this repository.
