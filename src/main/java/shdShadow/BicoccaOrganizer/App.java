package shdShadow.BicoccaOrganizer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;
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
		//FirstWindow a = new FirstWindow("BicoccaOrganizer", 600, 360, false);
		//a.showWindow();
		LoginWindow l = new LoginWindow("Login to Bicocca Services", 450, 240, false, cond);
		l.showWindow();
		//try{l.testSelenium();}catch(Exception e){}
	}
}	
	