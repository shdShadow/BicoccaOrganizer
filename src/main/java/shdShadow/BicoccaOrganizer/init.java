package shdShadow.BicoccaOrganizer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.net.URI;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class init {
    private static final String PREF_NODE = "shdShadow.BicoccaOrganizer";
    private static final String SHOW_KEY  = "showWelcome";
    private static final Preferences prefs = Preferences.userRoot().node(PREF_NODE);

    /**
     * Call this at app startup. It will only display the window
     * if the user hasn’t opted out before.
     */
    public static void showWelcomeIfNeeded() {
        if (prefs.getBoolean(SHOW_KEY, true)) {
            JFrame frame = buildWelcomeFrame();
            frame.setVisible(true);
        }
    }

    private static JFrame buildWelcomeFrame() {
        JFrame frame = new JFrame("Welcome to Bicocca Organizer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 360);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Header with inline links
        JEditorPane header = createHeaderPane(frame);

        // Main content: image + version text
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new LineBorder(Color.BLACK, 1, true));

        JLabel pic = new JLabel();
        ImageIcon icon = new ImageIcon(
            init.class.getResource("/shdShadow/BicoccaOrganizer/resources/umbra.png")
        );
        Image img = icon.getImage()
                       .getScaledInstance(130, 130, Image.SCALE_SMOOTH);
        pic.setIcon(new ImageIcon(img));
        pic.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.add(pic, BorderLayout.WEST);

        JLabel version = new JLabel(
            "<html><body style='font-family:sans-serif; font-size:14px;'>"
            + "Version α 0.0.1<br>"
            + "Thanks for installing!<br>"
            + "Visit the <a href='https://github.com/shdShadow/BicoccaOrganizer'>GitHub repo</a>"
            + " or <a href='https://www.buymeacoffee.com/shdShadow'>buy me a coffee</a>."
            + "</body></html>"
        );
        version.setHorizontalAlignment(SwingConstants.LEFT);
        version.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        main.add(version, BorderLayout.CENTER);

        // Bottom: signature, checkbox + close button
        JLabel madeWithLove = new JLabel("Made with ❤️ by shdShadow");
        madeWithLove.setFont(madeWithLove.getFont()
                                         .deriveFont(Font.ITALIC, 12f));
        madeWithLove.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JCheckBox dontShow = new JCheckBox("Don’t show this again");
        dontShow.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JButton close = new JButton("Close");
        close.addActionListener(e -> closeAndPersist(frame, dontShow.isSelected()));

        JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomRight.add(dontShow);
        bottomRight.add(close);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(madeWithLove, BorderLayout.WEST);
        bottom.add(bottomRight, BorderLayout.EAST);

        // Compose the frame
        frame.getContentPane().add(header, BorderLayout.NORTH);
        frame.getContentPane().add(main,   BorderLayout.CENTER);
        frame.getContentPane().add(bottom, BorderLayout.SOUTH);

        return frame;
    }

    private static void closeAndPersist(JFrame frame, boolean hideForever) {
        prefs.putBoolean(SHOW_KEY, !hideForever);
        frame.dispose();
    }

    private static JEditorPane createHeaderPane(JFrame parent) {
        String html = "<html><body style='font-family:sans-serif; "
                    + "font-size:16px; text-align:center;'>"
                    + "<p>Welcome to <b>Bicocca Organizer</b>!<br>"
                    + "This is your first launch after download.<br>"
                    + "Open source on "
                    + "<a href='https://github.com/shdShadow/BicoccaOrganizer'>GitHub</a>"
                    + " and support me at "
                    + "<a href='https://www.buymeacoffee.com/shdShadow'>BuyMeACoffee</a>.</p>"
                    + "</body></html>";

        JEditorPane pane = new JEditorPane("text/html", html);
        pane.setEditable(false);
        pane.setOpaque(false);
        pane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(URI.create(e.getURL().toString()));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(parent, "Could not open link.");
                    }
                }
            }
        });
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return pane;
    }
}