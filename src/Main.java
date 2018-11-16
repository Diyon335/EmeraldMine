import java.io.IOException;

/**
 * Author: Cameron Browne (based on code by Marc Lanctot).
 * @author cambolbro
 * Note: if you use this code, add your name and ID to this header!
 *
 * Diyon - i6176139
 */

public class Main {

    public static void main(final String[] args) throws IOException {

        //final World world = new World(20, 20, 16, 3, 5);
        Tests.testsLoadFile();

        // Play the game
        /**
         * Uncomment below
         */
        /*
        while (world.status() == World.Playing)
        {
            System.out.println(world);
            System.out.println("Enter move (w, a, s or d): ");
            final char ch = world.getMove();
            if (!world.validMove(ch))
                System.out.println("Not a valid move. Try one of: w, a, s, or d");
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
        }*/
    }
}
