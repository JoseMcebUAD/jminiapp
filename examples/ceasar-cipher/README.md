# Caesar Cipher Example

A Caesar cipher challenge application demonstrating the JMiniApp framework with interactive encryption practice.

## Overview

This example shows how to create an educational mini-app using JMiniApp core that teaches users about the Caesar cipher encryption technique. Users can select difficulty levels, practice encrypting text, and save their progress through an interactive menu.

## Features

- **Educational Information**: Learn about Caesar cipher encryption
- **Multiple Difficulty Levels**: Choose from NOOB, PRO, HACKER, or GOD difficulty
- **Custom Shift Selection**: Select any shift value from 1 to 26
- **Interactive Challenge**: Practice encrypting text manually
- **Validation Feedback**: Detailed error messages showing incorrect words
- **Export to JSON**: Save your current challenge progress to a JSON file
- **Import from JSON**: Load challenge progress from a JSON file
- **Persistent State**: Challenge state is maintained in the app state

## Project Structure

```
ceasar-cipher/
├── pom.xml
├── README.md
└── src/main/java/com/jminiapp/examples/ceasar/
    ├── CeasarApp.java              # Main application class
    ├── CeasarAppRunner.java        # Bootstrap configuration
    ├── CeasarCipherState.java      # Challenge state model
    ├── CeasarCipherConverter.java  # Cipher logic implementation
    ├── CeasarJSONAdapter.java      # JSON format adapter
    └── CeasarTextsDifficulty.java  # Difficulty level texts
```

## Key Components

### CeasarCipherState
The state model class that encapsulates challenge data and delegates cipher operations:
- `cipherText()`: Encrypt text using Caesar cipher
- `decipherText()`: Decrypt Caesar cipher text
- `compareUserText()`: Validate user's encrypted text
- `reset()`: Reset challenge to initial state
- Uses **composition and delegation** pattern with `CeasarCipherConverter`

### CeasarCipherConverter
The cipher logic implementation that handles:
- `cipherText(text, shift)`: Encrypts text by shifting characters
- `dechipherText(text, shift)`: Decrypts text by reverse shifting
- `compareCipheredToOriginalTex()`: Compares user input with expected output
- Supports uppercase, lowercase, and preserves non-alphabetic characters

### CeasarJSONAdapter
A format adapter that enables JSON import/export for `CeasarCipherState`:
- Implements `JSONAdapter<CeasarCipherState>` from the framework
- Registers with the framework during app bootstrap
- Provides automatic serialization/deserialization

### CeasarApp
The main application class that extends `JMiniApp` and implements:
- `initialize()`: Set up the app and load existing challenge state
- `run()`: Main loop displaying menu and handling user input
- `shutdown()`: Save the challenge state before exiting
- `selectDifficulty()`: Choose from 4 difficulty levels with preset texts
- `SelectShiftNumber()`: Set custom shift value (1-26)
- `startChallenge()`: Accept and validate user's encrypted text
- Uses framework's `context.importData()` and `context.exportData()` for file operations

### CeasarAppRunner
Bootstrap configuration that:
- Registers the `CeasarJSONAdapter` with `.withAdapters()`
- Configures the app name and state class
- Launches the application

### CeasarTextsDifficulty
Contains preset texts for each difficulty level:
- `NOOB_LEVEL`: Short, simple text
- `PRO_LEVEL`: Medium length text
- `HACKER_LEVEL`: Long, complex text
- `GOD_LEVEL`: Very long text with advanced vocabulary

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the project

From the **project root** (not the examples/ceasar-cipher directory):
```bash
mvn clean install
```

This will build both the jminiapp-core module and the caesar cipher example.

### Run the application

Option 1: Using Maven exec plugin (from the examples/ceasar-cipher directory)
```bash
cd examples/ceasar-cipher
mvn exec:java
```

Option 2: Using the packaged JAR (from the examples/ceasar-cipher directory)
```bash
cd examples/ceasar-cipher
java -jar target/ceasar-cipher-app.jar
```

Option 3: From the project root
```bash
cd examples/ceasar-cipher && mvn exec:java
```

## Usage Example

### Learning About Caesar Cipher

```
=== Caesar Cipher App ===
Welcome to the Caesar Cipher Challenge!

--- Caesar Cipher Challenge ---
1. What is the Ceasar cipher?
2. Select text difficulty
3. Select shift number
4. Start challenge
5. Export to JSON file
6. Import from JSON file
7. Exit

Choose an option: 1
The Caesar cipher is a substitution cipher where each letter in the plaintext
is shifted by a fixed number of positions in the alphabet.
For example, with a shift of 3
A becomes D
B becomes E
and so on.
When the shift passes Z, it wraps around to the start of the alphabet.
The same process works for lowercase letters. Non-letter characters such as
spaces or punctuation are usually left unchanged
```

### Setting Up a Challenge

```
--- Caesar Cipher Challenge ---
1. What is the Ceasar cipher?
2. Select text difficulty
3. Select shift number
4. Start challenge
5. Export to JSON file
6. Import from JSON file
7. Exit

Choose an option: 2

=== Select Difficulty ===
1. NOOB
2. PRO
3. HACKER
4. GOD

Choose difficulty: 1

NOOB mode selected!
Original text has been set!

--- Caesar Cipher Challenge ---
1. What is the Ceasar cipher?
2. Select text difficulty
3. Select shift number
4. Start challenge
5. Export to JSON file
6. Import from JSON file
7. Exit

Choose an option: 3

=== Select Shift Number ===
Enter a number of shifting from 1-26: 3

Shift number has been set to: 3
Now you can start the challenge (option 4)!
```

### Completing the Challenge

```
--- Caesar Cipher Challenge ---
1. What is the Ceasar cipher?
2. Select text difficulty
3. Select shift number
4. Start challenge
5. Export to JSON file
6. Import from JSON file
7. Exit

Choose an option: 4

=== Current Challenge ===
Original text: hello world
Shift number: 3

Enter YOUR ciphered version: khoor zruog

✓ CONGRATULATIONS, YOU HAVE PASSED THE CAESAR CIPHER
```

### Handling Errors

When you make a mistake, the app provides detailed feedback:

```
=== Current Challenge ===
Original text: hello world
Shift number: 3

Enter YOUR ciphered version: khoor wrld

✗ Incorrect! The input is not correct: Word number 2: 'wrld' is not correct
(expected: 'zruog').

Try again or select a new challenge.
```

### Export to JSON

```
Choose an option: 5
Challenge state exported successfully to: CeasarCipher.json
```

The exported JSON file will look like:
```json
[
  {
    "originalText": "hello world",
    "jumpNumber": 3,
    "userCipheredText": "khoor zruog",
    "completed": true
  }
]
```

### Import from JSON

```
Choose an option: 6
Challenge state imported successfully from CeasarCipher.json!

Current challenge:
  Original text: hello world
  Shift: 3
  Status: Completed
```

## Architecture Highlights

### Design Patterns Used

1. **Template Method Pattern**: `JMiniApp` defines the lifecycle (initialize → run → shutdown)
2. **Composition and Delegation**: `CeasarCipherState` delegates cipher operations to `CeasarCipherConverter`
3. **Adapter Pattern**: `CeasarJSONAdapter` adapts the state model for JSON serialization
4. **Strategy Pattern**: Framework's `ImportStrategy` for different import behaviors

### Class Relationships

```
CeasarApp (extends JMiniApp)
    ↓ uses
CeasarCipherState (state + behavior)
    ↓ delegates to
CeasarCipherConverter (cipher logic)

CeasarJSONAdapter (persistence)
    ↓ serializes
CeasarCipherState
```

### Exception Handling

The application uses `IllegalArgumentException` for validation errors, which are caught and handled gracefully in `CeasarApp.startChallenge()`, allowing users to retry without restarting the application.

## Next Steps

Try extending this example by:
- Adding ROT13 support (shift of 13)
- Implementing brute-force decryption (try all 26 shifts)
- Adding hints that reveal one word at a time
- Creating timed challenges with leaderboards
- Adding CSV export functionality using the `CSVAdapter`
- Implementing difficulty progression (unlock harder levels)
- Adding statistics tracking (attempts, success rate, average time)

## Author
Jose Manuel Ceballos Medina
