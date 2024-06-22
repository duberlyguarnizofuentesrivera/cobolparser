package com.duberlyguarnizo;

import com.formdev.flatlaf.FlatLightLaf;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class CobolParserApp extends JFrame {

  private static final long serialVersionUID = 1L;

  private JTextArea inputTextToAnalyse;
  private JTable tblLexicalResult;
  private JTextArea txtSyntaxResult;

  public static void main(String[] args) {
    log.info("Creating window");
    FlatLightLaf.setup();
    UIManager.put( "Button.arc", 999 );
    UIManager.put( "ScrollBar.trackArc", 999 );
    UIManager.put( "ScrollBar.thumbArc", 999 );
    UIManager.put( "ScrollBar.trackInsets", new Insets( 2, 4, 2, 4 ) );
    UIManager.put( "ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ) );
    UIManager.put( "ScrollBar.track", new Color( 0xe0e0e0 ) );
    SwingUtilities.invokeLater(() -> {
      try {
        CobolParserApp frame = new CobolParserApp();
        frame.setVisible(true);
        frame.setTitle("COBOL Parser");
        frame.pack(); // Pack the components
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());
        frame.setContentPane(contentPane);

        frame.createComponents(contentPane);
      } catch (SecurityException| IllegalComponentStateException e) {
        log.error("Failed to create window", e);
      }
    });
  }

  private void createComponents(JPanel contentPane) {
    JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Improved spacing
    contentPane.add(panelButtons, BorderLayout.NORTH);

    JSplitPane splitPaneMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPaneMain.setResizeWeight(0.5); // Divide space equally initially
    contentPane.add(splitPaneMain, BorderLayout.CENTER);

    JPanel inputPanel = createTextArea();
    splitPaneMain.setTopComponent(inputPanel);

    JPanel resultsPanel = createResultAreas();
    splitPaneMain.setBottomComponent(resultsPanel);

    splitPaneMain.setDividerLocation(0.7);

    createButtons(panelButtons);
  }

  private JPanel createTextArea() {
    inputTextToAnalyse = new JTextArea();
    inputTextToAnalyse.setFont(new Font("Monospace", Font.PLAIN, 16)); // Customize font
    JScrollPane scrollPane = new JScrollPane(inputTextToAnalyse);
    scrollPane.setPreferredSize(new Dimension(300, 200));

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(scrollPane, BorderLayout.CENTER);
    return panel;
  }

  private JPanel createResultAreas() {
    JPanel panel = new JPanel(new BorderLayout());

    JSplitPane splitPaneResults = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPaneResults.setResizeWeight(0.7); //resize the table more than the lower bar

    Object[] columnNames = {"Token", "Lexeme", "Line Number"};
    tblLexicalResult = new JTable(new DefaultTableModel(columnNames, 0));
    tblLexicalResult.setFont(new Font("Monospace", Font.PLAIN, 16));
    JScrollPane scrollPaneTable = new JScrollPane(tblLexicalResult);
    splitPaneResults.setTopComponent(scrollPaneTable);

    txtSyntaxResult = new JTextArea();
    txtSyntaxResult.setEditable(false);
    txtSyntaxResult.setFont(new Font("Monospace", Font.PLAIN, 16));
    JScrollPane scrollPaneSyntax = new JScrollPane(txtSyntaxResult);
    splitPaneResults.setBottomComponent(scrollPaneSyntax);

    splitPaneResults.setDividerLocation(0.2);

    panel.add(splitPaneResults, BorderLayout.CENTER);
    return panel;
  }

  private void createButtons(JPanel panelButtons) {
    JButton btnLoadFile = new JButton("Load File");
    btnLoadFile.addActionListener(e -> loadSourceCodeFile());
    panelButtons.add(btnLoadFile);

    JButton btnLexicalAnalysis = new JButton("Lexical Analysis");
    btnLexicalAnalysis.addActionListener(e -> doLexicalAnalysis());
    panelButtons.add(btnLexicalAnalysis);

    JButton btnSyntaxAnalysis = new JButton("Syntax Analysis");
    btnSyntaxAnalysis.addActionListener(e -> doSyntaxAnalysis());
    panelButtons.add(btnSyntaxAnalysis);
  }

  private void loadSourceCodeFile() {
    JFileChooser chooser = new JFileChooser();
    int result = chooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      try {
        String content = Files.readString(file.toPath());
        inputTextToAnalyse.setText(content);
      } catch (IOException e) {
        log.error("Error reading file", e);
        JOptionPane.showMessageDialog(this, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void doLexicalAnalysis() {
    String text = inputTextToAnalyse.getText();
    Object[][] result = ParsingLogic.doLexicalAnalysis(text);

    DefaultTableModel model = (DefaultTableModel) tblLexicalResult.getModel();
    model.setRowCount(0); // Clear existing rows

    for (Object[] row : result) {
      model.addRow(row);
    }
  }

  private void doSyntaxAnalysis() {
    String text = inputTextToAnalyse.getText();
    String result = ParsingLogic.doSyntaxAnalysis(text);
    txtSyntaxResult.setText(result);
    if (result.startsWith("Syntax error")) {
      txtSyntaxResult.setForeground(Color.RED); // Red color for error
    } else {
      txtSyntaxResult.setForeground(new Color(25, 111, 61)); // Green color for success
    }
  }
}
