/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfirstchatapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

/**
 * Message.java
 * PROG5121 - PoE Part 2: Sending Messages
 *
 * Represents a single QuickChat message. It validates the message ID,
 * recipient cell number and message length, builds the message hash,
 * and handles the send / disregard / store choice.
 *
 * Reference for storing messages in a JSON file:
 * Google. (n.d.). Gson User Guide. GitHub.
 * https://github.com/google/gson/blob/main/UserGuide.md
 */
public class Message {

    private static final String JSON_FILE = "messages.json";

    // Running total of messages that have been sent (accumulated across
    // all messages in this run of the application).
    private static int totalMessagesSent = 0;

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(String messageID, int messageNumber,
                   String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
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
     */
    public String sentMessage(int choice) {
        if (choice == 1) {
            totalMessagesSent++;
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
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

    /**
     * Stores this message in messages.json. Any messages already in the
     * file are kept, and this one is added to the end.
     */
    public void storeMessage() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<Message> storedMessages = new ArrayList<>();

        File file = new File(JSON_FILE);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Type listType = new TypeToken<ArrayList<Message>>() { }.getType();
                ArrayList<Message> existing = gson.fromJson(reader, listType);
                if (existing != null) {
                    storedMessages = existing;
                }
            } catch (IOException e) {
                System.out.println("Error reading stored messages: " + e.getMessage());
            }
        }

        storedMessages.add(this);

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(storedMessages, writer);
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }

    // Getters
    public String getMessageID() {
        return messageID;
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