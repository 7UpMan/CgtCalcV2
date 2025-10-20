# Changelog

## [Unreleased]

### Changed
- Upgraded Java runtime target to Java 21 (LTS).
  - `pom.xml`: set `maven.compiler.release` to `21` and added `maven-compiler-plugin` 3.11.0.
  - `src/main/java/cgtcalc/Tools.java`: replaced deprecated `BigDecimal.ROUND_HALF_UP` with `RoundingMode.HALF_UP` and applied minor utility-class cleanups.
  - Added GitHub Actions workflow `.github/workflows/maven.yml` to build on Java 21.

### Notes
- Build verified locally with OpenJDK 21 and Apache Maven 3.8.7: `mvn -U clean package` (BUILD SUCCESS).
- Consider additional modernization (migrate to `java.time` where applicable).
