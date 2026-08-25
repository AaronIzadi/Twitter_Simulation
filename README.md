# 🐦 Twitter Simulation

A **command-line Twitter-like social network simulation** built in **Java** ☕.

This project demonstrates **object-oriented design**, a **state-based CLI**, **JSON file persistence**, and common social-network functionality such as follows, tweets, timelines, privacy, moderation, and user interactions.

---

## ✨ Overview

Users interact with the application through a text-based menu to:

- 👤 Create accounts and manage profiles
- 🔐 Log in and authenticate securely
- 📝 Post tweets and replies
- 📰 Browse personalized timelines
- 🤝 Follow and interact with other users
- 🔒 Manage public/private account visibility
- 🚫 Block and mute users
- 💾 Save tweets
- 📊 View engagement and social activity

All persistent data is stored as **JSON files** on disk, while application activity is recorded in:

`src/resources/log/logging.txt` 📝

---

## 🚀 Features

### 👤 Accounts & Authentication

- 🆕 Create accounts with username/password validation
- 🔐 SHA-256 password hashing
- 🔄 Automatic upgrade from legacy plain-text passwords
- ✏️ Edit profile information:
  - Name
  - Bio
  - Email
  - Phone
  - Username
  - Password
  - Account visibility
- 🌍 Public & 🔒 private accounts
- 📩 Follow requests for private accounts
- 🚪 Log out
- 🗑️ Delete account

### 🤝 Social Graph

- ➕ Follow public accounts
- ➖ Unfollow users
- 📩 Send follow requests
- ✅ Accept follow requests
- ❌ Decline follow requests
- 🚫 Cancel pending requests
- 👥 View followers & following
- 🚷 Block users
- 🔇 Mute users

### 🐦 Tweets & Interactions

- ✍️ Compose tweets and replies
- 📏 280-character tweet limit
- 📰 Personalized timeline based on:
  - Your tweets
  - Following activity
  - Likes ❤️
  - Retweets 🔁
- ❤️ Like tweets
- 🔁 Retweet tweets
- 💾 Save tweets
- 🗑️ Delete tweets
- 💬 Reply to tweets
- 📋 View tweet lists
- ❤️ View likes
- 🔁 View retweets
- 💬 Browse comment threads

### 🔎 Other Features

- 🔍 Search for users
- 👤 View user profiles
- 💾 Manage saved tweets
- 📜 Structured application logging
- 👤 Session-aware logging with the current username

---

## 🏗️ Architecture

The application follows a layered architecture with clear separation of responsibilities:

| 📦 Layer | 🎯 Responsibility |
|---|---|
| `state` | 🖥️ CLI screens and navigation using the State Pattern |
| `logic` | 🧠 Business rules and managers such as `AccountManager`, `TweetManager`, and `TimeLineManager` |
| `repository` | 💾 JSON file persistence |
| `model` | 🧩 Domain objects such as `Account`, `Tweet`, `Time`, and `Record` |
| `utils` | 🛠️ Logging, paths, validation, and password hashing |

### ♻️ Reusable Tweet Actions

Common tweet operations are centralized in `TweetActionHandler`, including:

❤️ Like  
🔁 Retweet  
💾 Save  
💬 Reply  
🗑️ Delete  

These actions are reused across:

- 📰 Timeline views
- 👤 Profile tweet lists
- 💬 Reply/comment views

This keeps the codebase **DRY, reusable, and maintainable**.

---

## 📋 Requirements

- ☕ **Java 8 or newer**
- 🐘 **Gradle** — optional but recommended
- 💡 **IntelliJ IDEA** — optional

---

## ▶️ Running the Application

### 🐘 Gradle — Recommended

From the project root:

```bash
gradle run
```

The `run` task uses the project directory as the working directory so that application data files resolve correctly.

### 💡 IntelliJ IDEA

1. 📂 Open the project.
2. ▶️ Run `twitter.main.Main`.
3. ⚙️ Set the working directory to the project root:
   `Twitter_Simulation`

---

## 💾 Data Storage

Application data is stored under `src/resources/`:

```text
src/resources/
├── data/
│   ├── account/     👤 Account JSON files
│   └── tweet/       🐦 Tweet JSON files
├── app info/        🔢 ID counters
└── log/
    └── logging.txt  📜 Application logs
```

Paths are resolved automatically by `AppPaths` using the following strategy:

1. 🌎 Check the `TWITTER_SIM_HOME` environment variable
2. 🔎 Walk up from the current directory to find `src/resources`
3. 📁 Fall back to `./src/resources`

### Example

```bash
set TWITTER_SIM_HOME=C:\path\to\Twitter_Simulation
gradle run
```

---

## 🔐 Security

The application includes basic password security and input validation:

- 🔒 New and changed passwords are stored as `sha256:...` hashes
- 🔄 Existing plain-text passwords remain compatible and are automatically upgraded after login
- ✅ Input validation for:
  - Usernames
  - Passwords
  - Tweets
  - Email addresses
  - Phone numbers

---

## 📜 Logging

Application events are written to a structured log file.

### Log Format

```text
yyyy-MM-dd HH:mm:ss LEVEL  [user=username] message
```

### Log Levels

- 🟢 `INFO`
- 🟡 `WARN`
- 🔴 `ERROR`

State transitions and user actions are logged with structured messages, making application activity easier to trace and debug.

---

## 🧪 Running Tests

### 🐘 Gradle

```bash
gradle test
```

### 💻 Command Line — Without Gradle

From the project root on Windows:

```powershell
javac -encoding UTF-8 -cp "src/library/json-simple-1.1.1.jar;src/library/junit-4.13.2.jar;src/library/hamcrest-core-1.3.jar" -d out (Get-ChildItem -Recurse -Filter "*.java" src,test | ForEach-Object { $_.FullName })

java -cp "out;src/library/junit-4.13.2.jar;src/library/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore twitter.utils.InputValidatorTest twitter.logic.AccountManagerTest twitter.logic.TimeLineManagerTest twitter.repository.AccountFileRepositoryTest twitter.model.TimeTest twitter.model.RecordTest twitter.model.TweetEngagementTest twitter.utils.PasswordHasherTest twitter.utils.AppPathsTest twitter.repository.JsonFileHelperTest twitter.utils.LoggerTest
```

### 💡 IntelliJ IDEA

Right-click the `test` folder and select:

**Run → 'All Tests'** 🧪

---

## 📁 Project Structure

```text
src/
└── twitter/
    ├── main/          🚀 Context & Main entry point
    ├── state/         🖥️ CLI states & TweetActionHandler
    ├── logic/         🧠 Business logic & managers
    ├── model/         🧩 Domain models
    ├── repository/    💾 File repositories & JSON helpers
    └── utils/         🛠️ Logger, AppPaths, validation & hashing

    resources/
    ├── data/          💾 Persisted accounts & tweets
    ├── app info/      🔢 ID counters
    └── log/           📜 Log files

test/
└── twitter/           🧪 JUnit tests

build.gradle           🐘 Gradle build configuration
```

---

## 🛠️ Technologies

| Technology | Purpose |
|---|---|
| ☕ **Java** | Core application |
| 📦 **json-simple** | JSON-based persistence |
| 🧪 **JUnit 4** | Unit testing |
| 🐘 **Gradle** | Build & test automation |

---

## 🎯 What This Project Demonstrates

This project brings together several important software engineering concepts:

**☕ Java** · **🧱 OOP** · **🏗️ Layered Architecture** · **🔄 State Pattern** · **💾 Persistence** · **🔐 Password Hashing** · **🧪 Unit Testing** · **📜 Structured Logging** · **🧹 Code Reusability**

> 🐦 A small Twitter-like application, but a pretty solid playground for practicing **object-oriented design and software architecture**.
