---
sidebar_position: 2
---

# Caesar Cipher Example Application

An educational Caesar cipher application demonstrating composition, delegation, and interactive validation.

**Features:**
- Educational information about Caesar cipher
- Multiple difficulty levels (NOOB, PRO, HACKER, GOD)
- Custom shift selection (1-26)
- Interactive encryption challenge
- Detailed validation feedback
- State persistence
- JSON import/export

**Source Code:** [examples/ceasar-cipher](https://github.com/jminiapp/jminiapp/tree/main/examples/ceasar-cipher)

### Key Concepts Demonstrated

- **Composition and Delegation Pattern**: State class delegates cipher operations
- **Exception Handling**: Graceful error handling with user feedback
- **Interactive Menu System**: Multi-step challenge workflow
- **Input Validation**: Custom shift values and text comparison
- **Educational Application**: Step-by-step learning experience

### Quick Start

```bash
cd examples/ceasar-cipher
mvn clean install
mvn exec:java
```

## Step-by-Step Tutorial

Follow this tutorial to build the Caesar Cipher application from scratch.

### Step 1: Create Project Structure

Create the following directory structure:

```
examples/ceasar-cipher/
├── pom.xml
├── README.md
└── src/main/java/com/jminiapp/examples/ceasar/
    ├── CeasarApp.java
    ├── CeasarAppRunner.java
    ├── CeasarCipherState.java
    ├── CeasarCipherConverter.java
    ├── CeasarJSONAdapter.java
    └── CeasarTextsDifficulty.java
```

### Step 2: Configure Maven POM

Create `pom.xml` with the jminiapp-core dependency:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.jminiapp</groupId>
        <artifactId>jminiapp-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>ceasar-cipher</artifactId>
    <packaging>jar</packaging>

    <name>Caesar Cipher Example</name>
    <description>A Caesar cipher application demonstrating JMiniApp framework</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.jminiapp</groupId>
            <artifactId>jminiapp-core</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <mainClass>com.jminiapp.examples.ceasar.CeasarAppRunner</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Step 3: Create the Cipher Logic

Create `CeasarCipherConverter.java` - handles encryption/decryption logic:

```java
// src/main/java/com/jminiapp/examples/ceasar/CeasarCipherConverter.java
package com.jminiapp.examples.ceasar;

public class CeasarCipherConverter {

    public String cipherText(String originalText, int jumpNumber){
        char[] preCipheredtext = originalText.toCharArray();
        StringBuilder cipheredText = new StringBuilder();

        for(char c : preCipheredtext){
            if(c >= 'A' && c <= 'Z'){
                c = (char) ((c - 'A' + jumpNumber) % 26 + 'A');
            }
            else if(c >= 'a' && c <= 'z'){
                c = (char) ((c - 'a' + jumpNumber) % 26 + 'a');
            }
            cipheredText.append(c);
        }

        return cipheredText.toString();
    }

    public String dechipherText(String cipheredText, int jumpNumber){
        char[] preOriginalText = cipheredText.toCharArray();
        StringBuilder originalText = new StringBuilder();

        for(char c : preOriginalText){
            if(c >= 'A' && c <= 'Z'){
                c = (char) ((c - 'A' - jumpNumber + 26) % 26 + 'A');
            }
            else if(c >= 'a' && c <= 'z'){
                c = (char) ((c - 'a' - jumpNumber + 26) % 26 + 'a');
            }
            originalText.append(c);
        }

        return originalText.toString();
    }

    public String compareCipheredToOriginalTex(String originalText, String userCipheredText, int jumpNumber){
        String userDecipheredtext = this.dechipherText(userCipheredText, jumpNumber);

        // Compare the entire strings for exact match
        if(userDecipheredtext.equals(originalText)){
            return "CONGRATULATIONS, YOU HAVE PASSED THE CAESAR CIPHER";
        }

        // Provide detailed feedback on errors
        String[] userArrayText = userDecipheredtext.split("\\s+");
        String[] originalArrayText = originalText.split("\\s+");

        int toleranceWords = 0;
        StringBuilder incorrectWords = new StringBuilder();

        if(userArrayText.length != originalArrayText.length){
            throw new IllegalArgumentException("Word count mismatch: expected " +
                originalArrayText.length + " words, but got " + userArrayText.length + " words");
        }

        // Compare word by word
        for(int i = 0; i < originalArrayText.length; i++){
            if(!userArrayText[i].equals(originalArrayText[i])){
                String incorrectWord = userCipheredText.split("\\s+")[i];
                incorrectWords.append("Word number ").append(i+1)
                              .append(": '").append(incorrectWord)
                              .append("' is not correct (expected: '")
                              .append(this.cipherText(originalArrayText[i], jumpNumber))
                              .append("'). ");
                toleranceWords++;
            }

            if(toleranceWords > 5){
                throw new IllegalArgumentException("The text has more than 5 incorrect words");
            }
        }

        if(incorrectWords.length() > 0){
            throw new IllegalArgumentException("The input is not correct: " + incorrectWords.toString());
        }

        return "CONGRATULATIONS, YOU HAVE PASSED THE CAESAR CIPHER";
    }
}
```

### Step 4: Create the State Model

Create `CeasarCipherState.java` - uses composition and delegates to converter:

```java
// src/main/java/com/jminiapp/examples/ceasar/CeasarCipherState.java
package com.jminiapp.examples.ceasar;

public class CeasarCipherState {
    private String originalText;
    private int jumpNumber;
    private String userCipheredText;
    private boolean completed = false;
    private final CeasarCipherConverter converter = new CeasarCipherConverter();

    public CeasarCipherState() {
        this.jumpNumber = 3;
        this.completed = false;
    }

    public CeasarCipherState(String originalText, int jumpNumber) {
        this.originalText = originalText;
        this.jumpNumber = jumpNumber;
        this.userCipheredText = "";
    }

    // Getters and Setters
    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }
    public int getJumpNumber() { return jumpNumber; }
    public void setJumpNumber(int jumpNumber) { this.jumpNumber = jumpNumber; }
    public String getUserCipheredText() { return userCipheredText; }
    public void setUserCipheredText(String userCipheredText) { this.userCipheredText = userCipheredText; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // Delegate cipher operations to converter
    public String cipherText(String text, int shift) {
        return converter.cipherText(text, shift);
    }

    public String cipherText() {
        return converter.cipherText(this.originalText, this.jumpNumber);
    }

    public String decipherText(String cipheredText, int shift) {
        return converter.dechipherText(cipheredText, shift);
    }

    public String decipherUserText() {
        return converter.dechipherText(this.userCipheredText, this.jumpNumber);
    }

    public String compareUserText() {
        String result = converter.compareCipheredToOriginalTex(
            this.originalText, this.userCipheredText, this.jumpNumber);
        this.completed = true;
        return result;
    }

    public void reset() {
        this.userCipheredText = "";
        this.completed = false;
    }

    @Override
    public String toString() {
        return "CeasarCipherState{" +
                "originalText='" + originalText + '\'' +
                ", jumpNumber=" + jumpNumber +
                ", completed=" + completed +
                '}';
    }
}
```

### Step 5: Create Difficulty Levels

Create `CeasarTextsDifficulty.java` - preset texts for each difficulty:

```java
// src/main/java/com/jminiapp/examples/ceasar/CeasarTextsDifficulty.java
package com.jminiapp.examples.ceasar;

public class CeasarTextsDifficulty {
    public static final String NOOB_LEVEL = "El viento susurra promesas en la noche silenciosa, mientras la luna observa desde lo alto con calma";
    public static final String PRO_LEVEL = "Bajo un cielo estrellado, caminamos por senderos olvidados, escuchando el crujido de hojas secas. Cada paso traía recuerdos antiguos, sonrisas y susurros del pasado, resonando en la penumbra como ecos persistentes de historias vividas";
    public static final String HACKER_LEVEL = "Las luces de la ciudad se reflejaban en el agua, creando destellos danzantes que parecían latidos de un universo vivo. A lo lejos, un barco navegaba lentamente, dejando una estela luminosa detrás de sí. El murmullo del viento se mezclaba con el susurro del oleaje, generando una melodía suave y nostálgica";
    public static final String GOD_LEVEL = "Mientras la lluvia caía sin prisa sobre los tejados antiguos, un gato observaba silencioso desde la ventana. Cada gota derramaba un suspiro del cielo, repiqueteando en los tejados y creando charcos efímeros que reflejaban faroles apagados. Las calles vacías guardaban el eco de pasos perdidos. Dentro del cuarto, una vela titilaba, proyectando sombras danzantes que parecían memorias fugaces de tiempos lejanos";
}
```

### Step 6: Create the Main Application

Create `CeasarApp.java` - the complete application with all menu options:

```java
// src/main/java/com/jminiapp/examples/ceasar/CeasarApp.java
package com.jminiapp.examples.ceasar;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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

        // Load existing state or create new
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
        context.setData(List.of(state));
        scanner.close();
        System.out.println("\nGoodbye!");
    }

    private void displayMenu() {
        System.out.println("\n--- Caesar Cipher Challenge ---");
        System.out.println("1. What is the Caesar cipher?");
        System.out.println("2. Select text difficulty");
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
                case "1": CeasarInformation(); break;
                case "2": selectDifficulty(); break;
                case "3": SelectShiftNumber(); break;
                case "4": startChallenge(); break;
                case "5": exportToFile(); break;
                case "6": importFromFile(); break;
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
                "The same process works for lowercase letters. Non-letter characters such as spaces or punctuation are usually \n" +
                "left unchanged");
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
            System.out.println("\n" + result);
        } catch (Exception e) {
            System.out.println("\n✗ Incorrect! " + e.getMessage());
            System.out.println("\nTry again or select a new challenge.");
        }
    }

    private void exportToFile() {
        try {
            context.setData(List.of(state));
            context.exportData("json");
            System.out.println("\nChallenge state exported successfully to: CeasarCipher.json");
        } catch (IOException e) {
            System.out.println("\nError exporting file: " + e.getMessage());
        }
    }

    private void importFromFile() {
        try {
            context.importData("json");
            List<CeasarCipherState> data = context.getData();
            if (data != null && !data.isEmpty()) {
                state = data.get(0);
                System.out.println("\nChallenge state imported successfully!");
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
```

### Step 7: Create the JSON Adapter

Create `CeasarJSONAdapter.java`:

```java
// src/main/java/com/jminiapp/examples/ceasar/CeasarJSONAdapter.java
package com.jminiapp.examples.ceasar;

import com.jminiapp.core.adapters.JSONAdapter;

public class CeasarJSONAdapter implements JSONAdapter<CeasarCipherState> {

    @Override
    public Class<CeasarCipherState> getstateClass() {
        return CeasarCipherState.class;
    }
}
```

### Step 8: Create the Bootstrap Runner

Create `CeasarAppRunner.java`:

```java
// src/main/java/com/jminiapp/examples/ceasar/CeasarAppRunner.java
package com.jminiapp.examples.ceasar;

import com.jminiapp.core.engine.JMiniAppRunner;

public class CeasarAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(CeasarApp.class)
            .withState(CeasarCipherState.class)
            .withAdapters(new CeasarJSONAdapter())
            .named("CeasarCipher")
            .run(args);
    }
}
```

### Step 9: Build and Run

```bash
# From project root
mvn clean install

# Run the application
cd examples/ceasar-cipher
mvn exec:java
```

## Architecture Deep Dive

### Design Patterns

1. **Template Method**: `JMiniApp` defines lifecycle (initialize → run → shutdown)
2. **Composition**: `CeasarCipherState` has-a `CeasarCipherConverter`
3. **Delegation**: State delegates cipher operations to converter
4. **Adapter**: `CeasarJSONAdapter` adapts state for JSON serialization

### Exception Handling

The application uses `IllegalArgumentException` for validation errors, caught in `startChallenge()`:

```java
try {
    String result = state.compareUserText();
    System.out.println("\n" + result);
} catch (Exception e) {
    System.out.println("\n✗ Incorrect! " + e.getMessage());
    System.out.println("\nTry again or select a new challenge.");
}
```

This allows graceful error handling - the application continues running after errors.

### Data Flow

```
User Input → CeasarApp → CeasarCipherState → CeasarCipherConverter
                ↓                ↓
            Context API    JSON Adapter
```

## Testing Your Application

### Manual Test Scenario

1. **Learn** (Option 1): Read about Caesar cipher
2. **Configure** (Option 2): Select NOOB difficulty
3. **Set Shift** (Option 3): Enter shift value `3`
4. **Challenge** (Option 4):
   - Original: `hello world`
   - Your answer: `khoor zruog`
5. **Export** (Option 5): Save progress
6. **Import** (Option 6): Load saved state

### Expected Output

```
=== Current Challenge ===
Original text: hello world
Shift number: 3

Enter YOUR ciphered version: khoor zruog

 CONGRATULATIONS, YOU HAVE PASSED THE CAESAR CIPHER
```

## Next Steps

Extend this example by:
- Adding brute-force decryption (try all 26 shifts)
- Implementing hint system
- Creating timed challenges
- Adding statistics tracking
- Supporting multiple cipher algorithms

---

**Author:** Jose Manuel Ceballos Medina
