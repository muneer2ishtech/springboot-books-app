#

Known issues that have been confirmed by testing but not yet fixed. Each entry has enough detail to reproduce and fix without re-investigating.

---

## 1. CI doesn't check that `main` uses a release version and other branches use a SNAPSHOT version

**Status:** Open. Found by reading `.github/workflows/cicd.yml`; not reproduced with a push.
**Impact:** Medium — a SNAPSHOT version can reach `main`, or a release version can be pushed to `dev` or a feature branch, without CI failing. In release step 3 (`.claude/rules/versions-and-releases.md`, section "Release"), the run for the release-version commit on `dev` is expected to fail in "Validate Project Version"; in this repo it passes.
**Affects:** `.github/workflows/cicd.yml`, step "Validate Project Version". Only springboot-books-app: the other applications and the libraries have this check.

### Description

The step "Validate Project Version" only checks runs for a tag: the version must not be a SNAPSHOT, and the tag must match the version. For a push to a branch it checks nothing. The other applications (for example `springboot-multi-db/.github/workflows/cicd.yml`) also check that `main` has a release version, that every other branch has a SNAPSHOT version, and that `main` can't be released by a manual deploy.

### Steps to reproduce

1. On a feature branch, set `version` in `build.gradle.kts` to a release version, for example `0.7.0`, and push.

Expected: the step "Validate Project Version" fails, because a branch other than `main` must use a SNAPSHOT version.
Actual: the step passes.

### Likely cause

The branch rule (block "PUSH / BRANCH RULE" in the other applications' workflows) was never added to this workflow.

### Suggested fix

Add the branch rule from `springboot-multi-db/.github/workflows/cicd.yml`, including the check that `main` can't be released by a manual deploy. Then verify it with a push of a release version to a feature branch, and with a push of a SNAPSHOT version on `dev`.

---

## 2. `PUT /api/v1/users/{userId}` clears fields absent from the request body

See ishtech-springboot-jwtauth [`KNOWN-ISSUES.md`, issue 1](https://github.com/IshTech/ishtech-springboot-jwtauth/blob/dev/KNOWN-ISSUES.md).
