import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TypingGame extends JFrame {
    private JTextField inputField;
    private JLabel textLabel, wpmLabel, accuracyLabel, mistakeLabel, titleLabel;
    private String targetText;
    private int mistakes = 0, typedChars = 0, totalMistakes = 0;
    @SuppressWarnings("unused")
    private int totalTypedChars = 0;
    private long startTime = 0;
    private static final List<String> DICTIONARY = new ArrayList<>();

    public TypingGame() {
        loadDictionary();
        setupUI();
        startNewGame();
    }

    // Load words into dictionary
    private void loadDictionary() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/usr/share/dict/words"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() >= 3) {
                    DICTIONARY.add(line.trim().toLowerCase());
                }
            }
            reader.close();
        } catch (Exception e) {
            // Fallback word list for Windows users
            String[] fallbackWords = {
                "apple", "banana", "zebra", "library", "harmony", "symphony", 
                "exaggeration", "elephant", "crocodile", "optimization",
                "parallel", "synergy", "implementation", "transformation", 
                "kaleidoscope", "encyclopedia", "supercalifragilisticexpialidocious"
            };
            DICTIONARY.addAll(List.of(fallbackWords));
        }
    }

    // UI Setup
    private void setupUI() {
        setTitle("Typing Speed & Accuracy Test");
        setSize(850, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel with Stats
        JPanel topPanel = new JPanel(new GridLayout(1, 4));
        wpmLabel = new JLabel("WPM: 0", JLabel.CENTER);
        accuracyLabel = new JLabel("Accuracy: 100%", JLabel.CENTER);
        mistakeLabel = new JLabel("Mistakes: 0", JLabel.CENTER);
        titleLabel = new JLabel("Typing Challenge", JLabel.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.CYAN);
        wpmLabel.setForeground(Color.WHITE);
        accuracyLabel.setForeground(Color.YELLOW);
        mistakeLabel.setForeground(Color.RED);

        topPanel.add(titleLabel);
        topPanel.add(wpmLabel);
        topPanel.add(accuracyLabel);
        topPanel.add(mistakeLabel);

        // Text to Type
        textLabel = new JLabel("", JLabel.CENTER);
        textLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        textLabel.setForeground(Color.WHITE);

        // Input Field
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.YELLOW);
        inputField.setBorder(BorderFactory.createEmptyBorder());
        

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (startTime == 0) startTime = System.currentTimeMillis();
                updateText();
            }
        });

        getContentPane().setBackground(new Color(30, 30, 30));
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        add(topPanel, BorderLayout.NORTH);
        add(textLabel, BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void startNewGame() {
        String difficulty = askDifficulty();
        int wordCount = askWordCount();
        targetText = generateText(difficulty, wordCount);

        mistakes = 0;
        typedChars = 0;
        startTime = 0;
        inputField.setText("");
        inputField.setEditable(true);
        textLabel.setText(formatText(targetText, ""));
        textLabel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        updateStats();
        inputField.requestFocus();
    }

    private void updateText() {
        String userInput = inputField.getText();
        totalTypedChars++; // Increment on every key press
        if (typedChars > userInput.length()) { // Backspace detected
            if (mistakes > 0) mistakes--; // Reduce mistakes if user corrects an error
        } else { // Normal typing
            if (typedChars < targetText.length() && userInput.length() > 0) {
                if (userInput.charAt(typedChars) != targetText.charAt(typedChars)) {
                    mistakes++; // Only count mistakes when an incorrect character is typed
                }
            }
        }
        
        if (typedChars > userInput.length()) { // Backspace detected
            if (mistakes > 0) mistakes--; // Reduce mistakes if user corrects an error
        } else { // Normal typing
            if (typedChars < targetText.length() && userInput.length() > 0) {
                if (userInput.charAt(typedChars) != targetText.charAt(typedChars)) {
                    mistakes++; // Only count mistakes when an incorrect character is typed
                }
            }
        }
        
        typedChars = userInput.length();
        
        


        for (int i = totalMistakes; i < typedChars; i++) { 
            if (i < targetText.length() && userInput.charAt(i) != targetText.charAt(i)) {
                totalMistakes++; // Mistakes are now permanent
            }
        }
        

        textLabel.setText(formatText(targetText, userInput));
        updateStats();

        if (typedChars >= targetText.length()) {
            showCompletionPopup();
        }
    }

    private void showCompletionPopup() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double minutes = Math.max(elapsedTime / 60000.0, 0.01);
        int wordsTyped = targetText.split(" ").length;
        int wpm = (int) (wordsTyped / minutes);

        String accomplishment;
        if (wpm > 80) {
            accomplishment = "🌟 Incredible! You're a true typing champion! 🏆";
        } else if (wpm > 50) {
            accomplishment = "🔥 Fantastic! You're typing at lightning speed! 💪";
        } else if (wpm > 30) {
            accomplishment = "👍 Good effort! Keep practicing! 😊";
        } else {
            accomplishment = "✨ Nice work! Keep practicing! 🚀";
        }

        String message = "🎉 Typing Completed! 🎉\n\n"
                + "💨 WPM: " + Math.max(wpm, 0) + "\n"
                + "✅ Accuracy: " + accuracyLabel.getText() + "\n"
                + "❌ Mistakes: " + mistakeLabel.getText() + "\n"
                + "🕒 Time Taken: " + (elapsedTime / 1000) + " seconds\n\n"
                + accomplishment;

        JOptionPane.showMessageDialog(this, message, "Typing Test Results", JOptionPane.INFORMATION_MESSAGE);
        startNewGame();
    }

    private String randomCase(String word) {
        StringBuilder newWord = new StringBuilder();
        Random random = new Random();
        for (char c : word.toCharArray()) {
            newWord.append(random.nextBoolean() ? Character.toUpperCase(c) : c);
        }
        return newWord.toString();
    }
    
    private String randomPunctuation() {
        char[] punctuation = {',', ':', ';', '.', '?'};
        return String.valueOf(punctuation[new Random().nextInt(punctuation.length)]);
    }
    
    private String randomSymbol() {
        char[] symbols = {'@', '#', '$', '%', '&', '*'};
        return String.valueOf(symbols[new Random().nextInt(symbols.length)]);
    }
    

    private String askDifficulty() {
        String[] options = {"Easy", "Medium", "Hard", "Insane"};
        return (String) JOptionPane.showInputDialog(this, "Choose Difficulty Level:", 
                "Difficulty", JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    }

    private int askWordCount() {
        String input = JOptionPane.showInputDialog(this, "Enter number of words (1-10):", "Word Count", JOptionPane.QUESTION_MESSAGE);
        try {
            int count = Integer.parseInt(input);
            return Math.max(1, Math.min(count, 10));
        } catch (Exception e) {
            return 10;
        }
    }
    

    // Generates text based on difficulty
    private String generateText(String difficulty, int count) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
    
        for (int i = 0; i < count; i++) {
            String word = DICTIONARY.get(random.nextInt(DICTIONARY.size()));
    
            switch (difficulty) {
                case "Medium":
                    word = randomCase(word);
                    break;
                case "Hard":
                    word = randomCase(word) + randomPunctuation();
                    break;
                case "Insane":
                    word = randomCase(word) + randomPunctuation() + randomSymbol();
                    break;
            }
    
            text.append(word).append(" ");
        }
        return text.toString().trim();
    }
    

    // Formatting the text based on user input
// Formatting the text based on user input
private String formatText(String target, String userInput) {
    StringBuilder formattedText = new StringBuilder("<html>");

    for (int i = 0; i < target.length(); i++) {
        if (i < userInput.length()) {
            char targetChar = target.charAt(i);
            char userChar = userInput.charAt(i);

            if (targetChar == userChar) {
                // Correct: Italic, White, Strikethrough
                formattedText.append("<span style='color: white; text-decoration: line-through; font-style: italic;'>")
                             .append(targetChar)
                             .append("</span>");
            } else {
                // Incorrect: Red
                formattedText.append("<span style='color: red;'>").append(targetChar).append("</span>");
            }
        } else {
            // Not Typed Yet: Gray
            formattedText.append("<span style='color: gray;'>").append(target.charAt(i)).append("</span>");
        }
    }
    formattedText.append("</html>");
    return formattedText.toString();
}

// Updating statistics (WPM, Accuracy, Mistakes)
private void updateStats() {
    if (startTime == 0) return;

    long elapsedTime = System.currentTimeMillis() - startTime;
    double minutes = Math.max(elapsedTime / 60000.0, 0.01);
    int wordsTyped = typedChars / 5;
    int wpm = (int) (wordsTyped / minutes);
    double accuracy = typedChars > 0 ? ((typedChars - totalMistakes) / (double) typedChars) * 100 : 100;

    wpmLabel.setText("WPM: " + Math.max(wpm, 0));
    accuracyLabel.setText(String.format("Accuracy: %.1f%%", accuracy));
    mistakeLabel.setText("Mistakes: " + mistakes);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(TypingGame::new);
    }
}
