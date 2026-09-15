/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LoginTest.java
 * PROG5121 - PoE Part 1: Registration and Login feature
 *
 * Unit tests for the Login class, using the exact test data supplied
 * in the PoE brief (page 9-10).
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    public void setUp() {
        login = new Login("Kyle", "Smith");
    }

    // ---------- Username tests ----------

    @Test
    public void testCheckUserName_CorrectlyFormatted() {
        // Test Data: "kyl_1"
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testCheckUserName_IncorrectlyFormatted() {
        // Test Data: "kyle!!!!!!"  (no underscore, too long)
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }

    @Test
    public void testCheckUserNameMessage_CorrectlyFormatted() {
        assertEquals("Username successfully captured.",
                login.checkUserNameMessage("kyl_1"));
    }

    @Test
    public void testCheckUserNameMessage_IncorrectlyFormatted() {
        assertEquals("Username is not correctly formatted; please ensure that "
                + "your username contains an underscore and is no more "
                + "than five characters in length.",
                login.checkUserNameMessage("kyle!!!!!!"));
    }

    // ---------- Password tests ----------

    @Test
    public void testCheckPasswordComplexity_MeetsRequirements() {
        // Test Data: "Ch&&sec@ke99!"
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testCheckPasswordComplexity_DoesNotMeetRequirements() {
        // Test Data: "password"
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCheckPasswordMessage_MeetsRequirements() {
        assertEquals("Password successfully captured.",
                login.checkPasswordMessage("Ch&&sec@ke99!"));
    }

    @Test
    public void testCheckPasswordMessage_DoesNotMeetRequirements() {
        assertEquals("Password is not correctly formatted; please ensure that "
                + "the password contains at least eight characters, a "
                + "capital letter, a number, and a special character.",
                login.checkPasswordMessage("password"));
    }

    // ---------- Cell phone number tests ----------

    @Test
    public void testCheckCellPhoneNumber_CorrectlyFormatted() {
        // Test Data: +27838968976
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCheckCellPhoneNumber_IncorrectlyFormatted() {
        // Test Data: 08966553
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    @Test
    public void testCheckCellPhoneMessage_CorrectlyFormatted() {
        assertEquals("Cell phone number successfully added.",
                login.checkCellPhoneMessage("+27838968976"));
    }

    @Test
    public void testCheckCellPhoneMessage_IncorrectlyFormatted() {
        assertEquals("Cell phone number incorrectly formatted or does not "
                + "contain international code.",
                login.checkCellPhoneMessage("08966553"));
    }

    // ---------- registerUser tests ----------

    @Test
    public void testRegisterUser_AllValid() {
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username successfully captured. Password successfully "
                + "captured. Cell phone number successfully added. "
                + "User registered successfully.", result);
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        String result = login.registerUser("kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure that "
                + "your username contains an underscore and is no more "
                + "than five characters in length.", result);
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        String result = login.registerUser("kyl_1", "password", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure that "
                + "the password contains at least eight characters, a "
                + "capital letter, a number, and a special character.", result);
    }

    @Test
    public void testRegisterUser_InvalidCellPhoneNumber() {
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals("Cell phone number incorrectly formatted or does not "
                + "contain international code.", result);
    }

    // ---------- loginUser / returnLoginStatus tests ----------

    @Test
    public void testLoginUser_Successful() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginUser_Failed() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongPassword1!"));
    }

    @Test
    public void testReturnLoginStatus_Successful() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus());
    }

    @Test
    public void testReturnLoginStatus_Failed() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        login.loginUser("kyl_1", "wrongPassword1!");
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus());
    }
}