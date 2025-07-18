package shdShadow.BicoccaOrganizer;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;

import shdShadow.BicoccaOrganizer.CoursesWindow.CoursesWindow;
import shdShadow.BicoccaOrganizer.FirstWindow.FirstWindow;
import shdShadow.BicoccaOrganizer.LoginWindow.LoginWindow;
import shdShadow.BicoccaOrganizer.Status.ApplicationStatus;
import shdShadow.BicoccaOrganizer.util.Shared;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new FlatDarkLaf());
		System.out.println("Working dir: " + System.getProperty("user.dir"));
		System.out.println("User home: " + System.getProperty("user.home"));
		Shared cond = new Shared();
		cond.status = ApplicationStatus.FIRST_WINDOW;
		FirstWindow a = new FirstWindow("BicoccaOrganizer", 600, 360, false, cond);
		LoginWindow l = new LoginWindow("Login to Bicocca Services", 450, 240, false, cond);
		CoursesWindow c = new CoursesWindow("Your courses", 1280, 720, false, cond);
		while (cond.status != ApplicationStatus.CRASHED) {
			// Show the appropriate window based on the current status
			switch (cond.status) {
				case FIRST_WINDOW:
					a.showWindow();
					cond.waitForChange(ApplicationStatus.FIRST_WINDOW);
					break;
				case LOGIN:
					l.showWindow();
					cond.waitForChange(ApplicationStatus.LOGIN);
					break;
				case COURSES_WINDOW:
					c.showWindow();
					cond.waitForChange(ApplicationStatus.COURSES_WINDOW);
					break;
				default:
					break;
			}
		}
	}

}
