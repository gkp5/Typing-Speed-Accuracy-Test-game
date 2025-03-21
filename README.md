Typing Speed & Accuracy Test

Overview

This is a simple Java Swing-based typing speed and accuracy test. It presents randomly generated words for the user to type, calculates typing speed (WPM), accuracy, and mistake count, and provides a fun way to improve typing skills.

Features

Select difficulty levels: Easy, Medium, Hard, and Insane

Choose the number of words (1-10) for the test

Real-time WPM, accuracy, and mistake tracking

Dynamic text formatting:

Correct characters: Italicized with a strikethrough

Incorrect characters: Highlighted in red

Remaining characters: Displayed in gray

Final results popup with performance evaluation

How to Run

Ensure you have Java (JDK 8+) installed.

Clone this repository:

git clone https://github.com/your-username/TypingGame.git

Navigate to the project directory:

cd TypingGame

Compile and run the program:

javac TypingGame.java
java TypingGame

How It Works

The game loads a dictionary from /usr/share/dict/words (Unix-based systems) or a fallback word list (Windows users).

You select a difficulty level and number of words.

A random text sequence is generated based on the chosen settings.

You type the displayed text in the input field.

Your typing performance is monitored in real-time.

When completed, a results popup displays WPM, accuracy, and mistakes.

The game resets for a new challenge.

Difficulty Levels

Easy: Regular lowercase words.

Medium: Random capitalization in words.

Hard: Words with added punctuation.

Insane: Words with capitalization, punctuation, and symbols.

Requirements

Java Development Kit (JDK 8 or later)

Compatible with Windows, macOS, and Linux

Contribution

Feel free to contribute by improving the UI, optimizing performance, or adding new features.

Fork the repository

Create a new branch

Commit your changes

Submit a pull request

License

This project is open-source and available under the MIT License.

Author

[Your Name]

Happy Typing! 🚀
