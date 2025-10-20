Upgrade to Java 21

This repository was updated to target Java 21.

Files changed:
- pom.xml: maven.compiler.release set to 21 and maven-compiler-plugin added
- src/main/java/cgtcalc/Tools.java: replaced deprecated BigDecimal.ROUND_HALF_UP with RoundingMode.HALF_UP and small style fixes

How to commit locally

Run the following commands in your local clone of the repository:

```bash
# create branch
git checkout -b upgrade/java-21

# add changes
git add pom.xml src/main/java/cgtcalc/Tools.java

# commit
git commit -m "Upgrade to Java 21: set maven.compiler.release=21 and fix deprecation in Tools.java"

# push branch (optional)
git push -u origin upgrade/java-21
```

If you need me to prepare a full README update or additional commit content (CHANGELOG, etc.), tell me and I'll add it here.
