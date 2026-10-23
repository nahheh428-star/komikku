# Yokai Design Language

> The values below are **measured from the app's own source**, not invented. The source of
> truth is `yokai-design/src/main/java/yokai/design/YokaiTokens.kt`.
> `python3 tools/design_doc.py --assert` fails CI when this file and that source disagree,
> so this document cannot quietly become fiction.

Yokai is a reading instrument, not a shop window. The interface exists to put a strip of
art on a screen and get out of the way: one spacing scale, one card contract, one accent
that belongs to the user while every *status* colour is fixed and contrast-checked, and
motion that is never long enough to become decoration.

## Spacing

A 4dp-based scale. A screen body should need `gutter`, `section`, `row` and `pageBottom`
only; the raw steps exist for component internals.

| Token | Value | Used for |
|---|---|---|
| space.hairline | 2dp | the thinnest separation that still reads on a phone |
| space.xs | 4dp | inside a tight pair (icon ↔ label) |
| space.sm | 8dp | siblings in a row |
| space.md | 12dp | inner padding of a control |
| space.lg | 16dp | card inner padding |
| space.xl | 20dp | space before a section heading |
| space.xxl | 28dp | the widest gap the language allows |
| space.gutter | 20dp | horizontal page padding, every screen |
| space.section | 28dp | vertical gap between two sections |
| space.row | 8dp | gap between rows inside one section |
| space.rowPaddingHorizontal | 14dp | a list row |
| space.rowPaddingVertical | 12dp | a list row |
| space.pageBottom | 40dp | clearance so the navigation never covers the last row |

## Shape

A slot names the token it stands for, so a card and the dialog it opens can never
disagree about how soft the app is.

| Token | Value | Material slot |
|---|---|---|
| radius.control | 12dp | extraSmall — chip, text field, tag |
| radius.row | 14dp | small — list row, action row |
| radius.group | 22dp | medium — card, grouped container |
| radius.sheet | 28dp | large / extraLarge — sheet, dialog |
| radius.pill | 100dp | state only, never a primary action |

## Sizes

| Token | Value | Used for |
|---|---|---|
| size.minTouchTarget | 48dp | accessibility floor for anything clickable |
| size.iconGlyph | 20dp | glyph inside a card's frame |
| size.iconGlyphSmall | 16dp | glyph inside a chip |
| size.rowIconContainer | 34dp | icon frame in a list row |
| size.iconContainer | 40dp | icon frame in a card |
| size.border | 1dp | the hairline around a card |
| size.readingMaxWidth | 560dp | keeps long copy readable on tablets |
| size.barHeight | 8dp | a fractional progress bar |

## Opacity

Borders stay hairline-quiet and tone containers stay low, so content is always the
brightest thing on screen.

| Token | Value | Used for |
|---|---|---|
| alpha.border | 0.16 | a tone's hairline |
| alpha.borderStrong | 0.28 | the same line when it must win |
| alpha.toneWash | 0.045 | an accent washing a surface |
| alpha.toneContainer | 0.10 | background behind a tone's content |
| alpha.toneContainerStrong | 0.18 | …when the content needs to sit on it |
| alpha.disabledContent | 0.38 | disabled text and glyphs |
| alpha.supportingText | 0.80 | supporting copy |
| alpha.edgeLight | 0.22 | inner highlight along a panel's edge |
| alpha.haloGlow | 0.07 | ambient glow behind an accented panel |

## Motion

Motion explains a transition or confirms a state change. Anything longer than
`motion.deliberate` is decoration and is not allowed.

| Token | Value | Used for |
|---|---|---|
| motion.instant | 90ms | a colour turning over |
| motion.quick | 160ms | a control responding |
| motion.standard | 240ms | a container resizing |
| motion.deliberate | 360ms | the longest motion the app permits |

Ambient light is the single exception: the welcome surface drifts over 24 seconds,
because it is a surface, not a transition.

## Colour

- **Accent** follows the user's theme (`MaterialTheme.colorScheme.primary`). There is no
  single hex for it, on purpose.
- **Status colours are fixed**, because Material You can turn a semantic hue into a
  pastel that reads as decoration, and a warning that looks decorative is a bug:

| Tone | Dark surface | Light surface |
|---|---|---|
| positive | #5FD9AC | #0B6B4F |
| caution | #FFB86B | #8A5200 |
| critical | #FF9A90 | #9A1B1B |

- A tone is **never the only carrier of meaning**: every component that renders a tone
  also renders a label, and usually an icon.
- A value the app does not know renders as `—`. Never `0`, never `--%`: a zero is a
  measurement.

## Typography

| Role | Family | Reserved for |
|---|---|---|
| Display | Space Grotesk | screen titles, section headings, the wordmark |
| Body | Manrope | synopses, notes, settings copy |
| Mono | JetBrains Mono | live values — page numbers, sizes, percentages |

All three are bundled as static instances (SIL Open Font License) instead of being fetched
through Play Services, so the `foss` build and a device without Google services render
identically.

## Voice

- A heading says what the thing is; a supporting line says why it matters. No exclamation
  marks, no "awesome", no empty encouragement.
- Numbers are written in mono so they do not reflow while changing.
- Error copy states what happened and what the user can do; it never blames them.
