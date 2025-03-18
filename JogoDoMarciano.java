import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class JogoDoMarciano {
  private JFrame frame;
  private JTextField treeField;
  private JLabel resultLabel;
  private JButton tryButton;
  private JButton resetButton;
  private JButton saveRecordButton;
  private JButton viewLeaderboardButton;
  private JTextField nameField;
  private int position;
  private int guessCount;
  private final int guessLimit = 6;
  private final RecordRepo recordRepo = new RecordRepo();

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      try {
        JogoDoMarciano window = new JogoDoMarciano();
        window.frame.setVisible(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  public JogoDoMarciano() {
    initialize();
    gameStart();
  }

  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 450, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout(0, 0));

    JPanel panel = new JPanel();
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    panel.setLayout(new FlowLayout());

    JLabel typeNumberLabel = new JLabel("Digite o número da árvore (1 a 100):");
    panel.add(typeNumberLabel);

    treeField = new JTextField();
    panel.add(treeField);
    treeField.setColumns(10);

    tryButton = new JButton("Tentar");
    panel.add(tryButton);

    resetButton = new JButton("Reset");
    panel.add(resetButton);

    viewLeaderboardButton = new JButton("Ver Recordes");
    panel.add(viewLeaderboardButton);

    saveRecordButton = new JButton("Salvar Recorde");
    saveRecordButton.setEnabled(false);
    panel.add(saveRecordButton);

    nameField = new JTextField();
    panel.add(nameField);
    nameField.setColumns(40);

    resultLabel = new JLabel("");
    panel.add(resultLabel);

    tryButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (guessCount <= guessLimit) {
          guessInit();
        }
      }
    });

    resetButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        gameStart();
      }
    });

    viewLeaderboardButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        openLeaderboard();
      }
    });

    saveRecordButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        saveRecord();
      }
    });
  }

  private void gameStart() {
    position = (int) (Math.random() * 100) + 1;
    guessCount = 0;
    resultLabel.setText("");
    tryButton.setEnabled(true);
    saveRecordButton.setEnabled(false);
    nameField.setEnabled(false);
  }

  private void guessInit() {
    try {
      int guess = Integer.parseInt(treeField.getText());
      guessCount++;

      if (guess == position) {
        resultLabel.setText("Parabéns! Você acertou em " + guessCount + " tentativas.");
        tryButton.setEnabled(false);
        saveRecordButton.setEnabled(true);
        nameField.setEnabled(true);
        return;
      }

      if (guessCount > guessLimit) {
        resultLabel.setText("Número máximo de tentativas (" + guessLimit + ") excedido.");
        return;
      }

      String remainigGuesses = "Tentativas restantes: " + (guessLimit - guessCount);

      if (guess < position) {
        resultLabel.setText("O marciano está em uma árvore maior." + remainigGuesses);
      } else {
        resultLabel.setText("O marciano está em uma árvore menor." + remainigGuesses);
      }
    } catch (NumberFormatException ex) {
      resultLabel.setText("Por favor, insira um número válido entre 1 e 100.");
    }
  }

  private void openLeaderboard() {
    List<Record> records = recordRepo.getTopRecords();

    JFrame recordsFrame = new JFrame("Records");

    recordsFrame.setSize(400, 300);
    recordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    // Create a panel to display the user data
    JPanel recordPanel = new JPanel();
    recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));

    // Display each user in the new window
    for (Record record : records) {
      recordPanel
          .add(new JLabel("ID: " + record.ID + " | Name: " + record.name + " | Email: " + record.guesses
              + " | Timestamp: " + record.timestamp));
    }

    recordsFrame.add(new JScrollPane(recordPanel));
    recordsFrame.setVisible(true);
  }

  private void saveRecord() {
    String name = nameField.getText();
    if (!name.trim().isEmpty()) {

      recordRepo.insertRecord(name, guessCount);
      gameStart();
    } else {
      resultLabel.setText("Por favor, insira seu nome.");
    }
  }
}
