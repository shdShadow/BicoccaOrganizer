package shdShadow.BicoccaOrganizer;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;

import shdShadow.BicoccaOrganizer.CoursesWindow.CoursesWindow;
import shdShadow.BicoccaOrganizer.FirstWindow.FirstWindow;
import shdShadow.BicoccaOrganizer.LoginWindow.LoginWindow;
import shdShadow.BicoccaOrganizer.util.Shared;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new FlatDarkLaf());
		System.out.println("Working dir: " + System.getProperty("user.dir"));
		System.out.println("User home: " + System.getProperty("user.home"));
		Shared cond = new Shared();
		JOptionPane.showMessageDialog(null, "An error has occurred while writing a file", "An error has occurred!", -1);
		FirstWindow a = new FirstWindow("BicoccaOrganizer", 600, 360, false, cond);
		a.showWindow();
		LoginWindow l = new LoginWindow("Login to Bicocca Services", 450, 240, false, cond);
		l.showWindow();
		//try{l.testSelenium();}catch(Exception e){}
		CoursesWindow c = new CoursesWindow("Your courses", 1280, 720, false, cond);
		c.showWindow();
	}
}	
	