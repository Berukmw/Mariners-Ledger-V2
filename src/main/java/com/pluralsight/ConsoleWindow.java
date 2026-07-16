package com.pluralsight;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;

public class ConsoleWindow {


    private static final Pattern ANSI = Pattern.compile("\u001B\\[[;\\d]*m");

    private JTextPane outputPane;
    private JTextField inputField;
    private StyledDocument doc;


    private AttributeSet defaultStyle;
    private AttributeSet blueStyle;
    private AttributeSet inputStyle;
    private AttributeSet currentStyle;


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
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // center it on the screen

        // monospaced or the ledger tables wont line up
        Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 12);


        outputPane = new JTextPane() {
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        outputPane.setEditable(false);
        outputPane.setFont(mono);
        outputPane.setBackground(Color.BLACK);
        outputPane.setMargin(new Insets(8, 8, 8, 8));
        doc = outputPane.getStyledDocument();

        StyleContext styles = StyleContext.getDefaultStyleContext();
        defaultStyle = styles.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.WHITE);
        blueStyle = styles.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0x4F, 0xC3, 0xF7));
        inputStyle = styles.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(0x9C, 0xCC, 0x65));
        currentStyle = defaultStyle;


        outputPane.setFocusable(false);
        outputPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputField = new JTextField();
        inputField.setFont(mono);
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(new Color(0x9C, 0xCC, 0x65));
        inputField.setCaretColor(new Color(0x9C, 0xCC, 0x65));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x33, 0x33, 0x33)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputField.addActionListener(e -> submit(inputField.getText()));

        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, inputField.getPreferredSize().height));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.BLACK);
        content.add(outputPane);
        content.add(inputField);
        content.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.BLACK);

        frame.add(scroll, BorderLayout.CENTER);
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

        scrollToInput();
    }

    private void append(String text) {
        SwingUtilities.invokeLater(() -> {
            Matcher matcher = ANSI.matcher(text);
            int last = 0;


            while (matcher.find()) {
                insert(text.substring(last, matcher.start()), currentStyle);
                currentStyle = matcher.group().equals("\u001B[34m") ? blueStyle : defaultStyle;
                last = matcher.end();
            }

            insert(text.substring(last), currentStyle);
            scrollToInput();
        });
    }

    private void insert(String text, AttributeSet style) {
        if (text.isEmpty()) return;

        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {

        }
    }


    private void scrollToInput() {

        SwingUtilities.invokeLater(() -> inputField.scrollRectToVisible(
                new Rectangle(0, 0, inputField.getWidth(), inputField.getHeight())));
    }
}
