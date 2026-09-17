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

You'll need JDK 17+ and Maven installed locally (this sandbox has a JDK but
no `javac`/Maven binary, so the code hasn't been compiled here — please
compile it once in NetBeans/IntelliJ/VS Code before you submit).

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
