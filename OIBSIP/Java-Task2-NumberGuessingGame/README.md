# Java Development — Task 2: Number Guessing Game

## Objective
A GUI-based Number Guessing Game built in Java Swing. The system generates a random number within a chosen range, and the user attempts to guess it within a limited number of attempts, receiving "Too High" / "Too Low" feedback until the number is guessed correctly or the attempt limit is reached.

## Tech Stack
- Java (JDK 8+)
- Swing (GUI)
- `java.util.Random`

## Features
- Difficulty selector with three levels:
  - **Easy** — range 1 to 50, 10 attempts
  - **Medium** — range 1 to 100, 7 attempts
  - **Hard** — range 1 to 200, 5 attempts
- "Start Round" button generates a new random number for the selected difficulty
- Live feedback after every guess: **Too Low!**, **Too High!**, or **Correct!**
- Attempt counter displayed throughout the round (`Attempts: X / max`)
- Round counter that increments after every completed round
- "Game Over" message revealing the number if the attempt limit is exhausted
- "Play Again" option to reset and start a new round without restarting the application
- Round Summary panel that logs the outcome of every round played in the session (attempts taken, or loss with the revealed number)
- Input validation: non-numeric guesses show an "Enter a valid number" message instead of crashing

## How to Run
1. Make sure JDK is installed (`java -version` to confirm).
2. Compile the program:
   ```
   javac NumberGuessingGameGUI.java
   ```
3. Run the compiled program:
   ```
   java NumberGuessingGameGUI
   ```
4. Select a difficulty level, click **Start Round**, and begin guessing.

## File Structure
```
OIBSIP/Java-Task2-NumberGuessingGame/
├── NumberGuessingGameGUI.java
├── README.md
└── SARSIKA_SRI_K_TASK_2.mp4   (demo video)
```

## Demo Video
`SARSIKA_SRI_K_TASK_2.mp4` — screen-recorded walkthrough showing the application running end-to-end: selecting a difficulty, guessing, receiving feedback, completing a round, and starting a new round.

## Author
Sarsika Sri K — Oasis Infobyte Summer Internship Program (OIBSIP), Java Development Track
