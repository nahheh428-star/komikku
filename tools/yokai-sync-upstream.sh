#!/usr/bin/env bash
#
# Yokai — upstream sync helper.
#
# Komikku ships updates often; this repository is a fork that has to keep up
# without a manual archaeology session every time. The script:
#
#   bash tools/yokai-sync-upstream.sh           fetch, then create a merge branch
#   bash tools/yokai-sync-upstream.sh --check   fetch and report only, change nothing
#
# It never merges into master and never pushes. A merge you can inspect is worth
# more than a merge that "worked".
set -euo pipefail

UPSTREAM_URL="${YOKAI_UPSTREAM_URL:-https://github.com/komikku-app/komikku.git}"
UPSTREAM_BRANCH="${YOKAI_UPSTREAM_BRANCH:-master}"
CHECK_ONLY=0

for arg in "$@"; do
    case "$arg" in
        --check) CHECK_ONLY=1 ;;
        -h|--help) sed -n '2,14p' "$0"; exit 0 ;;
        *) echo "unknown argument: $arg" >&2; exit 2 ;;
    esac
done

cd "$(git rev-parse --show-toplevel)"

if ! git remote get-url upstream >/dev/null 2>&1; then
    git remote add upstream "$UPSTREAM_URL"
    echo "added remote 'upstream' -> $UPSTREAM_URL"
fi

git fetch upstream "$UPSTREAM_BRANCH"

BASE="$(git merge-base HEAD "upstream/$UPSTREAM_BRANCH")"
BEHIND="$(git rev-list --count "HEAD..upstream/$UPSTREAM_BRANCH")"
AHEAD="$(git rev-list --count "upstream/$UPSTREAM_BRANCH..HEAD")"

echo
echo "upstream/$UPSTREAM_BRANCH: $BEHIND commit(s) we do not have"
echo "our branch:                 $AHEAD commit(s) upstream does not have"
echo "merge base:                 $BASE"
echo

if [ "$BEHIND" -eq 0 ]; then
    echo "already up to date."
    exit 0
fi

echo "Files upstream changed that we also touch (the merge candidates):"
TOUCHED="settings.gradle.kts app/build.gradle.kts i18n/src/commonMain/moko-resources/base/strings.xml app/src/main/java/eu/kanade/presentation/theme/TachiyomiTheme.kt"
for f in $TOUCHED; do
    if git diff --name-only "$BASE" "upstream/$UPSTREAM_BRANCH" -- "$f" | grep -q .; then
        echo "  ! $f"
    fi
done
echo

if [ "$CHECK_ONLY" -eq 1 ]; then
    echo "check only: nothing was merged."
    exit 0
fi

BRANCH="sync/upstream-$(date +%Y%m%d-%H%M)"
git switch -c "$BRANCH"
git merge --no-ff --no-commit "upstream/$UPSTREAM_BRANCH" || {
    echo
    echo "Conflicts (expected to be limited to the files listed above):"
    git diff --name-only --diff-filter=U
    echo
    echo "Resolve them, then:"
    echo "  git add -A && git commit"
    echo "  ./gradlew spotlessApply && ./gradlew :app:compileDebugKotlin"
    exit 1
}

echo "Merged cleanly on branch $BRANCH (not committed)."
echo "Review, then: git commit && ./gradlew spotlessApply :app:compileDebugKotlin"
