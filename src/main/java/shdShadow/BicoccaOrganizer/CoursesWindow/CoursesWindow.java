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
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
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
import java.util.TreeMap;

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
    
    public CoursesWindow(String title, int width, int height, boolean isResizable, Shared condivisa) {
        super(title, width, height, true, condivisa);
        years = new ArrayList<AcademicYear>();
        ym = new AcademicYearsManager(condivisa);
        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showWindow() {
        scraper = new mainScrape(UserCookies.buildCookieMap(condivisa.getCookie()));
        years = ym.loadFromJson();
        if(years == null || years.isEmpty()) {
            try{
                years = scraper.scrapeCourses(Constants.COURSES_URL);   
            }catch (CookiesInvalidExcpetion ex){
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
            
            //TODO: handle case where scraping fails
        }
        JPanel mainPanel = createMainPanel();
        mainPanel.revalidate();
        for (AcademicYear year : years) {
            JPanel headerPanel = createHeaderPanel(year);
            JPanel coursePanel = createCoursePanel(year);

            addHeaderToggleBehavior(headerPanel, coursePanel);
            mainPanel.add(headerPanel);
            mainPanel.add(coursePanel);
        }

        addScrollPaneToWindow(mainPanel);
        this.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#1e1e1e"));
        return mainPanel;
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
        return headerPanel;
    }

    private JPanel createCoursePanel(AcademicYear year) {
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));
        coursePanel.setBackground(Color.decode("#1e1e1e"));
        coursePanel.setVisible(false); // collapsed initially

        for (Course course : year.getCourses()) {
            JPanel courseRow = createCourseRow(course);
            coursePanel.add(courseRow);
        }

        return coursePanel;
    }

    private JPanel createCourseRow(Course course) {
        JPanel courseRow = new JPanel(new BorderLayout());
        courseRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        courseRow.setBackground(getCorrectBackgroundColor(course));
        courseRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)),
                new EmptyBorder(10, 20, 10, 10)));

        JLabel courseLabel = new JLabel(getCorrectCourseName(course, null));
        courseLabel.setForeground(Color.decode("#dddddd"));
        courseLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        courseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        courseLabels.put(course, courseLabel);

        JButton renameButton = createRenameButton(course);

        JComboBox examStatusList = createComboBoxStatus(course);
        courseRow.add(courseLabel, BorderLayout.WEST);
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
        optionsPanel.setOpaque(false);
        optionsPanel.add(renameButton);
        optionsPanel.add(Box.createHorizontalStrut(10)); // spacing
        optionsPanel.add(examStatusList);
        courseRow.add(optionsPanel, BorderLayout.EAST);
        addCourseRowBehavior(courseRow, course);

        return courseRow;
    }

    private JComboBox createComboBoxStatus(Course c){
        JComboBox<String> examStatusList = new JComboBox<>();
        examStatusList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        //examStatusList.setBackground(Color.decode("#1e1e1e"));
        examStatusList.setForeground(Color.WHITE);
        examStatusList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        examStatusList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        for (Course.ExamStatus status : Course.ExamStatus.values()) {
            examStatusList.addItem(status.name());
        }

        examStatusList.setSelectedItem(c.getExamStatus().name());

        examStatusList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) examStatusList.getSelectedItem();
                c.setExamStatus(Course.ExamStatus.valueOf(selectedItem));
                reload();
                ym.saveToJson(years);
            }
        });

        return examStatusList;
    }
    private JButton createRenameButton(Course course) {
        ImageIcon renameIcon = new ImageIcon(
                CoursesWindow.class.getResource("/shdShadow/BicoccaOrganizer/resources/modify.png"));
        Image img = renameIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        renameIcon = new ImageIcon(img);

        JButton button = new JButton(renameIcon);
        button.setToolTipText("Rename Course");

        button.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name for the course: (keep it blank if you want to restore the original name)", getCorrectCourseName(course, null));
            if (newName != null && !newName.trim().isEmpty()) {
                course.setCustomName(newName);
                reload();
                ym.saveToJson(years);
            }else{
                course.setCustomName("");
                reload();
                ym.saveToJson(years);
            }
        });

        return button;
    }

    private void addCourseRowBehavior(JPanel courseRow, Course course) {
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
                courseRow.setBackground(getCorrectBackgroundColor(course));
            }
        });
    }
    private Color getCorrectBackgroundColor(Course course){
        switch (course.getExamStatus()) {
            case PASSED:
                return Constants.BGCOLOR_EXAM_PASSED;
            case NOT_PASSED:
                return Constants.BGCOLOR_EXAM_NOT_PASSED;
            case STUDYING:
                return Constants.BGCOLOR_EXAM_STUDYING;
            default:
                return new Color(30, 30, 30);
        }
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
    private String getCorrectCourseName(Course c, String filter){
        if (c.getCustomName() != null && !c.getCustomName().isBlank()){
            return c.getCustomName();
        }else{
            return c.getName();
        }
    }
    private void reload(){
        for (Map.Entry<Course, JLabel> entry : courseLabels.entrySet()) {
            Course course = entry.getKey();
            JLabel label = entry.getValue();
            label.setText(getCorrectCourseName(course, null));
        }
        
    }
    @Override
    public void closeWindow() {

    }

}
