package com.jminiapp.examples.ceasar;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * A Caesar cipher application demonstrating the JMiniApp framework.
 *
 * This app allows users to practice Caesar cipher encryption/decryption.
 */
public class CeasarApp extends JMiniApp {
    private Scanner scanner;
    private CeasarCipherState state;
    private boolean running;

    public CeasarApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("\n=== Caesar Cipher App ===");
        System.out.println("Welcome to the Caesar Cipher Challenge!");

        scanner = new Scanner(System.in);
        running = true;

        // Try to load existing state from context
        List<CeasarCipherState> data = context.getData();
        if (data != null && !data.isEmpty()) {
            state = data.get(0);
            System.out.println("Loaded existing challenge");
        } else {
            state = new CeasarCipherState();
            System.out.println("Starting new challenge");
        }
    }

    @Override
    protected void run() {
        while (running) {
            displayMenu();
            handleUserInput();
        }
    }

    @Override
    protected void shutdown() {
        // Save the state to context
        context.setData(List.of(state));

        scanner.close();
        System.out.println("\nvaya bien!");
    }

    private void displayMenu() {
        System.out.println("\n--- Caesar Cipher Challenge ---");
        System.out.println("1. What is the Ceasar cihper?");
        System.out.println("2. Select text dificculty");
        System.out.println("3. Select shift number");
        System.out.println("4. Start challenge");
        System.out.println("5. Export to JSON file");
        System.out.println("6. Import from JSON file");
        System.out.println("7. Exit");
        System.out.print("\nChoose an option: ");
    }

    private void handleUserInput() {
        try {
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    CeasarInformation();
                    break;

                case "2":
                    selectDifficulty();
                    break;

                case "3":
                    SelectShiftNumber();
                    break;

                case "4":
                    startChallenge();
                    break;

                case "5":
                    exportToFile();
                    break;

                case "6":
                    importFromFile();
                    break;

                case "7":
                    running = false;
                    System.out.println("\nExiting...");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1-7.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void CeasarInformation(){
        System.out.println("The Caesar cipher is a substitution cipher where each letter in the plaintext \n" +
                "is shifted by a fixed number of positions in the alphabet. \n" +
                "For example, with a shift of 3 \n" +
                "A becomes D \n" +
                "B becomes E \n" +
                "and so on. \n" +
                "When the shift passes Z, it wraps around to the start of the alphabet. \n" +
                "The same process works for lowercase letters. Non-letter characters such as spaces or punctuation are usually \n" +"left unchanged");
    }

    private void selectDifficulty() {
        System.out.println("\n=== Select Difficulty ===");
        System.out.println("1. NOOB");
        System.out.println("2. PRO");
        System.out.println("3. HACKER");
        System.out.println("4. GOD");
        System.out.print("\nChoose difficulty: ");

        String choice = scanner.nextLine().trim();
        String originalText;

        switch (choice) {
            case "1":
                originalText = CeasarTextsDifficulty.NOOB_LEVEL;
                System.out.println("\nNOOB mode selected!");
                break;

            case "2":
                originalText = CeasarTextsDifficulty.PRO_LEVEL;
                System.out.println("\nPRO mode selected!");
                break;

            case "3":
                originalText = CeasarTextsDifficulty.HACKER_LEVEL;
                System.out.println("\nHACKER mode selected!");
                break;

            case "4":
                originalText = CeasarTextsDifficulty.GOD_LEVEL;
                System.out.println("\nGOD mode selected!");
                break;

            default:
                originalText = CeasarTextsDifficulty.NOOB_LEVEL;
                System.out.println("Invalid choice. Using NOOB mode by default.");
                break;
        }

        this.state.setOriginalText(originalText);
        System.out.println("\nOriginal text has been set!");
    }

    private void startChallenge() {
        if (state.getOriginalText() == null || state.getOriginalText().isEmpty()) {
            System.out.println("\nNo active challenge. Please select difficulty first (option 2).");
            return;
        }

        if (state.getJumpNumber() == 0) {
            System.out.println("\nShift number not set. Please select shift number first (option 3).");
            return;
        }

        System.out.println("\n=== Current Challenge ===");
        System.out.println("Original text: " + state.getOriginalText());
        System.out.println("Shift number: " + state.getJumpNumber());
        System.out.println("\nEnter YOUR ciphered version:");

        String userCipheredText = scanner.nextLine().trim();
        state.setUserCipheredText(userCipheredText);

        try {
            String result = state.compareUserText();
            System.out.println("\n " + result);
        } catch (Exception e) {
            System.out.println("\n✗ Incorrect! " + e.getMessage());
            System.out.println("\nTry again or select a new challenge.");
        }
    }

    private void SelectShiftNumber(){
        System.out.println("\n=== Select Shift Number ===");
        System.out.println("Enter a number of shifting from 1-26: ");

        try {
            String shiftNumber = scanner.nextLine().trim();
            int iShiftNumber = Integer.parseInt(shiftNumber);

            if(iShiftNumber < 1 || iShiftNumber > 26){
                System.out.println("Please select a valid number between 1 and 26");
                return;
            }

            this.state.setJumpNumber(iShiftNumber);
            System.out.println("\nShift number has been set to: " + iShiftNumber);
            System.out.println("Now you can start the challenge (option 4)!");

        } catch (NumberFormatException e) {
            System.out.println("Error: The input is not a valid number, try again");
        }
    }

    private void exportToFile() {
        try {
            // Save current state to context before exporting
            context.setData(List.of(state));

            // Use default filename convention: {appName}.{format}
            context.exportData("json");
            System.out.println("\nChallenge state exported successfully to: CeasarCipher.json");
        } catch (IOException e) {
            System.out.println("\nError exporting file: " + e.getMessage());
        }
    }

    private void importFromFile() {
        try {
            // Use default filename convention: {appName}.{format}
            context.importData("json");

            // Update local state from context after import
            List<CeasarCipherState> data = context.getData();
            if (data != null && !data.isEmpty()) {
                state = data.get(0);
                System.out.println("\nChallenge state imported successfully from CeasarCipher.json!");
                System.out.println("\nCurrent challenge:");
                System.out.println("  Original text: " + state.getOriginalText());
                System.out.println("  Shift: " + state.getJumpNumber());
                System.out.println("  Status: " + (state.isCompleted() ? "Completed" : "In Progress"));
            } else {
                System.out.println("\nError: No data found in file.");
            }
        } catch (IOException e) {
            System.out.println("\nError importing file: " + e.getMessage());
        }
    }



}
