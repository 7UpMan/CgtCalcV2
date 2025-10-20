CgtCalcV2
=========

This project (CgtCalcV2) has been updated to target Java 21 (LTS).

Summary of important changes
- pom.xml: set `maven.compiler.release` to `21` and added the `maven-compiler-plugin` to ensure builds use Java 21.
- `src/main/java/cgtcalc/Tools.java`: fixed deprecated BigDecimal rounding usage and applied small utility-class style improvements.

Required environment
- Java 21 JDK installed. Example path used in these instructions: `/usr/lib/jvm/java-21-openjdk-amd64`.
- Maven (tested with Apache Maven 3.8.7).

Build instructions
1. Ensure JAVA_HOME points to a Java 21 JDK and is on your PATH:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

2. Build and run tests:

```bash
mvn -U clean package
```

Artifacts
- Built JAR: `target/CgtCalcV2-1.0-SNAPSHOT.jar`

Committing the upgrade (local Git)
If you're working from a local clone, use the following commands to create a branch and commit the upgrade changes:

```bash
# create a branch
git checkout -b upgrade/java-21

# stage files
git add pom.xml src/main/java/cgtcalc/Tools.java README.md

# commit
git commit -m "Upgrade to Java 21: set maven.compiler.release=21 and fix deprecations in Tools.java"

# push the branch
git push -u origin upgrade/java-21
```

Notes & next steps
- A deprecation warning in `Tools.java` was resolved by switching BigDecimal rounding to `RoundingMode.HALF_UP`. There may be additional modernization opportunities, such as migrating old date/time APIs to `java.time`.
- If you want, I can prepare a PR description, changelog entry, or scan the codebase for other deprecated APIs.
