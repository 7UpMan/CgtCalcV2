Upgrade to Java 21 (LTS)

Summary
-------
This PR upgrades the project to Java 21 (LTS). It updates the Maven build to compile with Java 21 and fixes a small deprecation in the codebase detected when compiling with the newer JDK.

What I changed
--------------
- pom.xml
  - Set `maven.compiler.release` to `21`.
  - Added `maven-compiler-plugin` (3.11.0) with `<release>${maven.compiler.release}</release>` to ensure reliable builds.
- src/main/java/cgtcalc/Tools.java
  - Replaced deprecated `BigDecimal.ROUND_HALF_UP` with `RoundingMode.HALF_UP`.
  - Minor utility-class style improvements (private constructor, modifier order, small cleanup).
- README.md
  - Updated build instructions and requirements (Java 21).
- .github/workflows/maven.yml
  - Added GitHub Actions workflow that builds on Java 21.
- .gitignore and PUBLISH-GITHUB.md
  - Added helper files for repository setup and publishing.

Why
---
Java 21 is the current LTS release and contains performance and language improvements. Upgrading ensures compatibility with new environments and makes it possible to use the latest Java features in future development.

Verification performed
----------------------
- Built the project locally using Java 21 (OpenJDK):
  - `mvn -U clean package` — BUILD SUCCESS
- Ran compile with `-Xlint:deprecation` and fixed a deprecation in `Tools.java`.

Files to review closely
-----------------------
- `Tools.java` — small behavior-preserving refactor; ensure it meets style preferences.
- `pom.xml` — verify that CI and local environments have JDK 21 available.

PR checklist
------------
- [ ] I have run mvn -U clean package locally with Java 21.
- [ ] All tests pass locally.
- [ ] CI (GitHub Actions) expected to run on push/PR; ensure Actions are enabled for the repository.
- [ ] No secrets or sensitive data added.

Notes / Follow-ups
------------------
- Consider scanning the codebase for additional deprecated or legacy APIs (e.g., java.util.Date -> java.time migration).
- Add a LICENSE file if the project will be published publicly.

If you prefer, I can open a PR body in GitHub for you (requires pushing a branch) — tell me the remote/repo name and I can craft the PR directly.