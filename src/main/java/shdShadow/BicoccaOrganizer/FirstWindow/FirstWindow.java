package shdShadow.BicoccaOrganizer.FirstWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Window;
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
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import shdShadow.BicoccaOrganizer.DialogWindowTemplate;

public class FirstWindow  extends DialogWindowTemplate {
    private static final String PREF_NODE = "shdShadow.BicoccaOrganizer";
    private static final String SHOW_KEY = "showWelcome";
    private static final Preferences prefs = Preferences.userRoot().node(PREF_NODE);
    public FirstWindow(String title, int width, int hegiht, boolean isResizable) {
        super(title, width, hegiht, isResizable);
    }
    @Override
    public void closeWindow(){
        this.dispose();
    }
    /** Call at startup; only shows on first run (unless reset). */
    @Override
    public void showWindow() {
        // DEBUG ONLY: always restore the welcome window
        Preferences.userRoot()
                .node("shdShadow.BicoccaOrganizer")
                .remove("showWelcome");
        //modifies system users preferences so that it knows when this windows has already been shown
        if (prefs.getBoolean(SHOW_KEY, true)) {
            buildWelcomeFrame();
            super.setVisible(true);
        }
    }

    private void buildWelcomeFrame() {
        //Creates the top header pane. 
        //contains a small thanks message a description of this project
        JEditorPane header = createHeaderPane();
        // Center: profile pic + version info with clickable links
        JPanel main = createMainPanel();
        // Bottom: “Made with ❤️” + checkbox + Close button and don't show again button
        JPanel bottom = createBottomPanel();
        // Assemble the main pain of the Frame of this window with the three previously created panels
        super.getContentPane().setLayout(new BorderLayout());
        super.getContentPane().add(header, BorderLayout.NORTH);
        super.getContentPane().add(main, BorderLayout.CENTER);
        super.getContentPane().add(bottom, BorderLayout.SOUTH);
    }

    private JEditorPane createHeaderPane() {
        String html = "<html><body style='font-family:sans-serif; "
                + "font-size:16px; text-align:center;'>"
                + "<p>Welcome to <b>Bicocca Organizer</b>!<br>"
                + "First launch after download.<br>"
                + "BicoccaOrganizer is open source. You can check the code"
                + "<a href='https://github.com/shdShadow/BicoccaOrganizer'> here</a></p>"
                + "</body></html>";

        JEditorPane pane = new JEditorPane("text/html", html);
        pane.setEditable(false);
        pane.setOpaque(false);
        //pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pane.addHyperlinkListener(linkListener(this));
        return pane;
    }

    private JPanel createMainPanel() {
        JPanel middle = new JPanel(new BorderLayout(10, 10));
        middle.setBorder(new LineBorder(Color.BLACK, 1, true));

        // Profile picture (left)
        JPanel picPanel = new JPanel();
        ImageIcon icon = new ImageIcon(
                FirstWindow.class.getResource("/shdShadow/BicoccaOrganizer/resources/umbra.png"));
        Image img = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
        picPanel.add(new JLabel(new ImageIcon(img)));
        middle.add(picPanel, BorderLayout.WEST);
        String versionHtml = "<html><body style='font-family:sans-serif; font-size:14px;'>"
                + "BicoccaOrganizer α 0.0.1<br>"
                + "Check out my "
                + "<a href='https://github.com/shdShadow/'> GitHub </a> or support me by <a href=''>buying a coffee</a>"
                + "</body></html>";

        JEditorPane versionPane = new JEditorPane("text/html", versionHtml);
        versionPane.setEditable(false);
        versionPane.setOpaque(false);
        versionPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        versionPane.addHyperlinkListener(linkListener(this));
        middle.add(versionPane, BorderLayout.CENTER);

        return middle;
    }

    private JPanel createBottomPanel() {
        // Left: signature
        JLabel madeWithLove = new JLabel("Made with ❤️ by shdShadow");
        madeWithLove.setFont(madeWithLove.getFont().deriveFont(java.awt.Font.ITALIC, 12f));
        madeWithLove.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Right: checkbox + close
        JCheckBox dontShow = new JCheckBox("Don’t show this again");
        JButton close = new JButton("Close");
        close.addActionListener(e -> closeAndPersist(this, dontShow.isSelected()));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.add(dontShow);
        right.add(close);

        // Combine
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        bottom.add(madeWithLove, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);
        return bottom;
    }

    private HyperlinkListener linkListener(Window frame) {
        return new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(URI.create(e.getURL().toString()));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Could not open link.");
                    }
                }
            }
        };
    }

    private static void closeAndPersist(Window parent, boolean hideForever) {
        prefs.putBoolean(SHOW_KEY, !hideForever);
        try {
            prefs.flush();
        } catch (Exception ignored) {
        }
        parent.dispose();
    }
}
