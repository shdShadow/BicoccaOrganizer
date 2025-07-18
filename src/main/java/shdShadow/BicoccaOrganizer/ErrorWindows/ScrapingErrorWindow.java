package shdShadow.BicoccaOrganizer.ErrorWindows;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.bouncycastle.crypto.examples.JPAKEExample;

import shdShadow.BicoccaOrganizer.util.General;

import java.awt.BorderLayout;
import java.awt.Window;
public class ScrapingErrorWindow extends JDialog {
    public ScrapingErrorWindow(Window parent, String url){
        super(parent, "Scraping Error", ModalityType.APPLICATION_MODAL);
        setSize(500, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
        String html = "<html>"
                + "<body style='font-family:sans-serif;'>"
                + "<h2 style='color:#c00;'>An error occurred while scraping for your courses</h2>"
                + "<p><b>URL:</b> " + url + "</p>"
                + "<ul>"
                + "<li>Please check your internet connection.</li>"
                + "<li>Ensure that the website is accessible.</li>"
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
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        parent.dispose();
    }

    public static void show(Window parent, String url){
        ScrapingErrorWindow dialog = new ScrapingErrorWindow(parent, url);
        dialog.setVisible(true);
    }
}

        
