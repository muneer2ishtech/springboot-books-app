#

Known issues that have been confirmed by testing but not yet fixed. Each entry has enough detail to reproduce and fix without re-investigating.

---

## 1. A GitHub release with a JDK-variant tag (`vx.y.z-jdkNN`) doesn't publish a Docker image

**Status:** Open. Found by reading `.github/workflows/cicd.yml` and GitHub's documentation; not yet reproduced, because no JDK-variant release has been made yet.
**Impact:** Medium — a JDK-variant release (`.claude/rules/versions-and-releases.md`, section "Releasing a JDK variant") publishes no Docker image, and nothing fails visibly, because the workflow doesn't run at all.
**Affects:** `.github/workflows/cicd.yml`, trigger `on.push.tags`. The same trigger is in springboot-multi-db and springboot-multi-port, whose `KNOWN-ISSUES.md` link to this entry. springboot-oms and ishtech-springboot-jwtauth trigger on `release: published` and aren't affected.

### Description

The workflow pushes the Docker image only when it runs for a pushed tag that matches `v[0-9]+.[0-9]+.[0-9]+`, or for a manual deploy (`workflow_dispatch` with `manual_deploy=true`). A GitHub tag filter must match the whole tag name: GitHub's "Workflow syntax for GitHub Actions", section "Filter pattern cheat sheet", uses `v2*` to match names that start with `v2`. A JDK-variant tag such as `v0.7.0-jdk21` has a suffix after the patch number, so it doesn't match, and no workflow run starts for it. Release tags without a suffix, such as `v0.7.0`, aren't affected.

### Steps to reproduce

1. On `dev-jdk21`, set a release version, for example `0.7.0-jdk21`, and push it.
2. Publish a GitHub release with the tag `v0.7.0-jdk21` on that commit.

Expected: a workflow run for the tag, which builds and pushes `muneer2ishtech/ishtech-springboot-books-app:0.7.0-jdk21` to Docker Hub.
Actual (according to GitHub's documentation): no workflow run for the tag, and no image.

### Likely cause

`on.push.tags: ['v[0-9]+.[0-9]+.[0-9]+']` in `.github/workflows/cicd.yml` allows no suffix after the version number.

### Suggested fix

1. Trigger the release on `release: types: [published]` and validate `github.event.release.tag_name` against the project version, as `springboot-oms/.github/workflows/cicd.yml` does. This matches the libraries and the release rules, which are based on a published GitHub release. Alternatively, add a second tag pattern, `'v[0-9]+.[0-9]+.[0-9]+-jdk[0-9]+'`.
2. Apply the same fix in springboot-multi-db and springboot-multi-port.
3. Verify with the next JDK-variant release: the workflow runs for the tag, and the image tag is on Docker Hub.
