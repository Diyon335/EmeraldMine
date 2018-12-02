import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventTrapper implements KeyListener {

    private World world;

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    //needed
    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()){
            case KeyEvent.VK_UP : world.applyMove('u'); break;
            case KeyEvent.VK_DOWN : world.applyMove('d'); break;
            case KeyEvent.VK_LEFT : world.applyMove('l'); break;
            case KeyEvent.VK_RIGHT : world.applyMove('r'); break;
            default: break;
        }
    }
}
