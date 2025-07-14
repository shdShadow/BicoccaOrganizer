package shdShadow.BicoccaOrganizer;
import javax.swing.JDialog;

public abstract class DialogWindowTemplate extends JDialog implements IWindowTemplate {
    public DialogWindowTemplate(String title, int width, int height, boolean isResizable){
        super((JDialog) null, title, true);
        this.setSize(width, height);
        this.setResizable(isResizable);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
 
}
