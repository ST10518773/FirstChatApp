# PROG5121 PoE — Part 1: Registration and Login Feature

A console-only Java application (no GUI, no `JOptionPane`) that lets a user
register an account and log in, built to satisfy the Part 1 brief.

## Project structure

```
PROG5121-Part1/
├── pom.xml                          # Maven build file (JUnit 5 wired up)
├── .github/workflows/maven.yml      # GitHub Actions: runs `mvn test` on every push
├── src/
│   ├── main/java/
│   │   ├── Login.java                # Registration/login logic (the required methods)
│   │   └── Main.java                 # Console app that drives registration + login
│   └── test/java/
│       └── LoginTest.java            # JUnit 5 tests, using the brief's exact test data
└── README.md
```

## What `Login.java` implements

| Method | Purpose |
|---|---|
| `boolean checkUserName(String)` | Username must contain `_` and be ≤ 5 characters. |
| `boolean checkPasswordComplexity(String)` | ≥ 8 chars, 1 capital, 1 number, 1 special character. |
| `boolean checkCellPhoneNumber(String)` | Must start with `+27` and be followed by the number (regex-based). |
| `String registerUser(String, String, String)` | Runs all three checks, stores the account if valid, returns the matching message. |
| `boolean loginUser(String, String)` | Compares supplied credentials to the stored account. |
| `String returnLoginStatus()` | Returns the welcome/failure message for the last login attempt. |

## Running it

Tested and confirmed working in NetBeans — `Login.java` and `Main.java` sit
in the same default package of one project, and the console app runs and
registers/logs in a user as expected.

```bash
# Run the console app
mvn compile exec:java -Dexec.mainClass="Main"

# Run the unit tests
mvn test
```

In **NetBeans**: open the folder as a Maven project, right-click `Main.java`
→ Run File, and right-click `LoginTest.java` → Run File to execute the tests.

## Test data used (matches the brief exactly)

| Check | Valid test data | Invalid test data |
|---|---|---|
| Username | `kyl_1` | `kyle!!!!!!` |
| Password | `Ch&&sec@ke99!` | `password` |
| Cell number | `+27838968976` | `08966553` |


# PROG5121 PoE — Part 2: QuickChat 

A console-only Java application (no GUI, no `JOptionPane`) built for the
PROG5121 Programming 1A Portfolio of Evidence.

- **Part 1 — Registration and Login:** a user registers with a username,
  password and South African cell number, then logs in.
- **Part 2 — Sending Messages:** once logged in, the user can send, discard
  or store messages in **QuickChat**. Stored messages are saved to a JSON file.

## Project structure

```
├── pom.xml                          # Maven build file (JUnit 5 + Gson)
├── .github/workflows/maven.yml      # GitHub Actions: runs `mvn test` on every push
└── src/
    ├── main/java/
    │   ├── Login.java                # Part 1: registration/login logic
    │   ├── Message.java              # Part 2: message validation, hash, send/store
    │   └── Main.java                 # Console app: login, then the QuickChat menu
    └── test/java/
        ├── LoginTest.java            # Part 1 unit tests
        └── MessageTest.java          # Part 2 unit tests
```

## Part 1 — `Login.java`

| Method | Purpose |
|---|---|
| `boolean checkUserName(String)` | Username must contain `_` and be ≤ 5 characters. |
| `boolean checkPasswordComplexity(String)` | ≥ 8 chars, 1 capital, 1 number, 1 special character. |
| `boolean checkCellPhoneNumber(String)` | Must start with `+27` followed by the number (regex-based). |
| `String registerUser(...)` | Runs all checks, stores the account if valid, returns the matching message. |
| `boolean loginUser(String, String)` | Compares credentials to the stored account. |
| `String returnLoginStatus()` | Returns the welcome / failure message for the last login attempt. |

## Part 2 — `Message.java`

| Method | Purpose |
|---|---|
| `boolean checkMessageID()` | Message ID must be no more than ten characters. |
| `String checkRecipientCell()` | Validates the recipient number (reuses `Login.checkCellPhoneNumber` from Part 1). |
| `String createMessageHash()` | Builds the hash: first two ID digits `:` message number `:` first + last word, in capitals (e.g. `00:0:HITONIGHT`). |
| `String sentMessage(int)` | Handles the user's choice: 1 = send, 2 = disregard, 3 = store. |
| `static String checkMessageLength(String)` | "Message ready to send." or "Message exceeds 250 characters by X; please reduce the size." |
| `static String generateMessageID()` | Random ten-digit message ID. |
| `String printMessages()` | Full details in order: Message ID, Message Hash, Recipient, Message. |
| `static int returnTotalMessages()` | Total number of messages sent. |
| `void storeMessage()` | Saves the message to `messages.json` using Gson. |

### How the QuickChat flow works

1. The user registers and logs in (Part 1). Messaging is only available after a successful login.
2. The app shows **"Welcome to QuickChat."** and asks how many messages the user wants to enter.
3. A numeric menu runs until the user quits:
   1) Send Messages 2) Show recently sent messages ("Coming Soon.") 3) Quit
4. For each message the user enters a recipient (re-asked until valid) and message text (re-asked until it is 250 characters or fewer). The message ID and hash are generated automatically.
5. The user then chooses to send, disregard (press 0 to delete) or store the message. A sent message prints its full details, and the total number sent is shown once all messages are done.

## Running it

```bash
# Run the unit tests
mvn test

# Run the console app
mvn compile exec:java -Dexec.mainClass="Main"
```

In **NetBeans**: open as a Maven project, right-click `Main.java` → Run File.
Right-click `LoginTest.java` / `MessageTest.java` → Test File to run the tests.

Part 2 uses the **Gson** library to write JSON, so make sure the Gson
dependency in `pom.xml` has downloaded (right-click the project → Reload POM / Clean and Build).

## Test data used (from the briefs)

| Check | Valid | Invalid |
|---|---|---|
| Username | `kyl_1` | `kyle!!!!!!` |
| Password | `Ch&&sec@ke99!` | `password` |
| Login cell number | `+27838968976` | `08966553` |
| Recipient (Message 1) | `+27718693002` | `08575975889` |
| Message 1 | "Hi Mike, can you join us for dinner tonight?" → hash `00:0:HITONIGHT` | |

## Git workflow

Part 2 was developed on a feature branch so `main` always stays in working order:

```bash
git checkout -b Part2-SendingMessages
# ...commit as each piece is added...
git push -u origin Part2-SendingMessages
# then open a Pull Request on GitHub and merge into main
```

## References

- Google. (n.d.). *Gson User Guide*. GitHub. https://github.com/google/gson/blob/main/UserGuide.md
- Oracle. (2024). *Class Pattern*. Oracle Java SE 21 Documentation. https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html

- 
# PROG5121 PoE: QuickChat (Parts 1, 2 and 3)

A console-only Java application (no GUI, no `JOptionPane`) built for the
PROG5121 Programming 1A Portfolio of Evidence.

- **Part 1: Registration and Login.** A user registers with a username, password and South African cell number, then logs in.
- **Part 2: Sending Messages.** Once logged in, the user can send, discard or store messages. Stored messages are saved to a JSON file.
- **Part 3: Store Data and Display Task Report.** Messages are kept in arrays, and a **Stored Messages** menu lets the user search, delete and report on them.

## Project structure

```
├── pom.xml                          # Maven build file (JUnit 5 + Gson)
├── .github/workflows/maven.yml      # GitHub Actions: runs `mvn test` on every push
└── src/
    ├── main/java/com/mycompany/myfirstchatapp/
    │   ├── Login.java                # Part 1: registration/login logic
    │   ├── Message.java              # Part 2: message validation, hash, send/disregard/store
    │   ├── MessageManager.java       # Part 3: the arrays and the Stored Messages features
    │   └── Main.java                 # Console app: login, then the QuickChat menus
    └── test/java/com/mycompany/myfirstchatapp/
        ├── LoginTest.java            # Part 1 unit tests
        ├── MessageTest.java          # Part 2 unit tests
        └── MessageManagerTest.java   # Part 3 unit tests
```

## Part 3: the arrays (`MessageManager.java`)

All arrays are filled while the app runs, so nothing is hard-coded.

| Array | Contents |
|---|---|
| `sentMessages` | Every message the user sent. |
| `disregardedMessages` | Every message the user disregarded. |
| `storedMessages` | Stored messages, read from `messages.json` when the app starts and added to as new ones are stored. |
| `messageHashes` | The hash of every message. |
| `messageIDs` | The ID of every message. |

Each array has a counter that tracks how many items it holds. Deleting a stored message shifts the later items one place to the left, and the change is saved back to `messages.json`.

## Main menu

1. Send Messages
2. Show recently sent messages (a report of the sent messages: hash, recipient, message)
3. Quit
4. **Stored Messages** (Part 3)

## Stored Messages menu

| Option | What it does |
|---|---|
| 1 | Displays the sender and recipient of all stored messages. |
| 2 | Displays the longest stored message. |
| 3 | Searches for a message ID and displays the recipient and message. |
| 4 | Searches for all messages sent or stored for a particular recipient. |
| 5 | Deletes a stored message using its message hash. |
| 6 | Displays a report of the full details of all stored messages. |
| 7 | Goes back to the main menu. |

## Part 2: `Message.java`

| Method | Purpose |
|---|---|
| `boolean checkMessageID()` | The message ID must be no more than ten characters. |
| `String checkRecipientCell()` | Validates the recipient number (reuses `Login.checkCellPhoneNumber` from Part 1). |
| `String createMessageHash()` | First two ID digits `:` message number `:` first and last word, in capitals (for example `00:0:HITONIGHT`). |
| `String sentMessage(int)` | Handles the choice: 1 = send, 2 = disregard, 3 = store. |
| `static String checkMessageLength(String)` | "Message ready to send." or "Message exceeds 250 characters by X; please reduce the size." |
| `static String generateMessageID()` | Random ten-digit message ID. |

## Part 1: `Login.java`

| Method | Purpose |
|---|---|
| `boolean checkUserName(String)` | Username must contain `_` and be 5 characters or fewer. |
| `boolean checkPasswordComplexity(String)` | At least 8 characters, 1 capital, 1 number, 1 special character. |
| `boolean checkCellPhoneNumber(String)` | Must start with `+27` followed by the number (regex-based). |
| `String registerUser(...)` | Runs all the checks, stores the account if valid, and returns the matching message. |
| `boolean loginUser(String, String)` | Compares the credentials to the stored account. |
| `String returnLoginStatus()` | Returns the welcome or failure message for the last login attempt. |

## Running it

```bash
# Run the unit tests
mvn test

# Run the console app
mvn compile exec:java -Dexec.mainClass="com.mycompany.myfirstchatapp.Main"
```

In **NetBeans**: right-click the project, then Clean and Build, then run it with the green arrow.
To run the tests, right-click the project and choose Test.

The app creates `messages.json` in the project folder the first time a message is stored.

## Unit tests

Run with `mvn test` (or right-click the project in NetBeans and choose Test).
`MessageManagerTest` covers the arrays, every Stored Messages feature, the
reports, the session summary and edge cases such as empty arrays and deleting
the same message twice. GitHub Actions runs the same tests on every push.

## Test data used (from the briefs)

| Part | Check | Test data |
|---|---|---|
| 1 | Username | `kyl_1` (valid), `kyle!!!!!!` (invalid) |
| 1 | Password | `Ch&&sec@ke99!` (valid), `password` (invalid) |
| 2 | Recipient / hash | `+27718693002`, "Hi Mike, can you join us for dinner tonight?" gives `00:0:HITONIGHT` |
| 3 | Message 1 (Sent) | `+27834557896`, "Did you get the cake?" |
| 3 | Message 2 (Stored) | `+27838884567`, "Where are you? You are late! I have asked you to be on time." |
| 3 | Message 3 (Disregard) | `+27834484567`, "Yohoooo, I am at your gate." |
| 3 | Message 4 (Sent) | `0838884567`, "It is dinner time!" |

## Git workflow

Each part was developed on its own feature branch so `main` always stays in working order:

```bash
git checkout -b Part3-StoreAndDisplay
# ...commit as each piece is added...
git push -u origin Part3-StoreAndDisplay
# then open a Pull Request on GitHub and merge it into the main branch
```

## References

- Google. (n.d.). *Gson User Guide*. GitHub. https://github.com/google/gson/blob/main/UserGuide.md
- Oracle. (2024). *Class Pattern*. Oracle Java SE 21 Documentation. https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
