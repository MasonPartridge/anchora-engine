# anchora-engine

The a engine forged within the great starforge of Donnick! The great holy site of the Informita Cult and last remaining starforges in the galaxy after the Denominational Collapse of the 843,322th century.

## Details

Java 2D engine built upon top of the Light Weight Java Game Engine.

## How to Assign an Issue to Yourself

1. Navigate to the **Issues** tab in the GitHub repository.
2. Browse through available issues or create a new one if necessary.
3. Choose an issue you'd like to work on and click on it to open the details.
4. Comment on the issue mentioning that you'd like to handle it.
5. If the maintainers approve, they might assign the issue to you officially.

## Working on a New Feature Fork

1. Ensure you have the latest changes from the original repository:
    - git remote add upstream https://github.com/MasonPartridge/anchora-engine.git
    - git fetch upstream
    - git checkout main
    - git merge upstream/main
2. Create a new fork of the repository for your feature:
    - Click on the "Fork" button on the original repository's GitHub page.
    - Clone your forked repository to your local machine:
        - git clone https://github.com/<your-username>/anchora-engine.git
3. Make changes, write code, and commit your work:
    - git add .
    - git commit -m "Description of changes made"
4. Push your changes to your forked repository:
    - git push origin main
5. Initiate a pull request from your forked repository to the original repository:
    - Go to your forked repository on GitHub.
    - Click on the "New pull request" button.
    - Submit the pull request.
6. If approved, a maintainer will merge your pull request into the `main` branch.

## Commit Messages

### Your commit messages should be formatted as follows:

Short message example:

`type(scope):subject`

Long message example:
```
type(scope):subject
(blank line)
body
```

`type`: Select one of the following 8 commit types. These are your _only_ options for commit type:

- **_feat_**: A new feature for the application user. Rolling out a new module, new piece of functionality, etc.
- **_fix_**: Bug fix to production code. Dealing with GH Issues, fixing a bug, etc.
- **_docs_**: Changes to documentation. Adding a comment, editing a comment, changing README.md files, etc.
- **_style_**: Changes to code formatting. The style of code, reformatting, etc.
- **_refactor_**: Refactor to production code. Upgrading a package and changing code to meet the new demands.
- **_test_**: All things that apply to unit testing. Creating tests, refactoring tests, etc. No changes to production code occur.
- **_chore_**: Updating gradle, adding error handling, or github files. This is developer-facing _only_.
- **_workaround_**: Temporary fix until a more robust solution is found or until other factors are resolved.

`scope`: Narrow the scope of the commit to a one or two word description in parentheses

`subject`: Favor imperative mood, present tense, active voice, and start with verbs. Don't use a period at the end.

`body` (optional): If necessary, provide additional context that can help other developers in the future. This is normally unnecessary but some use cases are:

- If the commit contains a new package you've added to the project
- If the commit contains a change to your build that you need to notate
- If the commit includes changes that would benefit from an explanation and from additional context.
- If the commit is the last in a series that will become a Pull Request and you want to communicate something to other developers and maintiners.

H-happy contributing (or something)! *pouty face* i-it's not like I like your commits
or anything.,. baka >w<

