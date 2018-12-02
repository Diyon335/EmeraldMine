import java.io.IOException;

/**
 * Author: Cameron Browne (based on code by Marc Lanctot).
 * @author cambolbro
 * Note: if you use this code, add your name and ID to this header!
 *
 * Diyon - i6176139
 */

public class Main {

    public static void main(final String[] args) throws BadFileFormatException, IOException {

        //final World world = new World(20, 20, 16, 3, 5);
        //Tests.testLoadFiles();
        World world = new World (20,20,7);
        //final World world = new World("a.txt");
        //System.out.println(world.getCols());
        //System.out.println(world);
        GUI gui = new GUI();
        gui.init();

        // Play the game

        while (world.status() == World.Playing) {
            System.out.println(world);
            System.out.println("Enter move (u, l, d or r): ");
            final char ch = world.getMove();
            if (!world.validMove(ch))
                System.out.println("Not a valid move. Try one of: u, l, d, or r");
            else
                world.applyMove(ch);
        }

        // Show the result
        System.out.println(world);
        switch (world.status())
        {
            case World.Win:  System.out.println("You win!"); break;
            case World.Loss: System.out.println("You lose. Bad luck."); break;
            default:         System.out.println("** Unexpected game outcome " + world.status() + ".");
        }
    }
}
