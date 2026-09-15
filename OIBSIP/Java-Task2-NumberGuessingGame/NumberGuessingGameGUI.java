]import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Random;

public class NumberGuessingGameGUI extends JFrame {

    int number;
    int attempts = 0;
    int maxAttempts = 10;
    int round = 1;

    JComboBox<String> levelBox;
    JButton startButton, guessButton, playAgainButton;
    JTextField guessField;
    JLabel attemptsValue, roundValue, resultLabel, rangeLabel;
    JTextArea summaryArea;

    Color bg = new Color(245, 247, 250);
    Color card = Color.WHITE;
    Color accent = new Color(70, 110, 230);

    public NumberGuessingGameGUI() {
        setTitle("Number Guessing Game");
        setSize(430, 520);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bg);
        setLayout(new BorderLayout(10, 10));

        // ---- Title ----
        JLabel title = new JLabel("Number Guessing Game", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(new EmptyBorder(15, 0, 5, 0));
        add(title, BorderLayout.NORTH);

        // ---- Center: cards ----
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(bg);
        center.setBorder(new EmptyBorder(0, 15, 15, 15));

        // Difficulty card
        JPanel setupCard = card("Difficulty");
        levelBox = new JComboBox<>(new String[] { "Easy (1-50)", "Medium (1-100)", "Hard (1-200)" });
        startButton = new JButton("Start Round");
        setupCard.add(levelBox);
        setupCard.add(startButton);
        center.add(setupCard);
        center.add(Box.createVerticalStrut(10));

        // Stats card (Attempts + Round side by side)
        JPanel statsCard = card("Status");
        rangeLabel = new JLabel("Click Start Round to begin");
        JPanel statsRow = new JPanel(new GridLayout(1, 2, 10, 0));
        statsRow.setOpaque(false);

        attemptsValue = new JLabel("Attempts: 0 / 0", SwingConstants.CENTER);
        roundValue = new JLabel("Round: 1", SwingConstants.CENTER);
        statsRow.add(attemptsValue);
        statsRow.add(roundValue);

        statsCard.add(rangeLabel);
        statsCard.add(statsRow);
        center.add(statsCard);
        center.add(Box.createVerticalStrut(10));

        // Guess card
        JPanel guessCard = card("Your Guess");
        guessField = new JTextField(10);
        guessButton = new JButton("Guess");
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        JPanel guessRow = new JPanel();
        guessRow.setOpaque(false);
        guessRow.add(guessField);
        guessRow.add(guessButton);
        guessCard.add(guessRow);

        resultLabel = new JLabel(" ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 15));
        guessCard.add(resultLabel);

        playAgainButton = new JButton("Play Again");
        playAgainButton.setEnabled(false);
        guessCard.add(playAgainButton);
        center.add(guessCard);
        center.add(Box.createVerticalStrut(10));

        // Round summary card
        JPanel summaryCard = card("Round Summary");
        summaryArea = new JTextArea(5, 25);
        summaryArea.setEditable(false);
        summaryArea.setBackground(new Color(250, 250, 252));
        summaryCard.add(new JScrollPane(summaryArea));
        center.add(summaryCard);

        add(center, BorderLayout.CENTER);

        startButton.addActionListener(e -> startRound());
        guessButton.addActionListener(e -> checkGuess());
        playAgainButton.addActionListener(e -> resetForNewRound());

        setVisible(true);
    }

    JPanel card(String titleText) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(card);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(225, 228, 235)), titleText);
        tb.setTitleColor(accent);
        p.setBorder(BorderFactory.createCompoundBorder(tb, new EmptyBorder(8, 10, 10, 10)));
        p.setMaximumSize(new Dimension(400, 150));
        return p;
    }

    void startRound() {
        int choice = levelBox.getSelectedIndex();
        int max = 50;
        if (choice == 1)
            max = 100;
        if (choice == 2)
            max = 200;

        Random rand = new Random();
        number = rand.nextInt(max) + 1;
        attempts = 0;
        maxAttempts = (choice == 0) ? 10 : (choice == 1) ? 7 : 5;

        rangeLabel.setText("Guess a number between 1 and " + max);
        attemptsValue.setText("Attempts: 0 / " + maxAttempts);
        roundValue.setText("Round: " + round);
        resultLabel.setText(" ");
        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        startButton.setEnabled(false);
        levelBox.setEnabled(false);
        playAgainButton.setEnabled(false);
    }

    void checkGuess() {
        int guess;
        try {
            guess = Integer.parseInt(guessField.getText());
        } catch (Exception e) {
            resultLabel.setText("Enter a valid number");
            return;
        }

        attempts++;
        attemptsValue.setText("Attempts: " + attempts + " / " + maxAttempts);
        guessField.setText("");

        if (guess < number) {
            resultLabel.setText("Too Low!");
        } else if (guess > number) {
            resultLabel.setText("Too High!");
        } else {
            resultLabel.setText("Correct!");
            summaryArea.append("Round " + round + ": guessed in " + attempts + " attempts\n");
            round++;
            endRound();
            return;
        }

        if (attempts == maxAttempts) {
            resultLabel.setText("Game Over! Number was " + number);
            summaryArea.append("Round " + round + ": lost, number was " + number + "\n");
            round++;
            endRound();
        }
    }

    void endRound() {
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        playAgainButton.setEnabled(true);
    }

    void resetForNewRound() {
        startButton.setEnabled(true);
        levelBox.setEnabled(true);
        playAgainButton.setEnabled(false);
        rangeLabel.setText("Click Start Round to begin");
        resultLabel.setText(" ");
    }

    public static void main(String[] args) {
        new NumberGuessingGameGUI();
    }
}