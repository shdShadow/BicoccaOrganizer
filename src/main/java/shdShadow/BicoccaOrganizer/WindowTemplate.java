package shdShadow.BicoccaOrganizer;

import javax.swing.JFrame;

import shdShadow.BicoccaOrganizer.Interfaces.IWindowTemplate;
import shdShadow.BicoccaOrganizer.util.Shared;

public abstract class WindowTemplate extends JFrame implements IWindowTemplate{
    public Shared condivisa;
    public WindowTemplate(String title, int width, int height, boolean isResizable, Shared cond){
        super(title);
        this.setSize(width, height);
        this.setResizable(isResizable);
        //maybe to change?
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.condivisa = cond;
    }
}
