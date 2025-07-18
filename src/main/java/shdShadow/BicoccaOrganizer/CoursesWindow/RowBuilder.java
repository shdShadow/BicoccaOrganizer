package shdShadow.BicoccaOrganizer.CoursesWindow;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import shdShadow.BicoccaOrganizer.util.Constants;

public class RowBuilder {
    public static JPanel createCourseRow(Course course, Map<Course, JLabel> courseLabels, Runnable reloadCallback,
            Runnable saveCallback) {
        JPanel courseRow = new JPanel(new BorderLayout());
        courseRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        courseRow.setBackground(course.getCorrectBackgroundColor());
        courseRow.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)),
                new EmptyBorder(10, 20, 10, 10)));

        JLabel courseLabel = new JLabel(course.getCorrectCourseName(null));
        courseLabel.setForeground(Color.decode("#dddddd"));
        courseLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        courseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        courseLabels.put(course, courseLabel);

        JButton renameButton = createRenameButton(course, reloadCallback, saveCallback);

        JComboBox<String> examStatusList = createComboBoxStatus(course, reloadCallback, saveCallback);
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

    private static void addCourseRowBehavior(JPanel courseRow, Course course) {
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
                switch (course.getExamStatus()) {
                    case PASSED:
                        courseRow.setBackground(Constants.BGCOLOR_EXAM_PASSED_HOVER);
                        break;
                    case NOT_PASSED:
                        courseRow.setBackground(Constants.BGCOLOR_EXAM_NOT_PASSED_HOVER);
                        break;
                    case STUDYING:
                        courseRow.setBackground(Constants.BGCOLOR_EXAM_STUDYING_HOVER);
                        break;
                    default:
                        courseRow.setBackground(Constants.NOT_DEFINED_COLOR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                courseRow.setBackground(course.getCorrectBackgroundColor());
            }
        });
    }

    private static JComboBox<String> createComboBoxStatus(Course c, Runnable reload, Runnable save) {
        JComboBox<String> examStatusList = new JComboBox<>();
        examStatusList.setFont(new Font("SansSerif", Font.PLAIN, 12));
        // examStatusList.setBackground(Color.decode("#1e1e1e"));
        examStatusList.setForeground(Color.WHITE);
        examStatusList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        examStatusList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        examStatusList.addItem("Not defined");
        examStatusList.addItem("Exam passed");
        examStatusList.addItem("Studying");
        examStatusList.addItem("Not passed");

        examStatusList.setSelectedItem(c.getExamStatus().name());

        examStatusList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) examStatusList.getSelectedItem();
                switch (selectedItem) {
                    case "Not defined":
                        c.setExamStatus(Course.ExamStatus.NOT_DEFINED);
                        break;
                    case "Exam passed":
                        c.setExamStatus(Course.ExamStatus.PASSED);
                        break;
                    case "Studying":
                        c.setExamStatus(Course.ExamStatus.STUDYING);
                        break;
                    case "Not passed":
                        c.setExamStatus(Course.ExamStatus.NOT_PASSED);
                        break;
                }
                reload.run();
                save.run();
            }
        });

        return examStatusList;
    }

    private static JButton createRenameButton(Course course, Runnable reload, Runnable save) {
        ImageIcon renameIcon = new ImageIcon(
                CoursesWindow.class.getResource("/shdShadow/BicoccaOrganizer/resources/modify.png"));
        Image img = renameIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        renameIcon = new ImageIcon(img);

        JButton button = new JButton(renameIcon);
        button.setToolTipText("Rename Course");

        button.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(
                    "Enter new name for the course: (keep it blank if you want to restore the original name)",
                    course.getCorrectCourseName(null));
            if (newName != null && !newName.trim().isEmpty()) {
                course.setCustomName(newName);
            } else {
                course.setCustomName("");
            }
            reload.run();
            save.run();
        });

        return button;
    }

}
