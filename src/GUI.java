import javax.swing.*;
import java.awt.*;

public class GUI {

    private JFrame frame;


    public void init(){
        //initialize frame
        this.frame = new JFrame("Emerald Mine");
        this.frame.setPreferredSize(new Dimension(640,640));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //create the panel
        EmeraldMinePanel panel = new EmeraldMinePanel();
        panel.setSize(640,640);
        frame.add(panel);

        // Add the key event trapper to the frame
        frame.addKeyListener(new KeyEventTrapper());

        frame.pack();
        frame.setVisible(true);

    }
}




