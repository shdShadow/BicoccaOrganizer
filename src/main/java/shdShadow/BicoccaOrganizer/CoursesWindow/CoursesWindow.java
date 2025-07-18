package shdShadow.BicoccaOrganizer.CoursesWindow;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shdShadow.BicoccaOrganizer.WindowTemplate;
import shdShadow.BicoccaOrganizer.DataManager.AcademicYearsManager;
import shdShadow.BicoccaOrganizer.ErrorWindows.ScrapingErrorWindow;
import shdShadow.BicoccaOrganizer.Exceptions.CookiesInvalidExcpetion;
import shdShadow.BicoccaOrganizer.scrape.mainScrape;
import shdShadow.BicoccaOrganizer.util.Constants;
import shdShadow.BicoccaOrganizer.util.Shared;
import shdShadow.BicoccaOrganizer.util.UserCookies;

public class CoursesWindow extends WindowTemplate {
    private mainScrape scraper;
    private List<AcademicYear> years;
    private AcademicYearsManager ym;
    private Map<Course, JLabel> courseLabels = new HashMap<Course, JLabel>();
    private Map<Course, JPanel> courseRows = new HashMap<Course, JPanel>();
    private Map<AcademicYear, JPanel> yearHeaders = new HashMap<AcademicYear, JPanel>();
    private JPanel mainPanel;
    private JComboBox<String> filterComboBox;

    public CoursesWindow(String title, int width, int height, boolean isResizable, Shared condivisa) {
        super(title, width, height, true, condivisa);
        years = new ArrayList<AcademicYear>();
        ym = new AcademicYearsManager(condivisa);
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showWindow() {
        scraper = new mainScrape(UserCookies.buildCookieMap(condivisa.getCookie()));
        years = ym.loadFromJson();
        if (years == null || years.isEmpty()) {
            try {
                years = scraper.scrapeCourses(Constants.COURSES_URL);
            } catch (CookiesInvalidExcpetion ex) {
                JOptionPane.showMessageDialog(this,
                        "Session cookies are no longer valid. Please log in again.",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                ScrapingErrorWindow.show(this, Constants.COURSES_URL);
                return;
            }

            // TODO: handle case where scraping fails
        }

        // Create the main content panel
        mainPanel = createMainPanel();

        // Create and add the filter panel
        JPanel filterPanel = createFilterPanel();

        // Create a container panel to hold both filter and main panels
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.decode("#1e1e1e"));
        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(mainPanel, BorderLayout.CENTER);

        populateCoursesPanel();

        addScrollPaneToWindow(containerPanel);
        this.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#1e1e1e"));
        return mainPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(Color.decode("#2d2d30"));
        filterPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel filterLabel = new JLabel("Filter courses: ");
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        filterComboBox = new JComboBox<>();
        filterComboBox.addItem("All courses");
        filterComboBox.addItem("Passed");
        filterComboBox.addItem("Studying");
        filterComboBox.addItem("Not passed");
        filterComboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        filterComboBox.setBackground(Color.decode("#3c3c3c"));
        filterComboBox.setForeground(Color.WHITE);
        filterComboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        filterComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });

        JPanel filterContainer = new JPanel();
        filterContainer.setLayout(new BoxLayout(filterContainer, BoxLayout.X_AXIS));
        filterContainer.setOpaque(false);
        filterContainer.add(filterLabel);
        filterContainer.add(Box.createHorizontalStrut(10));
        filterContainer.add(filterComboBox);

        filterPanel.add(filterContainer, BorderLayout.WEST);
        return filterPanel;
    }

    private void populateCoursesPanel() {
        mainPanel.removeAll();

        for (AcademicYear year : years) {
            JPanel headerPanel = createHeaderPanel(year);
            JPanel coursePanel = createCoursePanel(year);

            addHeaderToggleBehavior(headerPanel, coursePanel);
            mainPanel.add(headerPanel);
            mainPanel.add(coursePanel);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void applyFilter() {
        String selectedFilter = (String) filterComboBox.getSelectedItem();
        Course.ExamStatus filterStatus = null;

        switch (selectedFilter) {
            case "Passed":
                filterStatus = Course.ExamStatus.PASSED;
                break;
            case "Studying":
                filterStatus = Course.ExamStatus.STUDYING;
                break;
            case "Not passed":
                filterStatus = Course.ExamStatus.NOT_PASSED;
                break;
            case "All courses":
            default:
                filterStatus = null; // Show all courses
                break;
        }

        // Apply filter to all course rows
        for (Map.Entry<Course, JPanel> entry : courseRows.entrySet()) {
            Course course = entry.getKey();
            JPanel courseRow = entry.getValue();

            if (filterStatus == null || course.getExamStatus() == filterStatus) {
                courseRow.setVisible(true);
            } else {
                courseRow.setVisible(false);
            }
        }

        // Hide year headers if no courses are visible in that year
        for (AcademicYear year : years) {
            boolean hasVisibleCourses = false;
            for (Course course : year.getCourses()) {
                JPanel courseRow = courseRows.get(course);
                if (courseRow != null && courseRow.isVisible()) {
                    hasVisibleCourses = true;
                    break;
                }
            }

            // Find the header panel for this year and hide/show it
            JPanel headerPanel = yearHeaders.get(year);
            if (headerPanel != null) {
                headerPanel.setVisible(hasVisibleCourses);
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createHeaderPanel(AcademicYear year) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(55, 55, 60));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 10));

        JLabel headerLabel = new JLabel(year.getName());
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        headerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        headerPanel.add(headerLabel, BorderLayout.WEST);
        yearHeaders.put(year, headerPanel);
        return headerPanel;
    }

    private JPanel createCoursePanel(AcademicYear year) {
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        coursePanel.setBackground(Color.decode("#1e1e1e"));
        coursePanel.setVisible(false); // collapsed initially

        for (Course course : year.getCourses()) {
            JPanel courseRow = RowBuilder.createCourseRow(course, courseLabels, () -> reload(),
                    () -> ym.saveToJson(years));
            courseRows.put(course, courseRow);
            coursePanel.add(courseRow);
        }

        return coursePanel;
    }

    private void addHeaderToggleBehavior(JPanel headerPanel, JPanel coursePanel) {
        Component[] components = headerPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel headerLabel = (JLabel) comp;
                headerLabel.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        coursePanel.setVisible(!coursePanel.isVisible());
                        getContentPane().revalidate();
                    }
                });
            }
        }
    }

    private void addScrollPaneToWindow(JPanel mainPanel) {
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        getContentPane().add(scrollPane);
    }

    private void reload() {
        for (Course course : courseLabels.keySet()) {
            JLabel label = courseLabels.get(course);
            JPanel row = courseRows.get(course);

            label.setText(course.getCorrectCourseName(null));
            row.setBackground(course.getCorrectBackgroundColor());
        }

        // Reapply the current filter after reload
        if (filterComboBox != null) {
            applyFilter();
        }

    }

    @Override
    public void closeWindow() {

    }

}
