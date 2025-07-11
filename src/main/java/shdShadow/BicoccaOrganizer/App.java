package shdShadow.BicoccaOrganizer;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new FlatDarkLaf());
		init.showWelcomeIfNeeded();
	}
}	
	