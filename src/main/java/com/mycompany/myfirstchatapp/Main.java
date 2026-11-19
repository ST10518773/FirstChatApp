package com.mycompany.myfirstchatapp;

import java.util.Scanner;

/**
 * Main.java
 * PROG5121 - PoE Parts 1, 2 and 3
 *
 * Console-only application (no GUI / no JOptionPane).
 * Part 1: the user registers and logs in.
 * Part 2: once logged in, the user can send, disregard or store messages.
 * Part 3: messages are kept in arrays, and a "Stored Messages" menu lets
 *         the user search, delete and report on them.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ---------- Part 1: registration and login ----------
        Login login = registerUser(scanner);
        boolean loggedIn = false;

        System.out.println("\n--- Log in to your account ---");
        while (!loggedIn) {
            System.out.print("Enter your username: ");
            String loginUserName = scanner.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();

            loggedIn = login.loginUser(loginUserName, loginPassword);
            System.out.println(login.returnLoginStatus());

            if (!loggedIn) {
                System.out.println("Please try logging in again.\n");
            }
        }

        // ---------- Parts 2 and 3: QuickChat (only after a successful login) ----------
        runQuickChat(scanner, login);

        scanner.close();
    }

    // Keeps asking for registration details until registration succeeds.
    private static Login registerUser(Scanner scanner) {
        System.out.println("=== Welcome to the Registration and Login App ===");

        while (true) {
            System.out.println("\n--- Register a new account ---");

            System.out.print("Enter your first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter your last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter a username (must contain an underscore "
                    + "and be no more than 5 characters): ");
            String userName = scanner.nextLine();

            System.out.print("Enter a password (min 8 characters, a capital "
                    + "letter, a number and a special character): ");
            String password = scanner.nextLine();

            System.out.print("Enter your South African cell phone number "
                    + "(e.g. +27838968976): ");
            String cellPhoneNumber = scanner.nextLine();

            Login login = new Login(firstName, lastName);
            String result = login.registerUser(userName, password, cellPhoneNumber);
            System.out.println(result);

            if (result.endsWith("User registered successfully.")) {
                return login;
            }
            System.out.println("Please try registering again.\n");
        }
    }

    // The QuickChat menu. Runs until the user chooses to quit.
    private static void runQuickChat(Scanner scanner, Login login) {
        System.out.println("\nWelcome to QuickChat.");

        // Arrays for sent, disregarded and stored messages, hashes and IDs
        MessageManager manager = new MessageManager();
        // Read any previously stored messages from the JSON file into the array
        manager.loadStoredMessagesFromJson();

        int numMessages = readPositiveNumber(scanner,
                "How many messages do you wish to enter? ");
        int messagesEntered = 0;

        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Option: ");
            String option = scanner.nextLine().trim();

            if (option.equals("1")) {
                if (messagesEntered >= numMessages) {
                    System.out.println("You have already entered all "
                            + numMessages + " messages.");
                } else {
                    while (messagesEntered < numMessages) {
                        sendOneMessage(scanner, manager, login.getUserName(),
                                messagesEntered);
                        messagesEntered++;
                    }
                    System.out.println("\nTotal number of messages sent: "
                            + Message.returnTotalMessages());
                }
            } else if (option.equals("2")) {
                System.out.println(manager.displaySentReport());
            } else if (option.equals("3")) {
                System.out.println("Thank you for using QuickChat. Goodbye!");
                running = false;
            } else if (option.equals("4")) {
                runStoredMessagesMenu(scanner, manager);
            } else {
                System.out.println("Invalid option, please enter 1, 2, 3 or 4.");
            }
        }
    }

    // The "Stored Messages" menu (Part 3).
    private static void runStoredMessagesMenu(Scanner scanner, MessageManager manager) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display the sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a particular recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back to the main menu");
            System.out.print("Option: ");
            String option = scanner.nextLine().trim();

            if (option.equals("1")) {
                System.out.println(manager.displaySenderAndRecipientOfStored());
            } else if (option.equals("2")) {
                System.out.println(manager.displayLongestStoredMessage());
            } else if (option.equals("3")) {
                System.out.print("Enter the message ID: ");
                String id = scanner.nextLine().trim();
                System.out.println(manager.searchByMessageID(id));
            } else if (option.equals("4")) {
                System.out.print("Enter the recipient's cell number: ");
                String recipient = scanner.nextLine().trim();
                System.out.println(manager.searchByRecipient(recipient));
            } else if (option.equals("5")) {
                System.out.print("Enter the message hash: ");
                String hash = scanner.nextLine().trim().toUpperCase();
                String result = manager.deleteMessageByHash(hash);
                System.out.println(result);
                if (result.endsWith("successfully deleted.")) {
                    // Keep the JSON file in step with the array
                    manager.saveStoredMessagesToJson();
                }
            } else if (option.equals("6")) {
                System.out.println(manager.displayStoredReport());
            } else if (option.equals("7")) {
                back = true;
            } else {
                System.out.println("Invalid option, please enter a number from 1 to 7.");
            }
        }
    }

    // Captures, validates and handles a single message.
    private static void sendOneMessage(Scanner scanner, MessageManager manager,
                                       String sender, int messageNumber) {
        System.out.println("\n--- Message " + (messageNumber + 1) + " ---");

        // Recipient: repeat until the cell number is valid
        String recipient;
        Message check;
        String messageID = Message.generateMessageID();
        while (true) {
            System.out.print("Enter the recipient's cell number "
                    + "(with international code, e.g. +27718693002): ");
            recipient = scanner.nextLine().trim();
            check = new Message(messageID, messageNumber, recipient, "x");
            String cellResult = check.checkRecipientCell();
            System.out.println(cellResult);
            if (cellResult.equals("Cell phone number successfully captured.")) {
                break;
            }
        }

        // Message text: repeat until it is 250 characters or fewer
        String text;
        while (true) {
            System.out.print("Enter your message (max 250 characters): ");
            text = scanner.nextLine();
            if (text.trim().isEmpty()) {
                System.out.println("The message cannot be empty.");
                continue;
            }
            String lengthResult = Message.checkMessageLength(text);
            System.out.println(lengthResult);
            if (lengthResult.equals("Message ready to send.")) {
                break;
            }
        }

        Message message = new Message(messageID, messageNumber, recipient, text, sender);
        System.out.println(message.returnMessageIDStatus());

        // Send / Disregard / Store
        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.println("\nWhat would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Option: ");
            String input = scanner.nextLine().trim();
            if (input.equals("1")) {
                choice = 1;
            } else if (input.equals("2")) {
                choice = 2;
            } else if (input.equals("3")) {
                choice = 3;
            } else {
                System.out.println("Invalid option, please enter 1, 2 or 3.");
            }
        }

        System.out.println(message.sentMessage(choice));

        if (choice == 1) {
            manager.addSentMessage(message);
            System.out.println("\n" + message.printMessages());
        } else if (choice == 2) {
            manager.addDisregardedMessage(message);
            String answer = "";
            while (!answer.equals("0")) {
                System.out.print("Enter 0: ");
                answer = scanner.nextLine().trim();
            }
            System.out.println("Message deleted.");
        } else {
            manager.addStoredMessage(message);
            manager.saveStoredMessagesToJson();
        }
    }

    // Keeps asking until the user enters a whole number greater than zero.
    private static int readPositiveNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int number = Integer.parseInt(input);
                if (number > 0) {
                    return number;
                }
            } catch (NumberFormatException e) {
                // fall through to the error message below
            }
            System.out.println("Please enter a whole number greater than 0.");
        }
    }
}