package shdShadow.BicoccaOrganizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.IOException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class init {
	public static JFrame firstRunWindow() {
		JFrame frame = new JFrame("Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 250);
        frame.setLocationRelativeTo(null); // Center the window

        // Create JEditorPane to render HTML
        JEditorPane htmlPane = new JEditorPane("text/html", WelcomeText());
        htmlPane.setEditable(false);
        htmlPane.setOpaque(false);
        htmlPane.setBorder(new LineBorder(Color.BLUE, 1));

        // Make links clickable
        htmlPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(URI.create(e.getURL().toString()));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Failed to open link.");
                    }
                }
            }
        });
        JPanel middleJPanel = new JPanel(new GridLayout(1,2,10,0));
        // Close button
        JButton closeButton = new JButton("Close Window");
        closeButton.addActionListener(e -> frame.dispose());
        closeButton.setBorder(new LineBorder(Color.RED, 1));

        // Layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.GREEN, 1));
        panel.add(htmlPane, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        return frame;
        
        
	}
	
	//Utility methods for this window:
	private static String WelcomeText() {
		String html = "<html><body style='font-family:sans-serif; font-size:12px; text-align:center;'>"
                + "<p>Hi! Thank you for choosing <b>Bicocca Organizer</b> by shdShadow.</p>"
                + "<p>This project is open source and the code is available at "
                + "<a href='https://github.com/shdShadow'>this GitHub link</a>.</p>"
                + "<p>If you wish to support me," 
                + "<a href=''>buy me a coffee!</a> </p>"
                + "</body></html>";
		
		return html;
	}
}
