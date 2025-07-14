package shdShadow.BicoccaOrganizer;

import javax.swing.JFrame;

public abstract class WindowTemplate extends JFrame implements IWindowTemplate{
    public WindowTemplate(String title, int width, int height, boolean isResizable){
        super(title);
        this.setSize(width, height);
        this.setResizable(isResizable);
        //maybe to change?
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
}
