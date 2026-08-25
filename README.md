# Twitter Simulation

A simplified simulation of core Twitter functionalities, built to demonstrate fundamental object-oriented programming principles and system modeling. This project is ideal for students, hobbyists, or developers interested in understanding how social networking platforms like Twitter manage users, tweets, timelines, and social interactions at a basic level.

## 📌 Overview

This simulation models a miniature Twitter-like ecosystem where users can:
- Create and manage their accounts
- Follow or unfollow other users
- Post, view, and delete tweets
- Receive a personalized timeline of tweets from followed users

It aims to provide hands-on practice with data structures, OOP concepts (like encapsulation and inheritance), and system interaction patterns in a controlled environment.

## 🧱 Features

### User System
- Create new users with unique usernames
- Maintain lists of followers and following
- Profile stats including tweet count and connections

### Tweet System
- Post tweets with timestamps
- Delete tweets from one's own timeline
- View tweet history by user

### Follow/Unfollow Logic
- Establish follow relationships
- Automatically update feeds with new tweets from followed users
- Remove tweets from unfollowed users in feed

### Feed Generation
- Aggregate tweets from all followed accounts
- Sort feed chronologically
- Optionally display metadata (e.g., user, timestamp)

## 🛠️ Technologies Used

- **Java** (Core logic and OOP modeling)
- **Java Collections Framework** for managing lists, maps, and queues
- **JUnit 4** for unit tests

## 🔐 Security Notes

- Passwords are stored as SHA-256 hashes (`sha256:...`) for new accounts and password changes.
- Existing plain-text passwords from older data files still work on login and are upgraded to hashes automatically.

## 🧪 Running Tests

From the project root:

```bash
javac -encoding UTF-8 -cp "src/library/json-simple-1.1.1.jar;src/library/junit-4.13.2.jar;src/library/hamcrest-core-1.3.jar" -d out src/twitter/**/*.java test/twitter/**/*.java
java -cp "out;src/library/junit-4.13.2.jar;src/library/hamcrest-core-1.3.jar;src/library/json-simple-1.1.1.jar" org.junit.runner.JUnitCore twitter.model.TimeTest twitter.model.RecordTest twitter.utils.PasswordHasherTest twitter.repository.JsonFileHelperTest
```

In IntelliJ, right-click the `test` folder and choose **Run 'All Tests'**.
