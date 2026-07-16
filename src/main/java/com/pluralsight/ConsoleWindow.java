package com.pluralsight;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.*;

public class ConsoleWindow {


    private static final Pattern ANSI = Pattern.compile("\u001B\\[[;\\d]*m");

    private JTextPane outputPane;
    private JTextField inputField;
    private StyledDocument doc;


    private AttributeSet defaultStyle;
    private AttributeSet inputStyle;
    private AttributeSet currentStyle;

    // ansi code -> style, so we can support more than just blue
    private Map<String, AttributeSet> ansiStyles;

    private PipedOutputStream inputPipe;

    public void open() {
        try {

            SwingUtilities.invokeAndWait(this::buildWindow);
        } catch (Exception e) {
            throw new RuntimeException("Could not open the console window", e);
        }

        redirectStreams();
    }

    private void buildWindow() {
        JFrame frame = new JFrame("Seattle Mariners 2026 Finances");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 720);
        frame.setLocationRelativeTo(null); // center it on the screen

        // monospaced or the ledger tables wont line up
        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 12);


        outputPane = new JTextPane();
        outputPane.setEditable(false);
        outputPane.setFont(mono);
        outputPane.setBackground(Color.BLACK);
        outputPane.setMargin(new Insets(8, 8, 8, 8));
        doc = outputPane.getStyledDocument();

        StyleContext styles = StyleContext.getDefaultStyleContext();
        defaultStyle = styles.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE);
        AttributeSet blueStyle = styles.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0x4F, 0xC3, 0xF7));
        AttributeSet greenStyle = styles.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0x81, 0xC7, 0x84));
        AttributeSet redStyle = styles.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0xE5, 0x73, 0x73));
        inputStyle = styles.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0x9C, 0xCC, 0x65));
        currentStyle = defaultStyle;

        // only codes in this map get recognized, anything else falls back to default
        ansiStyles = new HashMap<>();
        ansiStyles.put("\u001B[0m", defaultStyle);  // RESET
        ansiStyles.put("\u001B[34m", blueStyle);    // BLUE
        ansiStyles.put("\u001B[32m", greenStyle);   // GREEN (revenue)
        ansiStyles.put("\u001B[31m", redStyle);     // RED (expense)


        outputPane.setFocusable(false);

        inputField = new JTextField();
        inputField.setFont(mono);
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(new Color(0x9C, 0xCC, 0x65));
        inputField.setCaretColor(new Color(0x9C, 0xCC, 0x65));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x33, 0x33, 0x33)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        inputField.addActionListener(e -> submit(inputField.getText()));

        // only the output scrolls; the input field is pinned to the bottom so it never leaves the screen
        JScrollPane scroll = new JScrollPane(outputPane);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.BLACK);

        frame.add(scroll, BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);
        frame.setVisible(true);
        inputField.requestFocusInWindow();
    }

    private void redirectStreams() {
        try {

            OutputStream toPane = new OutputStream() {
                @Override
                public void write(int b) {
                    write(new byte[]{(byte) b}, 0, 1);
                }

                @Override
                public void write(byte[] b, int off, int len) {
                    append(new String(b, off, len, StandardCharsets.UTF_8));
                }
            };
            System.setOut(new PrintStream(toPane, true, StandardCharsets.UTF_8));


            inputPipe = new PipedOutputStream();
            System.setIn(new PipedInputStream(inputPipe));

        } catch (IOException e) {
            throw new RuntimeException("Could not redirect the streams", e);
        }
    }


    private void submit(String line) {
        inputField.setText("");


        insert(line + "\n", inputStyle);

        try {
            inputPipe.write((line + "\n").getBytes(StandardCharsets.UTF_8));
            inputPipe.flush();
        } catch (IOException e) {
            insert("Input error: " + e.getMessage() + "\n", defaultStyle);
        }

        scrollToBottom();
    }

    private void append(String text) {
        SwingUtilities.invokeLater(() -> {
            Matcher matcher = ANSI.matcher(text);
            int last = 0;


            while (matcher.find()) {
                insert(text.substring(last, matcher.start()), currentStyle);
                currentStyle = ansiStyles.getOrDefault(matcher.group(), defaultStyle);
                last = matcher.end();
            }

            insert(text.substring(last), currentStyle);
            scrollToBottom();
        });
    }

    private void insert(String text, AttributeSet style) {
        if (text.isEmpty()) return;

        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {

        }
    }


    // keep the newest output line in view; the input field itself is always visible at the bottom
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> outputPane.setCaretPosition(doc.getLength()));
    }
}