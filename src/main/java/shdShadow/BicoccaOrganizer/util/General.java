package shdShadow.BicoccaOrganizer.util;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

public class General {
    public static void addHyperlinkListener(JEditorPane editorPane) {
         editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(new URI(e.getURL().toString()));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Failed to open link: " + e.getURL(),
                            "Link Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
