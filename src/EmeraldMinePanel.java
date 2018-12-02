import javax.swing.*;
import java.awt.*;

public class EmeraldMinePanel extends JPanel {

    private final JLabel[][] panel;
    private World world;

    public EmeraldMinePanel(){

        //WorldObject[][] test = world.getWorld();
        GridLayout layout = new GridLayout(world.getRows(), world.getCols(),0,0);
        setLayout(layout);

        // Create a label for each cell
        panel = new JLabel[world.getRows()][world.getCols()];
        for (int r = 0; r < world.getRows(); r++) {
            for (int c = 0; c < world.getCols(); c++) {
                panel[r][c] = new JLabel();
                // Add the label. the layout chooses where it is placed
                // the gridlayout places them in reading order
                add(panel[r][c]);
            }
        }
        redrawWorld(world);
    }

    public void redrawWorld(World world){

        String icon ="";
        this.world = world;
        int r = 0;
        while(r<world.getRows()) {
            for (int c = 0; c < world.getCols(); ++c) {
                switch (world.getWorldObject(r, c)) {
                    case ".": icon = "space"; break;
                    case "#": icon = "dirt"; break;
                    case "a": icon = "alien"; break;
                    case "p": icon = "player"; break;
                    case "r": icon = "rock"; break;
                    case "d": icon = "diamond"; break;
                    case "e": icon = "emerald"; break;
                }
                panel[r][c].setIcon(new ImageIcon(icon + ".png"));
                ++r;
            }
        }
        repaint();
    }
}