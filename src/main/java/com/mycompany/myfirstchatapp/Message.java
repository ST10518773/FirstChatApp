package com.mycompany.myfirstchatapp;

import java.util.Random;

/**
 * Message.java
 * PROG5121 - PoE Parts 2 and 3
 *
 * Represents a single QuickChat message. It validates the message ID,
 * recipient cell number and message length, builds the message hash,
 * and handles the send / disregard / store choice.
 *
 * Part 3: a message now also records its sender, and saving stored
 * messages to the JSON file is handled by MessageManager (using arrays).
 */
public class Message {

    // Running total of messages that have been sent (accumulated across
    // all messages in this run of the application).
    private static int totalMessagesSent = 0;

    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(String messageID, int messageNumber,
                   String recipient, String messageText) {
        this(messageID, messageNumber, recipient, messageText, "Unknown");
    }

    public Message(String messageID, int messageNumber,
                   String recipient, String messageText, String sender) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.sender = sender;
        this.messageHash = createMessageHash();
    }

    /**
     * Generates a random ten-digit message ID.
     */
    public static String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    /**
     * Ensures the message ID is not more than ten characters long.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Returns the message used to tell the user their ID was created.
     */
    public String returnMessageIDStatus() {
        return "Message ID generated: " + messageID;
    }

    /**
     * Checks the message is not more than 250 characters long and
     * returns the appropriate message.
     */
    public static String checkMessageLength(String text) {
        if (text.length() <= 250) {
            return "Message ready to send.";
        }
        int extra = text.length() - 250;
        return "Message exceeds 250 characters by " + extra
                + "; please reduce the size.";
    }

    /**
     * Ensures the recipient cell number contains the international
     * code and is correctly formatted. This reuses the cell phone check
     * written in Part 1 (Login.checkCellPhoneNumber).
     */
    public String checkRecipientCell() {
        Login login = new Login();
        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the "
                + "number and try again.";
    }

    /**
     * Creates the message hash: first two digits of the message ID, a
     * colon, the message number, a colon, then the first and last words
     * of the message - all in capitals. Example: 00:0:HITONIGHT
     */
    public String createMessageHash() {
        String text = messageText.trim();

        String firstWord;
        String lastWord;

        int firstSpace = text.indexOf(' ');
        int lastSpace = text.lastIndexOf(' ');

        if (firstSpace == -1) {
            // Only one word in the message
            firstWord = text;
            lastWord = text;
        } else {
            firstWord = text.substring(0, firstSpace);
            lastWord = text.substring(lastSpace + 1);
        }

        String hash = messageID.substring(0, 2) + ":" + messageNumber + ":"
                + lettersAndDigitsOnly(firstWord)
                + lettersAndDigitsOnly(lastWord);

        return hash.toUpperCase();
    }

    // Removes punctuation such as commas and question marks from a word.
    private String lettersAndDigitsOnly(String word) {
        StringBuilder cleaned = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                cleaned.append(c);
            }
        }
        return cleaned.toString();
    }

    /**
     * Handles what the user chose to do with the message:
     * 1 = Send, 2 = Disregard, 3 = Store to send later.
     * (Saving a stored message to the JSON file is done by MessageManager.)
     */
    public String sentMessage(int choice) {
        if (choice == 1) {
            totalMessagesSent++;
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            return "Message successfully stored.";
        }
        return "Invalid option selected.";
    }

    /**
     * Returns the full details of the message in the required order:
     * Message ID, Message Hash, Recipient, Message.
     */
    public String printMessages() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    /**
     * Returns the total number of messages sent so far.
     */
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Resets the running total (used by the unit tests).
    public static void resetTotalMessages() {
        totalMessagesSent = 0;
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getSender() {
        // Messages saved before Part 3 have no sender
        return sender == null ? "Unknown" : sender;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }
}