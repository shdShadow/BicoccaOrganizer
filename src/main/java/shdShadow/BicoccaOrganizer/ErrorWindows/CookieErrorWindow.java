package shdShadow.BicoccaOrganizer.ErrorWindows;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.Window;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import shdShadow.BicoccaOrganizer.util.General;

public class CookieErrorWindow extends JDialog {

    public CookieErrorWindow(Window parent, String fullPath) {
        super(parent, "Cookie Save Error", ModalityType.APPLICATION_MODAL);
        setSize(550, 300);
        setLocationRelativeTo(parent);
        setResizable(false);

        String html = "<html>"
                + "<body style='font-family:sans-serif;'>"
                + "<h2 style='color:#c00;'>An error occurred while writing the cookies file</h2>"
                + "<p><b>Full path:</b> " + fullPath + "</p>"
                + "<ul>"
                + "<li>Please check this directory's user permissions.</li>"
                + "<li>Ensure that you have enough disk space.</li>"
                + "</ul>"
                + "<p>If you believe this error is not your fault, you can:</p>"
                + "<ul>"
                + "<li><a href='https://github.com/shdShadow/BicoccaOrganizer'>Open a pull request on GitHub</a></li>"
                + "<li><a href='mailto:s.ballerini6@campus.unimib.it'>Write me an email</a></li>"
                + "</ul>"
                + "</body></html>";

        JEditorPane editorPane = new JEditorPane("text/html", html);
        editorPane.setEditable(false);
        editorPane.setOpaque(false);

        // Enable hyperlink clicks
        General.addHyperlinkListener(editorPane);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        parent.dispose();
    }
      public static void show(Window parent, String fullPath) {
        CookieErrorWindow dialog = new CookieErrorWindow(parent, fullPath);
        dialog.setVisible(true);
    }
}
