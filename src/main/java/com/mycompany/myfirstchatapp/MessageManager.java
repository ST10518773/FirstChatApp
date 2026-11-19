package com.mycompany.myfirstchatapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * MessageManager.java
 * PROG5121 - PoE Part 3: Store Data and Display Task Report
 *
 * Holds the arrays that are populated while the app runs (nothing is
 * hard-coded) and provides the features used by the "Stored Messages"
 * menu: display sender/recipient, longest message, search by message ID,
 * search by recipient, delete by message hash and the reports.
 *
 * Reference for reading/writing the JSON file into an array:
 * Google. (n.d.). Gson User Guide. GitHub.
 * https://github.com/google/gson/blob/main/UserGuide.md
 */
public class MessageManager {

    private static final int MAX_MESSAGES = 100;
    private static final String JSON_FILE = "messages.json";

    // Array 1: all the messages that were sent
    private Message[] sentMessages = new Message[MAX_MESSAGES];
    private int sentCount = 0;

    // Array 2: all the messages that were disregarded
    private Message[] disregardedMessages = new Message[MAX_MESSAGES];
    private int disregardedCount = 0;

    // Array 3: all the stored messages (read from the JSON file)
    private Message[] storedMessages = new Message[MAX_MESSAGES];
    private int storedCount = 0;

    // Array 4: all the message hashes
    private String[] messageHashes = new String[MAX_MESSAGES * 3];
    private int hashCount = 0;

    // Array 5: all the message IDs
    private String[] messageIDs = new String[MAX_MESSAGES * 3];
    private int idCount = 0;

    // ---------- Populating the arrays ----------

    public boolean addSentMessage(Message message) {
        if (sentCount >= MAX_MESSAGES) {
            return false;
        }
        sentMessages[sentCount] = message;
        sentCount++;
        recordHashAndID(message);
        return true;
    }

    public boolean addDisregardedMessage(Message message) {
        if (disregardedCount >= MAX_MESSAGES) {
            return false;
        }
        disregardedMessages[disregardedCount] = message;
        disregardedCount++;
        recordHashAndID(message);
        return true;
    }

    public boolean addStoredMessage(Message message) {
        if (storedCount >= MAX_MESSAGES) {
            return false;
        }
        storedMessages[storedCount] = message;
        storedCount++;
        recordHashAndID(message);
        return true;
    }

    // Every message's hash and ID go into their own arrays.
    private void recordHashAndID(Message message) {
        if (hashCount < messageHashes.length) {
            messageHashes[hashCount] = message.getMessageHash();
            hashCount++;
        }
        if (idCount < messageIDs.length) {
            messageIDs[idCount] = message.getMessageID();
            idCount++;
        }
    }

    // ---------- Counts and array contents ----------

    public int getSentCount() {
        return sentCount;
    }

    public int getDisregardedCount() {
        return disregardedCount;
    }

    public int getStoredCount() {
        return storedCount;
    }

    public int getHashCount() {
        return hashCount;
    }

    public int getIDCount() {
        return idCount;
    }

    public String getMessageHash(int index) {
        return messageHashes[index];
    }

    public String getMessageID(int index) {
        return messageIDs[index];
    }

    /**
     * Returns the text of every sent message as an array of Strings.
     */
    public String[] getSentMessageTexts() {
        String[] texts = new String[sentCount];
        for (int i = 0; i < sentCount; i++) {
            texts[i] = sentMessages[i].getMessageText();
        }
        return texts;
    }

    /**
     * Returns the text of every disregarded message.
     */
    public String[] getDisregardedMessageTexts() {
        String[] texts = new String[disregardedCount];
        for (int i = 0; i < disregardedCount; i++) {
            texts[i] = disregardedMessages[i].getMessageText();
        }
        return texts;
    }

    /**
     * Returns the text of every stored message.
     */
    public String[] getStoredMessageTexts() {
        String[] texts = new String[storedCount];
        for (int i = 0; i < storedCount; i++) {
            texts[i] = storedMessages[i].getMessageText();
        }
        return texts;
    }

    // ---------- Stored Messages menu features ----------

    /**
     * (a) Displays the sender and recipient of all stored messages.
     */
    public String displaySenderAndRecipientOfStored() {
        if (storedCount == 0) {
            return "There are no stored messages.";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < storedCount; i++) {
            result.append("Sender: ").append(storedMessages[i].getSender())
                    .append(" | Recipient: ").append(storedMessages[i].getRecipient());
            if (i < storedCount - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    /**
     * (b) Returns the longest stored message.
     */
    public String displayLongestStoredMessage() {
        if (storedCount == 0) {
            return "There are no stored messages.";
        }
        String longest = storedMessages[0].getMessageText();
        for (int i = 1; i < storedCount; i++) {
            String text = storedMessages[i].getMessageText();
            if (text.length() > longest.length()) {
                longest = text;
            }
        }
        return longest;
    }

    /**
     * (c) Searches for a message ID and returns the recipient and message.
     * Searches the sent and stored messages.
     */
    public String searchByMessageID(String messageID) {
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].getMessageID().equals(messageID)) {
                return "Recipient: " + sentMessages[i].getRecipient()
                        + ", Message: " + sentMessages[i].getMessageText();
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getMessageID().equals(messageID)) {
                return "Recipient: " + storedMessages[i].getRecipient()
                        + ", Message: " + storedMessages[i].getMessageText();
            }
        }
        return "Message ID " + messageID + " was not found.";
    }

    /**
     * (d) Returns all the messages sent or stored for a particular
     * recipient, one per line.
     */
    public String searchByRecipient(String recipient) {
        StringBuilder result = new StringBuilder();
        int found = 0;

        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i].getRecipient().equals(recipient)) {
                if (found > 0) {
                    result.append("\n");
                }
                result.append("\"").append(sentMessages[i].getMessageText()).append("\"");
                found++;
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getRecipient().equals(recipient)) {
                if (found > 0) {
                    result.append("\n");
                }
                result.append("\"").append(storedMessages[i].getMessageText()).append("\"");
                found++;
            }
        }

        if (found == 0) {
            return "No messages found for " + recipient + ".";
        }
        return result.toString();
    }

    /**
     * (e) Deletes a stored message using its message hash.
     */
    public String deleteMessageByHash(String hash) {
        int index = -1;
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i].getMessageHash().equals(hash)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return "Message hash " + hash + " was not found.";
        }

        Message deleted = storedMessages[index];

        // Shift the remaining stored messages one place to the left
        for (int i = index; i < storedCount - 1; i++) {
            storedMessages[i] = storedMessages[i + 1];
        }
        storedCount--;
        storedMessages[storedCount] = null;

        // Remove its hash and ID from the hash and ID arrays too
        hashCount = removeFromArray(messageHashes, hashCount, deleted.getMessageHash());
        idCount = removeFromArray(messageIDs, idCount, deleted.getMessageID());

        return "Message: \"" + deleted.getMessageText() + "\" successfully deleted.";
    }

    // Removes the first matching value from a String array and returns
    // the new number of items in the array.
    private int removeFromArray(String[] array, int count, String value) {
        for (int i = 0; i < count; i++) {
            if (array[i].equals(value)) {
                for (int j = i; j < count - 1; j++) {
                    array[j] = array[j + 1];
                }
                array[count - 1] = null;
                return count - 1;
            }
        }
        return count;
    }

    /**
     * (f) Report listing the full details of all stored messages.
     */
    public String displayStoredReport() {
        if (storedCount == 0) {
            return "There are no stored messages.";
        }
        StringBuilder report = new StringBuilder("--- Stored Messages Report ---\n");
        for (int i = 0; i < storedCount; i++) {
            Message m = storedMessages[i];
            report.append("\nMessage ID: ").append(m.getMessageID())
                    .append("\nMessage Hash: ").append(m.getMessageHash())
                    .append("\nSender: ").append(m.getSender())
                    .append("\nRecipient: ").append(m.getRecipient())
                    .append("\nMessage: ").append(m.getMessageText())
                    .append("\n");
        }
        return report.toString();
    }

    /**
     * Report of all the sent messages: Message Hash, Recipient, Message.
     */
    public String displaySentReport() {
        if (sentCount == 0) {
            return "There are no sent messages.";
        }
        StringBuilder report = new StringBuilder("--- Sent Messages Report ---\n");
        for (int i = 0; i < sentCount; i++) {
            Message m = sentMessages[i];
            report.append("\nMessage Hash: ").append(m.getMessageHash())
                    .append("\nRecipient: ").append(m.getRecipient())
                    .append("\nMessage: ").append(m.getMessageText())
                    .append("\n");
        }
        return report.toString();
    }

    // ---------- JSON file (read into / write from the stored array) ----------

    /**
     * Reads the messages stored in messages.json into the stored
     * messages array.
     */
    public void loadStoredMessagesFromJson() {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Message[] loaded = gson.fromJson(reader, Message[].class);
            if (loaded != null) {
                for (int i = 0; i < loaded.length; i++) {
                    if (loaded[i] != null) {
                        addStoredMessage(loaded[i]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading stored messages: " + e.getMessage());
        } catch (JsonParseException e) {
            System.out.println("The stored messages file could not be read: " + e.getMessage());
        }
    }

    /**
     * Writes everything in the stored messages array to messages.json.
     */
    public void saveStoredMessagesToJson() {
        Message[] toSave = new Message[storedCount];
        for (int i = 0; i < storedCount; i++) {
            toSave[i] = storedMessages[i];
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(toSave, writer);
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
        }
    }
}