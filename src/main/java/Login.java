/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


/**
 *
 * @author Student
 */
import java.util.regex.Pattern;

/**
 * Login.java
 
 * This class handles registering a new user (username, password, South
 * African cell phone number) and logging that user in again.
 *
 * Reference used to build the regular-expression based cell phone
 * validation approach:
 * Oracle. (2024). Class Pattern. Oracle Java SE 21 Documentation.
 * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
 */
public class Login {

    // Stored account details (captured once registration succeeds)
    private String userName;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Tracks the outcome of the most recent loginUser() call so that
    // returnLoginStatus() can report on it.
    private boolean lastLoginSuccessful;

    // Regex: username must contain an underscore and be no more than 5
    // characters long in total.
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^(?=.*_).{1,5}$");

    // Regex: at least 8 characters, at least one capital letter, at least
    // one digit and at least one special (non-alphanumeric) character.
    private static final Pattern PASSWORD_UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern PASSWORD_DIGIT = Pattern.compile(".*[0-9].*");
    private static final Pattern PASSWORD_SPECIAL = Pattern.compile(".*[^a-zA-Z0-9].*");

    // Regex: South African cell number - must start with the international
    // country code (+27) followed by 9 digits (the number without the
    // leading 0), giving a maximum total length of 10 characters after
    // the country code is applied.
    private static final Pattern CELL_PATTERN = Pattern.compile("^\\+27[0-9]{9}$");

    public Login() {
        this.lastLoginSuccessful = false;
    }

    // Convenience constructor used by the console app so first/last name
    // can be captured alongside the account credentials.
    public Login(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Checks that the username contains an underscore and is no more
     * than five characters long.
     */
    public boolean checkUserName(String userName) {
        if (userName == null) {
            return false;
        }
        return USERNAME_PATTERN.matcher(userName).matches();
    }

    /**
     * Checks that the password is at least eight characters long and
     * contains a capital letter, a number and a special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        return PASSWORD_UPPERCASE.matcher(password).matches()
                && PASSWORD_DIGIT.matcher(password).matches()
                && PASSWORD_SPECIAL.matcher(password).matches();
    }

    /**
     * Checks that the cell phone number contains the international
     * country code (+27) followed by the number, with no more than
     * ten characters after the country code.
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        return CELL_PATTERN.matcher(cellPhoneNumber).matches();
    }

    /**
     * Registers a user: validates the username, password and cell
     * phone number, stores them if valid, and returns the appropriate
     * message to the caller.
     */
    public String registerUser(String userName, String password, String cellPhoneNumber) {

        boolean userNameOk = checkUserName(userName);
        boolean passwordOk = checkPasswordComplexity(password);
        boolean cellOk = checkCellPhoneNumber(cellPhoneNumber);

        if (!userNameOk) {
            return "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length.";
        }

        if (!passwordOk) {
            return "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, a "
                    + "capital letter, a number, and a special character.";
        }

        if (!cellOk) {
            return "Cell phone number incorrectly formatted or does not "
                    + "contain international code.";
        }

        // All conditions met - store the account details.
        this.userName = userName;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;

        return "Username successfully captured. Password successfully "
                + "captured. Cell phone number successfully added. "
                + "User registered successfully.";
    }

    /**
     * Verifies that the supplied username and password match the
     * details captured during registration.
     */
    public boolean loginUser(String userName, String password) {
        boolean success = this.userName != null
                && this.password != null
                && this.userName.equals(userName)
                && this.password.equals(password);

        this.lastLoginSuccessful = success;
        return success;
    }

    /**
     * Returns the message for the most recent login attempt.
     */
    public String returnLoginStatus() {
        if (lastLoginSuccessful) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    // Individual message helpers, useful for the console app / tests
    // where each field is checked separately rather than all at once.

    public String checkUserNameMessage(String userName) {
        return checkUserName(userName)
                ? "Username successfully captured."
                : "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more "
                    + "than five characters in length.";
    }

    public String checkPasswordMessage(String password) {
        return checkPasswordComplexity(password)
                ? "Password successfully captured."
                : "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, a "
                    + "capital letter, a number, and a special character.";
    }

    public String checkCellPhoneMessage(String cellPhoneNumber) {
        return checkCellPhoneNumber(cellPhoneNumber)
                ? "Cell phone number successfully added."
                : "Cell phone number incorrectly formatted or does not "
                    + "contain international code.";
    }

    // Getters, mainly for use in the console app / tests.
    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}