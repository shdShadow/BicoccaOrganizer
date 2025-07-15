package shdShadow.BicoccaOrganizer;
import javax.swing.JDialog;

import shdShadow.BicoccaOrganizer.Interfaces.IWindowTemplate;
import shdShadow.BicoccaOrganizer.util.Shared;

public abstract class DialogWindowTemplate extends JDialog implements IWindowTemplate {
    public Shared condivisa;
    public DialogWindowTemplate(String title, int width, int height, boolean isResizable, Shared condivisa){
        super((JDialog) null, title, true);
        this.setSize(width, height);
        this.setResizable(isResizable);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.condivisa = condivisa;
    }
 
}
