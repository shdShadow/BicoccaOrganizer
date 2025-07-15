package shdShadow.BicoccaOrganizer.CoursesWindow;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.crypto.examples.JPAKEExample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import shdShadow.BicoccaOrganizer.WindowTemplate;
import shdShadow.BicoccaOrganizer.DataManager.AcademicYearsManager;
import shdShadow.BicoccaOrganizer.scrape.mainScrape;
import shdShadow.BicoccaOrganizer.util.Constants;
import shdShadow.BicoccaOrganizer.util.Shared;
import shdShadow.BicoccaOrganizer.util.UserCookies;

public class CoursesWindow extends WindowTemplate {
    private mainScrape scraper;
    private List<AcademicYear> years;
    private AcademicYearsManager ym;
    public CoursesWindow(String title, int width, int height, boolean isResizable, Shared condivisa) {
        super(title, width, height, isResizable, condivisa);
        scraper = new mainScrape(UserCookies.buildCookieMap(condivisa.getCookie()));
        years = new ArrayList<AcademicYear>();
        ym = new AcademicYearsManager(condivisa);
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void showWindow() {
        this.years = scraper.scrapeCourses(Constants.COURSES_URL);
        ym.saveToJson(years);
        years.clear();
        years = ym.loadFromJson();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#1e1e1e"));

        for (AcademicYear year : years) {
            // --- HEADER PANEL ---
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(55, 55, 60));
            headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            headerPanel.setBorder(new EmptyBorder(10, 15, 10, 10));

            JLabel headerLabel = new JLabel(year.getName());
            headerLabel.setForeground(Color.WHITE);
            headerLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
            headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            headerLabel.setOpaque(false);

            // --- COURSE PANEL (COLLAPSIBLE) ---
            JPanel coursePanel = new JPanel();
            coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
            coursePanel.setBackground(Color.decode("#1e1e1e"));
            coursePanel.setVisible(false); // collapsed initially

            for (Course course : year.getCourses()) {
                JPanel courseRow = new JPanel(new BorderLayout());
                courseRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
                courseRow.setBackground(new Color(30, 30, 30));
                courseRow.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)),
                        new EmptyBorder(10, 20, 10, 10)));

                JLabel courseLabel = new JLabel(course.getName());
                courseLabel.setForeground(Color.decode("#dddddd"));
                courseLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                courseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                // Make whole row clickable
                courseRow.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            Desktop.getDesktop().browse(new URI(course.getUrl()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        courseRow.setBackground(new Color(45, 45, 45));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        courseRow.setBackground(new Color(30, 30, 30));
                    }
                });

                courseRow.add(courseLabel, BorderLayout.WEST);
                coursePanel.add(courseRow);
            }

            // Toggle open/close on header click
            headerLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    coursePanel.setVisible(!coursePanel.isVisible());
                    getContentPane().revalidate();
                }
            });

            headerPanel.add(headerLabel, BorderLayout.WEST);
            mainPanel.add(headerPanel);
            mainPanel.add(coursePanel);
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smoother scrolling
        getContentPane().add(scrollPane);

        this.setVisible(true);
    }

    private JPanel createYearPanel(AcademicYear year) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JToggleButton toggle = new JToggleButton(year.getName());
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        coursePanel.setVisible(false);

        for (Course course : year.getCourses()) {
            JLabel link = new JLabel("<html><a href=''>" + course.getName() + "</a></html>");
            link.setCursor(new Cursor(Cursor.HAND_CURSOR));
            link.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI(course.getUrl()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            coursePanel.add(link);
        }
        toggle.addActionListener(e -> coursePanel.setVisible(toggle.isSelected()));
        panel.add(toggle, BorderLayout.NORTH);
        panel.add(coursePanel, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void closeWindow() {

    }

}
