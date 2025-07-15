package shdShadow.BicoccaOrganizer.LoginWindow;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;

import org.openqa.selenium.TimeoutException;

import shdShadow.BicoccaOrganizer.CookieErrorWindow;
import shdShadow.BicoccaOrganizer.DialogWindowTemplate;
import shdShadow.BicoccaOrganizer.DataManager.CookieManager;
import shdShadow.BicoccaOrganizer.util.Constants;
import shdShadow.BicoccaOrganizer.util.Requests;
import shdShadow.BicoccaOrganizer.util.Shared;

public class LoginWindow extends DialogWindowTemplate {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private CookieManager cookieManager;

    public LoginWindow(String title, int width, int height, boolean isResizable, Shared cond) {
        super(title, width, height, isResizable, cond);
        this.cookieManager = new CookieManager(cond);
    }

    @Override
    public void closeWindow() {
        this.dispose();
    }

    @Override
    public void showWindow() {
        condivisa.setCookie(cookieManager.loadFromJson());
        if (condivisa.getCookie() == null || !Requests.areCookiesValid(condivisa.getCookie())) {
            JPanel header = createHeaderPanel();
            JPanel middle = createMiddlePanel();
            JPanel bottom = createBottomPanel();
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            mainPanel.add(header, BorderLayout.NORTH);
            mainPanel.add(middle, BorderLayout.CENTER);
            mainPanel.add(bottom, BorderLayout.SOUTH);
            super.getContentPane().add(mainPanel);
            this.setVisible(true);
        } else {
            System.out.println("Cookies saved!");
            try {
                Requests.areCookiesValid(condivisa.getCookie());
            } catch (Exception e) {

            }
        }

    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel headerText = new JLabel("Please Log into your university account");
        headerText.setFont(new Font("SansSerif", Font.PLAIN, 22));
        headerText.setHorizontalAlignment(JLabel.CENTER);
        header.add(headerText, BorderLayout.CENTER);
        return header;
    }

    private JPanel createUsernamePanel() {
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel username = new JLabel("Username: ");
        usernameField = new JTextField(20);
        username.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernamePanel.add(username);
        usernamePanel.add(usernameField);
        return usernamePanel;
    }

    private JPanel createPasswordPanel() {
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel password = new JLabel("Password: ");
        passwordField = new JPasswordField(19);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                passwordField.getBorder(), new EmptyBorder(0, 1, 0, 0)));
        password.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordPanel.add(password);
        passwordPanel.add(passwordField);
        return passwordPanel;
    }

    private JPanel createMiddlePanel() {
        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.add(createUsernamePanel());
        middle.add(createPasswordPanel());
        return middle;
    }

    private JPanel createBottomPanel() {
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        JButton loginButton = new JButton("Log In");
        statusLabel = new JLabel(" ");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        bottom.add(statusLabel);
        loginButton.addActionListener(e -> {
            statusLabel.setText("Logging you in...");
            SwingUtilities.invokeLater(() -> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                try {
                    condivisa.setCookie(Requests.loginToServices(username, password));
                    CookieManager c = new CookieManager(condivisa);
                    statusLabel.setForeground(Color.GREEN);
                    statusLabel.setText("Logged in! This window will close shortly...");
                    c.saveToJson(condivisa.getCookie());
                    closeWindow();
                } catch (TimeoutException ex) {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Timed out! Check your network or your credentials!");
                    System.err.println(ex.getMessage());
                } catch (IOException ex) {
                    String fullPath = condivisa.getBaseDataPath() + Constants.COOKIES_FILE;
                    String message = "An error was encountered while trying to write to the cookies file.\n"
                            + "Full path: " + fullPath + "\n\n"
                            + "• Please check this directory's user permissions.\n"
                            + "• Ensure that you have enough disk space.\n\n"
                            + "If you believe this error is not your fault,\n"
                            + "feel free to open a pull request at:\n"
                            + "https://github.com/your-repo-url\n"
                            + "or write me an email: your.email@example.com";

                    //CookieErrorWindow.show(, condivisa.getBaseDataPath() + Constants.COOKIES_FILE);
                } catch (Exception ex) {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Unexpected error occurred!");
                    ex.printStackTrace();
                }
            });
        });
        bottom.add(loginButton);

        return bottom;
    }

}
