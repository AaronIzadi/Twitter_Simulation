# Twitter Simulation

A command-line Twitter-like simulation built in Java. It demonstrates object-oriented design, a state-based CLI, file persistence, and common social-network features (follows, tweets, timeline, privacy, and moderation).

## Overview

Users interact through a text menu to create accounts, post tweets, browse timelines, manage profiles, and interact with other users. Data is stored as JSON files on disk. Application activity is logged to `src/resources/log/logging.txt`.

## Features

### Accounts and authentication
- Create accounts and log in with username/password validation
- SHA-256 password hashing with automatic upgrade from legacy plain-text passwords
- Edit profile (name, bio, email, phone, username, password, visibility)
- Public and private accounts with follow requests
- Log out and delete account

### Social graph
- Follow / unfollow public accounts
- Send, accept, decline, and cancel follow requests for private accounts
- View followers and following lists
- Block and mute users

### Tweets and interactions
- Compose tweets and replies (max 280 characters)
- Timeline built from your tweets, following activity, likes, and retweets
- Like, retweet, save, delete, and reply to tweets
- View tweet lists, likes, retweets, and comment threads

### Other
- Search and view user profiles
- Saved tweets
- Structured application logging with session user context

## Architecture

| Layer | Responsibility |
|-------|----------------|
| `state` | CLI screens and navigation (state pattern) |
| `logic` | Business rules (`AccountManager`, `TweetManager`, `TimeLineManager`) |
| `repository` | JSON file persistence |
| `model` | Domain objects (`Account`, `Tweet`, `Time`, `Record`) |
| `utils` | Logging, paths, validation, password hashing |

Shared tweet actions (like, retweet, save, reply, etc.) are handled by `TweetActionHandler` and reused across timeline, profile tweet list, and reply views.

## Requirements

- Java 8 or newer
- Gradle (optional, recommended) or IntelliJ IDEA

## Running the application

### Gradle (recommended)

From the project root:

```bash
gradle run
```

The `run` task uses the project directory as the working directory so data files resolve correctly.

### IntelliJ IDEA

1. Open the project.
2. Run `twitter.main.Main`.
3. Set the working directory to the project root (`Twitter_Simulation`).

### Data location

Application data lives under `src/resources/`:

- `data/account/` — account JSON files
- `data/tweet/` — tweet JSON files
- `app info/` — ID counters
- `log/logging.txt` — application log

Paths are resolved by `AppPaths`, which:
1. Checks the `TWITTER_SIM_HOME` environment variable (project root or `resources` folder)
2. Walks up from the current directory to find `src/resources`
3. Falls back to `./src/resources` relative to the working directory

Example:

```bash
set TWITTER_SIM_HOME=C:\path\to\Twitter_Simulation
gradle run
```

## Security

- New and changed passwords are stored as `sha256:...` hashes.
- Existing plain-text passwords still work on login and are upgraded automatically.
- Input validation is applied for usernames, passwords, tweets, email, and phone numbers.

## Logging

Log format:

```text
yyyy-MM-dd HH:mm:ss LEVEL  [user=username] message
```

Levels: `INFO`, `WARN`, `ERROR`. State transitions and user actions are logged with structured messages.

## Running tests

### Gradle

```bash
gradle test
```

### Command line (without Gradle)

From the project root on Windows:

```bash
javac -encoding UTF-8 -cp "src/library/json-simple-1.1.1.jar;src/library/junit-4.13.2.jar;src/library/hamcrest-core-1.3.jar" -d out (Get-ChildItem -Recurse -Filter "*.java" src,test | ForEach-Object { $_.FullName })

java -cp "out;src/library/junit-4.13.2.jar;src/library/hamcrest-core-1.3.jar;src/library/json-simple-1.1.1.jar" org.junit.runner.JUnitCore twitter.utils.InputValidatorTest twitter.logic.AccountManagerTest twitter.logic.TimeLineManagerTest twitter.repository.AccountFileRepositoryTest twitter.model.TimeTest twitter.model.RecordTest twitter.model.TweetEngagementTest twitter.utils.PasswordHasherTest twitter.utils.AppPathsTest twitter.repository.JsonFileHelperTest twitter.utils.LoggerTest
```

In IntelliJ, right-click the `test` folder and choose **Run 'All Tests'**.

## Project structure

```text
src/
  twitter/
    main/          Context, Main entry point
    state/         CLI states and TweetActionHandler
    logic/         Managers
    model/         Domain models
    repository/    File repositories and JSON helpers
    utils/         Logger, AppPaths, InputValidator, PasswordHasher
  resources/
    data/          Persisted accounts and tweets
    app info/      ID counters
    log/           Log file
test/
  twitter/         JUnit tests
build.gradle       Gradle build (optional)
```

## Technologies

- Java
- json-simple (JSON persistence)
- JUnit 4 (tests)
- Gradle (build and test runner)
