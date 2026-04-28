import dsa.ScoringEngine;
import dsa.AnalysisResult;
import recommendation.AdaptivePasswordGenerator;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.datatransfer.StringSelection;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Swing GUI for the Smart Password Strength Analyzer.
 * Completely decoupled from analysis logic — delegates to PasswordAnalyzer.
 */
public class PasswordAnalyzerUI extends JFrame {

    // ── Colors ────────────────────────────────────────────────────────────
    private static final Color BG_DARK       = new Color(18, 18, 28);
    private static final Color BG_CARD       = new Color(28, 28, 42);
    private static final Color BG_INPUT      = new Color(38, 38, 55);
    private static final Color ACCENT        = new Color(99, 102, 241);
    private static final Color TEXT_PRIMARY  = new Color(240, 240, 255);
    private static final Color TEXT_MUTED    = new Color(140, 140, 170);
    private static final Color COLOR_WEAK    = new Color(239, 68, 68);
    private static final Color COLOR_MOD     = new Color(234, 179, 8);
    private static final Color COLOR_STRONG  = new Color(34, 197, 94);
    private static final Color COLOR_ISSUE   = new Color(252, 165, 165);
    private static final Color COLOR_SUGGEST = new Color(134, 239, 172);

    // ── Fonts ─────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_MONO    = new Font("Consolas", Font.PLAIN, 14);
    private static final Font FONT_SCORE   = new Font("Segoe UI", Font.BOLD, 42);
    private static final Font FONT_STRENGTH= new Font("Segoe UI", Font.BOLD, 18);

    // ── Components ────────────────────────────────────────────────────────
    private JPasswordField passwordField;
    private JCheckBox       showPasswordBox;
    private JButton         analyzeButton;
    private JButton         clearButton;

    // Result panel widgets
    private JLabel  scoreLabel;
    private JLabel  strengthLabel;
    private JLabel  crackTimeLabel;
    private JLabel  entropyLabel;
    private JProgressBar scoreBar;
    private JTextArea issuesArea;
    private JTextArea suggestionsArea;
    private JPanel  resultPanel;

    private final ScoringEngine analyzer = new ScoringEngine();
    private final AdaptivePasswordGenerator adaptiveGenerator = new AdaptivePasswordGenerator();

    // ─────────────────────────────────────────────────────────────────────

    public PasswordAnalyzerUI() {
        setTitle("Smart Password Strength Analyzer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(680, 780);
        setMinimumSize(new Dimension(560, 680));
        setLocationRelativeTo(null);
        setBackground(BG_DARK);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);

        setContentPane(root);
    }

    // ── Header ────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Smart Password Strength Analyzer");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);

        JLabel sub = new JLabel("Evaluate security, entropy, and crack resistance");
        sub.setFont(FONT_BODY);
        sub.setForeground(TEXT_MUTED);

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 4));
        text.setBackground(BG_DARK);
        text.add(title);
        text.add(sub);
        p.add(text, BorderLayout.CENTER);
        return p;
    }

    // ── Center (input + results) ──────────────────────────────────────────

    private JScrollPane buildCenter() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_DARK);

        JPanel inputCard = buildInputCard();
        inputCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel resultCard = buildResultCard();
        resultCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(inputCard);
        content.add(Box.createVerticalStrut(12));
        content.add(resultCard);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BG_DARK);
        scrollPane.getViewport().setBackground(BG_DARK);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    // ── Input card ────────────────────────────────────────────────────────

    private JPanel buildInputCard() {
        JPanel card = createCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;

        // Label
        JLabel lbl = styledLabel("Enter Password");
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        card.add(lbl, g);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setFont(FONT_MONO);
        passwordField.setBackground(BG_INPUT);
        passwordField.setForeground(TEXT_PRIMARY);
        passwordField.setCaretColor(TEXT_PRIMARY);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 90), 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));
        passwordField.setPreferredSize(new Dimension(0, 44));
        g.gridy = 1;
        card.add(passwordField, g);

        // Show password checkbox
        showPasswordBox = new JCheckBox("Show password");
        showPasswordBox.setFont(FONT_BODY);
        showPasswordBox.setForeground(TEXT_MUTED);
        showPasswordBox.setBackground(BG_CARD);
        showPasswordBox.setFocusPainted(false);
        showPasswordBox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordBox.isSelected() ? (char) 0 : '•');
        });
        g.gridy = 2; g.gridwidth = 1;
        card.add(showPasswordBox, g);

        // Buttons
        analyzeButton = styledButton("Analyze", ACCENT);
        clearButton   = styledButton("Clear", new Color(60, 60, 90));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(BG_CARD);
        btnRow.add(clearButton);
        btnRow.add(analyzeButton);

        g.gridx = 1; g.gridy = 2; g.anchor = GridBagConstraints.EAST;
        card.add(btnRow, g);

        // Wire actions
        analyzeButton.addActionListener(e -> runAnalysis());
        clearButton.addActionListener(e -> clearAll());
        passwordField.addActionListener(e -> runAnalysis()); // Enter key

        return card;
    }

    // ── Result card ───────────────────────────────────────────────────────

    private JPanel buildResultCard() {
        resultPanel = createCard();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        // Placeholder
        JLabel placeholder = new JLabel("Results will appear here after analysis.");
        placeholder.setFont(FONT_BODY);
        placeholder.setForeground(TEXT_MUTED);
        placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeholder.setBorder(new EmptyBorder(30, 0, 30, 0));
        resultPanel.add(placeholder);

        return resultPanel;
    }

    // ── Analysis logic ────────────────────────────────────────────────────

    private void runAnalysis() {
        String password = new String(passwordField.getPassword());
        AnalysisResult result = analyzer.analyzePassword(password);
        renderResult(result, password);
    }

    private void clearAll() {
        passwordField.setText("");
        resultPanel.removeAll();
        JLabel placeholder = new JLabel("Results will appear here after analysis.");
        placeholder.setFont(FONT_BODY);
        placeholder.setForeground(TEXT_MUTED);
        placeholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeholder.setBorder(new EmptyBorder(30, 0, 30, 0));
        resultPanel.add(placeholder);
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    // ── Render result ─────────────────────────────────────────────────────

    private void renderResult(AnalysisResult r, String originalPassword) {
        resultPanel.removeAll();

        Color strengthColor;
        String strengthText;
        if (r.getStrength() == AnalysisResult.Strength.STRONG) {
            strengthColor = COLOR_STRONG;
            strengthText  = "STRONG";
        } else if (r.getStrength() == AnalysisResult.Strength.MODERATE) {
            strengthColor = COLOR_MOD;
            strengthText  = "MODERATE";
        } else {
            strengthColor = COLOR_WEAK;
            strengthText  = "WEAK";
        }

        // ── Score row ─────────────────────────────────────────────────────
        JPanel scoreRow = new JPanel(new BorderLayout(20, 0));
        scoreRow.setBackground(BG_CARD);

        scoreLabel = new JLabel(r.getScore() + "");
        scoreLabel.setFont(FONT_SCORE);
        scoreLabel.setForeground(strengthColor);
        scoreLabel.setBorder(new EmptyBorder(0, 0, 0, 4));

        JLabel scoreOf = new JLabel("/ 100");
        scoreOf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        scoreOf.setForeground(TEXT_MUTED);

        JPanel scoreNumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        scoreNumPanel.setBackground(BG_CARD);
        scoreNumPanel.add(scoreLabel);
        scoreNumPanel.add(scoreOf);

        strengthLabel = new JLabel(strengthText);
        strengthLabel.setFont(FONT_STRENGTH);
        strengthLabel.setForeground(strengthColor);

        JPanel scoreTextPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        scoreTextPanel.setBackground(BG_CARD);
        scoreTextPanel.add(scoreNumPanel);
        scoreTextPanel.add(strengthLabel);

        // Crack time + entropy on the right
        crackTimeLabel = new JLabel("Crack time: " + r.getCrackTime());
        crackTimeLabel.setFont(FONT_BODY);
        crackTimeLabel.setForeground(TEXT_MUTED);

        entropyLabel = new JLabel(String.format("Entropy: %.1f bits", r.getEntropy()));
        entropyLabel.setFont(FONT_BODY);
        entropyLabel.setForeground(TEXT_MUTED);

        JPanel metaPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        metaPanel.setBackground(BG_CARD);
        metaPanel.add(crackTimeLabel);
        metaPanel.add(entropyLabel);

        scoreRow.add(scoreTextPanel, BorderLayout.WEST);
        scoreRow.add(metaPanel, BorderLayout.EAST);

        // ── Progress bar ──────────────────────────────────────────────────
        scoreBar = new JProgressBar(0, 100);
        scoreBar.setValue(r.getScore());
        scoreBar.setStringPainted(false);
        scoreBar.setForeground(strengthColor);
        scoreBar.setBackground(BG_INPUT);
        scoreBar.setBorder(BorderFactory.createEmptyBorder());
        scoreBar.setPreferredSize(new Dimension(0, 8));

        // ── Issues ────────────────────────────────────────────────────────
        JLabel issuesLbl = styledLabel("Detected Issues");
        issuesArea = buildTextArea(r.getIssues().stream()
            .map(s -> "  ✗  " + s)
            .reduce("", (a, b) -> a + (a.isEmpty() ? "" : "\n") + b), COLOR_ISSUE);

        // ── Suggestions ───────────────────────────────────────────────────
        JLabel suggestLbl = styledLabel("Improvement Suggestions");
        suggestionsArea = buildTextArea(r.getSuggestions().stream()
            .map(s -> "  ✓  " + s)
            .reduce("", (a, b) -> a + (a.isEmpty() ? "" : "\n") + b), COLOR_SUGGEST);

        // ── Assemble ──────────────────────────────────────────────────────
        resultPanel.add(scoreRow);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(scoreBar);
        resultPanel.add(Box.createVerticalStrut(12));
        resultPanel.add(issuesLbl);
        resultPanel.add(Box.createVerticalStrut(4));
        JPanel issuesWrap = new JPanel(new BorderLayout());
        issuesWrap.setBackground(BG_INPUT);
        issuesWrap.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 90), 1, true));
        issuesWrap.add(issuesArea, BorderLayout.CENTER);
        resultPanel.add(issuesWrap);
        resultPanel.add(Box.createVerticalStrut(12));
        resultPanel.add(suggestLbl);
        resultPanel.add(Box.createVerticalStrut(4));
        JPanel suggestionsWrap = new JPanel(new BorderLayout());
        suggestionsWrap.setBackground(BG_INPUT);
        suggestionsWrap.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 90), 1, true));
        suggestionsWrap.add(suggestionsArea, BorderLayout.CENTER);
        resultPanel.add(suggestionsWrap);

        if (r.getStrength() != AnalysisResult.Strength.STRONG) {
            JLabel generatedLabel = styledLabel("Recommended Strong Passwords");
            JPanel generatedPanel = buildRecommendationList(adaptiveGenerator.generate(originalPassword, 5));

            resultPanel.add(Box.createVerticalStrut(12));
            resultPanel.add(generatedLabel);
            resultPanel.add(Box.createVerticalStrut(4));
            JPanel generatedWrap = new JPanel(new BorderLayout());
            generatedWrap.setBackground(BG_INPUT);
            generatedWrap.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 90), 1, true));
            generatedWrap.add(generatedPanel, BorderLayout.CENTER);
            resultPanel.add(generatedWrap);
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private JPanel buildRecommendationList(List<AdaptivePasswordGenerator.GeneratedRecommendation> generated) {
        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(BG_INPUT);
        list.setBorder(new EmptyBorder(8, 10, 8, 10));

        if (generated.isEmpty()) {
            JLabel empty = new JLabel("No recommendations generated.");
            empty.setForeground(TEXT_MUTED);
            empty.setFont(FONT_BODY);
            list.add(empty);
            return list;
        }

        int rank = 1;
        for (AdaptivePasswordGenerator.GeneratedRecommendation g : generated) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(BG_INPUT);
            row.setBorder(new EmptyBorder(4, 0, 4, 0));

            JLabel passwordLabel = new JLabel(rank + ")  " + g.getPassword());
            passwordLabel.setFont(FONT_MONO);
            passwordLabel.setForeground(TEXT_PRIMARY);

            JButton copyButton = new JButton("Copy");
            copyButton.setFont(FONT_BODY);
            copyButton.setBackground(new Color(60, 60, 90));
            copyButton.setForeground(Color.WHITE);
            copyButton.setFocusPainted(false);
            copyButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            copyButton.addActionListener(e -> copyToClipboard(g.getPassword()));

            row.add(passwordLabel, BorderLayout.CENTER);
            row.add(copyButton, BorderLayout.EAST);
            list.add(row);
            rank++;
        }
        return list;
    }

    private void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit()
            .getSystemClipboard()
            .setContents(new StringSelection(text), null);
    }

    // ── UI helpers ────────────────────────────────────────────────────────

    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 75), 1, true),
            new EmptyBorder(18, 20, 18, 20)
        ));
        return card;
    }

    private JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_LABEL);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setOpaque(true);
        return btn;
    }

    private JTextArea buildTextArea(String text, Color fg) {
        JTextArea area = new JTextArea(text);
        area.setFont(FONT_BODY);
        area.setForeground(fg);
        area.setBackground(BG_INPUT);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(8, 10, 8, 10));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        return area;
    }

    // ── Entry point ───────────────────────────────────────────────────────

    public static void main(String[] args) {
        // Use system look and feel as base, then override with custom colors
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new PasswordAnalyzerUI().setVisible(true));
    }
}
