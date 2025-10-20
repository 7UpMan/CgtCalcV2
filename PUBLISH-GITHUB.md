Publishing this project to GitHub

Follow these steps locally in your clone of the repo to initialize git (if needed), create a GitHub repo, push the code, and enable CI.

1) Initialize git (if your local copy isn't a repo yet)

```bash
cd /path/to/CgtCalcV2
git init
git branch -M main
```

2) Add files and commit

```bash
git add .
git commit -m "Initial commit: upgrade to Java 21 and project files"
```

3) Create a GitHub repository and push

Using GitHub CLI (recommended if you have it):

```bash
# login if needed
gh auth login
# create repo (replace 'your-username' and repo name as needed)
gh repo create your-username/CgtCalcV2 --public --source=. --remote=origin --push
```

Or use the web UI: create an empty repo and follow the instructions to push an existing repo:

```bash
git remote add origin git@github.com:your-username/CgtCalcV2.git
git push -u origin main
```

4) Enable GitHub Actions CI
- The repository already contains a sample workflow at `.github/workflows/maven.yml` which uses Java 21.
- Actions should run automatically after your first push. You can inspect the Actions tab for run logs.

5) Create a pull request
- If you prefer to push a branch (e.g., `upgrade/java-21`) instead of `main`, create the branch locally and push it then open a PR on GitHub.

Notes
- If you plan to share the code publicly, confirm there are no secrets or private data in the repo.
- You may wish to add a LICENSE file.

If you want, I can generate a ready-to-paste GitHub PR description and a small CHANGELOG entry.
