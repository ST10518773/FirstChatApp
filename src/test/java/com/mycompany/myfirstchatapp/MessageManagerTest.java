package com.mycompany.myfirstchatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MessageManagerTest.java
 * PROG5121 - PoE Part 3: Store Data and Display Task Report
 *
 * Unit tests for the arrays and the "Stored Messages" features, using
 * the test data from the brief.
 *
 *   Message 1: +27834557896, "Did you get the cake?"            -> Sent
 *   Message 2: +27838884567, "Where are you? ... on time."      -> Stored
 *   Message 3: +27834484567, "Yohoooo, I am at your gate."      -> Disregard
 *   Message 4: 0838884567 (developer), "It is dinner time!"     -> Sent
 *   Message 5: +27838884567, "Ok, I am leaving without you."    -> Stored
 */
public class MessageManagerTest {

    private static final String TEXT_1 = "Did you get the cake?";
    private static final String TEXT_2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String TEXT_3 = "Yohoooo, I am at your gate.";
    private static final String TEXT_4 = "It is dinner time!";
    private static final String TEXT_5 = "Ok, I am leaving without you.";

    private MessageManager manager;
    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    public void setUp() {
        manager = new MessageManager();

        message1 = new Message("1000000001", 0, "+27834557896", TEXT_1, "Developer");
        message2 = new Message("2000000002", 1, "+27838884567", TEXT_2, "Developer");
        message3 = new Message("3000000003", 2, "+27834484567", TEXT_3, "Developer");
        message4 = new Message("4000000004", 3, "0838884567", TEXT_4, "Developer");
        message5 = new Message("5000000005", 4, "+27838884567", TEXT_5, "Developer");

        // Populate the arrays according to each message's flag
        manager.addSentMessage(message1);          // Sent
        manager.addStoredMessage(message2);        // Stored
        manager.addDisregardedMessage(message3);   // Disregard
        manager.addSentMessage(message4);          // Sent
        manager.addStoredMessage(message5);        // Stored
    }

    // ---------- Arrays correctly populated ----------

    @Test
    public void testSentMessagesArray_ContainsExpectedTestData() {
        String[] expected = {"Did you get the cake?", "It is dinner time!"};
        assertArrayEquals(expected, manager.getSentMessageTexts());
    }

    @Test
    public void testDisregardedMessagesArray_ContainsExpectedTestData() {
        String[] expected = {"Yohoooo, I am at your gate."};
        assertArrayEquals(expected, manager.getDisregardedMessageTexts());
    }

    @Test
    public void testStoredMessagesArray_ContainsExpectedTestData() {
        String[] expected = {TEXT_2, TEXT_5};
        assertArrayEquals(expected, manager.getStoredMessageTexts());
    }

    @Test
    public void testMessageHashArray_ContainsAllHashes() {
        assertEquals(5, manager.getHashCount());
        assertEquals("10:0:DIDCAKE", manager.getMessageHash(0));
        assertEquals("20:1:WHERETIME", manager.getMessageHash(1));
        assertEquals("40:3:ITTIME", manager.getMessageHash(3));
    }

    @Test
    public void testMessageIDArray_ContainsAllIDs() {
        assertEquals(5, manager.getIDCount());
        assertEquals("1000000001", manager.getMessageID(0));
        assertEquals("2000000002", manager.getMessageID(1));
        assertEquals("3000000003", manager.getMessageID(2));
        assertEquals("4000000004", manager.getMessageID(3));
        assertEquals("5000000005", manager.getMessageID(4));
    }

    // ---------- Display the longest message ----------

    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                manager.displayLongestStoredMessage());
    }

    // ---------- Search for a message ID ----------

    @Test
    public void testSearchForMessageID_Message4() {
        assertEquals("Recipient: 0838884567, Message: It is dinner time!",
                manager.searchByMessageID("4000000004"));
    }

    @Test
    public void testSearchForMessageID_NotFound() {
        assertEquals("Message ID 9999999999 was not found.",
                manager.searchByMessageID("9999999999"));
    }

    // ---------- Search all messages for a recipient ----------

    @Test
    public void testSearchByRecipient() {
        String expected = "\"Where are you? You are late! I have asked you to be on time.\"\n"
                + "\"Ok, I am leaving without you.\"";
        assertEquals(expected, manager.searchByRecipient("+27838884567"));
    }

    @Test
    public void testSearchByRecipient_NoMessages() {
        assertEquals("No messages found for +27000000000.",
                manager.searchByRecipient("+27000000000"));
    }

    // ---------- Delete a message using the message hash ----------

    @Test
    public void testDeleteMessageUsingHash_TestMessage2() {
        String result = manager.deleteMessageByHash(message2.getMessageHash());
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" "
                + "successfully deleted.", result);

        // The message has been removed from the stored array and the hash/ID arrays
        assertEquals(1, manager.getStoredCount());
        assertArrayEquals(new String[]{TEXT_5}, manager.getStoredMessageTexts());
        assertEquals(4, manager.getHashCount());
        assertEquals(4, manager.getIDCount());
    }

    @Test
    public void testDeleteMessageUsingHash_NotFound() {
        assertEquals("Message hash 00:0:NOTFOUND was not found.",
                manager.deleteMessageByHash("00:0:NOTFOUND"));
        assertEquals(2, manager.getStoredCount());
    }

    // ---------- Reports ----------

    @Test
    public void testDisplayReport_ShowsSentMessages() {
        String report = manager.displaySentReport();
        assertTrue(report.contains("Message Hash: 10:0:DIDCAKE"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Message Hash: 40:3:ITTIME"));
        assertTrue(report.contains("Recipient: 0838884567"));
        assertTrue(report.contains("Message: It is dinner time!"));
    }

    @Test
    public void testStoredReport_ShowsFullDetails() {
        String report = manager.displayStoredReport();
        assertTrue(report.contains("Message ID: 2000000002"));
        assertTrue(report.contains("Message Hash: 20:1:WHERETIME"));
        assertTrue(report.contains("Sender: Developer"));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Message: " + TEXT_2));
    }

    @Test
    public void testDisplaySenderAndRecipientOfStored() {
        String expected = "Sender: Developer | Recipient: +27838884567\n"
                + "Sender: Developer | Recipient: +27838884567";
        assertEquals(expected, manager.displaySenderAndRecipientOfStored());
    }
}