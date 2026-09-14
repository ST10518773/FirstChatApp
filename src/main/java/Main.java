/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
 
/**
 *
 * @author Student
 */
import java.util.Scanner;

/**
 * Main.java
 *
 * Console-only application (no GUI / no JOptionPane) that lets a user
 * register an account and then log in with it.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = null;
        boolean registered = false;

        System.out.println("=== Welcome to the Registration and Login App ===");

        while (!registered) {
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

            login = new Login(firstName, lastName);
            String result = login.registerUser(userName, password, cellPhoneNumber);
            System.out.println(result);

            registered = result.startsWith("Username successfully captured. "
                    + "Password successfully captured. Cell phone number "
                    + "successfully added.");

            if (!registered) {
                System.out.println("Please try registering again.\n");
            }
        }

        System.out.println("\n--- Log in to your account ---");
        boolean loggedIn = false;

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

        System.out.println("\nApplication finished.");
        scanner.close();
    }
}
