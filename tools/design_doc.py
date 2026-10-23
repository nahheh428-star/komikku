#!/usr/bin/env python3
"""Fail when docs/DESIGN.md and the design tokens disagree.

A design document that is maintained by hand drifts: a radius is tuned in code,
the table keeps the old number, and six months later the document describes an
app that does not exist. This checker reads the numbers out of
`yokai-design/.../YokaiTokens.kt` and refuses to let the document quote anything
else.

    python3 tools/design_doc.py --assert   # exit 1 on drift (CI)
    python3 tools/design_doc.py            # print a report, never fail
"""

from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
TOKENS = ROOT / "yokai-design/src/main/java/yokai/design/YokaiTokens.kt"
DOC = ROOT / "docs/DESIGN.md"

# object name -> prefix used in the document's tables
PREFIXES = {
    "YokaiSpace": "space",
    "YokaiRadius": "radius",
    "YokaiSize": "size",
    "YokaiAlpha": "alpha",
    "YokaiMotion": "motion",
}

OBJECT_RE = re.compile(r"^object\s+(Yokai\w+)\s*\{")
DP_RE = re.compile(r"^\s*val\s+(\w+)\s*:\s*Dp\s*=\s*([0-9.]+)\.dp\b")
FLOAT_RE = re.compile(r"^\s*val\s+(\w+)\s*=\s*([0-9.]+)f\b")
INT_RE = re.compile(r"^\s*val\s+(\w+)\s*=\s*([0-9]+)\b")
ROW_RE = re.compile(r"^\|\s*([a-z]+)\.(\w+)\s*\|\s*([^|]+?)\s*\|")


def read_tokens() -> dict[tuple[str, str], tuple[float, str]]:
    """(prefix, name) -> (number, unit), read from the Kotlin source.

    The number is compared numerically: `0.10f` in Kotlin and `0.10` in the
    document are the same value, and a checker that fails on trailing zeros
    would teach everyone to ignore it.
    """
    tokens: dict[tuple[str, str], tuple[float, str]] = {}
    current: str | None = None

    for raw in TOKENS.read_text(encoding="utf-8").splitlines():
        line = raw.strip()

        match = OBJECT_RE.match(line)
        if match:
            current = match.group(1)
            continue
        if current is None:
            continue
        if line.startswith("}"):
            current = None
            continue

        prefix = PREFIXES.get(current)
        if prefix is None:
            continue

        if match := DP_RE.match(raw):
            tokens[(prefix, match.group(1))] = (float(match.group(2)), "dp")
        elif match := FLOAT_RE.match(raw):
            tokens[(prefix, match.group(1))] = (float(match.group(2)), "")
        elif match := INT_RE.match(raw):
            tokens[(prefix, match.group(1))] = (float(match.group(2)), "ms")

    return tokens


def parse_doc_value(raw: str) -> tuple[float, str] | None:
    text = raw.strip().lower()
    for unit in ("dp", "ms"):
        if text.endswith(unit):
            unit = text[-2:]
            text = text[: -2]
            break
    else:
        unit = ""
    try:
        return float(text), unit
    except ValueError:
        return None


def read_doc() -> dict[tuple[str, str], tuple[float, str]]:
    """(prefix, name) -> (number, unit) quoted in the document's tables."""
    quoted: dict[tuple[str, str], tuple[float, str]] = {}
    for line in DOC.read_text(encoding="utf-8").splitlines():
        match = ROW_RE.match(line)
        if not match:
            continue
        value = parse_doc_value(match.group(3))
        if value is not None:
            quoted[(match.group(1), match.group(2))] = value
    return quoted


def main() -> int:
    tokens = read_tokens()
    quoted = read_doc()

    missing = sorted(k for k in tokens if k not in quoted)
    extra = sorted(k for k in quoted if k not in tokens)
    drifted = sorted(
        (k, tokens[k], quoted[k])
        for k in tokens
        if k in quoted and (abs(tokens[k][0] - quoted[k][0]) > 1e-6 or tokens[k][1] != quoted[k][1])
    )

    print(f"tokens: {len(tokens)} in source, {len(quoted)} in docs/DESIGN.md")

    def show(value: tuple[float, str]) -> str:
        return f"{value[0]:g}{value[1]}"

    for prefix, name in missing:
        print(f"missing  {prefix}.{name} = {show(tokens[(prefix, name)])} is not documented")
    for prefix, name in extra:
        print(f"stale    {prefix}.{name} is documented but no longer exists")
    for (prefix, name), expected, actual in drifted:
        print(f"drift    {prefix}.{name}: source {show(expected)}, document {show(actual)}")

    ok = not (missing or extra or drifted)
    print("design doc matches the token source" if ok else "design doc is out of date")

    if "--assert" in sys.argv and not ok:
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
