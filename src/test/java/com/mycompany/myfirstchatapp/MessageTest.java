/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.myfirstchatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static o
/**rg.junit.jupiter.api.Assertions.*;

 * MessageTest.java
 *
 * Unit tests for the Message class, using the test data from the brief.
 */
public class MessageTest {

    private static final String TEST_ID = "0012345678";
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String MESSAGE_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String MESSAGE_2 = "Hi Keegan, did you receive the payment?";

    private Message message1;

    @BeforeEach
    public void setUp() {
        Message.resetTotalMessages();
        message1 = new Message(TEST_ID, 0, RECIPIENT_1, MESSAGE_1);
    }

    // Builds a string with the given number of characters (using a loop)
    private String buildText(int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append("a");
        }
        return text.toString();
    }

    // ---------- Message length ----------

    @Test
    public void testMessageLength_Success() {
        assertEquals("Message ready to send.",
                Message.checkMessageLength(MESSAGE_1));
    }

    @Test
    public void testMessageLength_Failure() {
        // 260 characters is 10 over the limit
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(buildText(260)));
    }

    // ---------- Recipient number ----------

    @Test
    public void testRecipientCell_Success() {
        assertEquals("Cell phone number successfully captured.",
                message1.checkRecipientCell());
    }

    @Test
    public void testRecipientCell_Failure() {
        Message message2 = new Message(TEST_ID, 1, RECIPIENT_2, MESSAGE_2);
        assertEquals("Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the number "
                + "and try again.", message2.checkRecipientCell());
    }

    // ---------- Message hash ----------

    @Test
    public void testMessageHash_TestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    public void testMessageHash_Loop() {
        // Checks the hash for several message numbers in a loop
        for (int i = 0; i < 5; i++) {
            Message m = new Message(TEST_ID, i, RECIPIENT_1, "Hi Mike, thanks");
            assertEquals("00:" + i + ":HITHANKS", m.createMessageHash());
        }
    }

    // ---------- Message ID ----------

    @Test
    public void testMessageID_IsCreated() {
        assertEquals("Message ID generated: " + TEST_ID,
                message1.returnMessageIDStatus());
    }

    @Test
    public void testMessageID_NotMoreThanTenCharacters() {
        assertTrue(message1.checkMessageID());
    }

    @Test
    public void testGeneratedMessageID_IsTenDigits() {
        String id = Message.generateMessageID();
        assertEquals(10, id.length());
    }

    // ---------- Send / Disregard / Store ----------

    @Test
    public void testSentMessage_Send() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        assertEquals("Press 0 to delete the message.", message1.sentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    // ---------- Total number sent ----------

    @Test
    public void testTotalMessagesSent() {
        message1.sentMessage(1);                       // Test case 1: send
        Message message2 = new Message(TEST_ID, 1, RECIPIENT_2, MESSAGE_2);
        message2.sentMessage(2);                       // Message 2: discard
        assertEquals(1, Message.returnTotalMessages());
    }
}