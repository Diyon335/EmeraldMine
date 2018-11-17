
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Author: Cameron Browne (based on code by Marc Lanctot).
 * @author cambolbro
 * Note: if you use this code, add your name and ID to this header!
 *
 * Diyon - i6176139
 */

//-----------------------------------------------------------------------------

class World {
    private int rows, cols;  // dimension of world in rows and columns
    //int rows, cols;  // dimension of world in rows and columns
    private WorldObject[][] world;

    private int emeraldsRemaining;  // target number of emeralds
    //add diamonds and rocks
    private int diamonds;
    private int rocks;

    public static final int Playing = 0;  // game in progress
    public static final int Win     = 1;  // player win
    public static final int Loss    = 2;  // player loss
    private int status;

    public static final int Off = -1;  // off-board cell

    private RC playerAt = null;
    private RC alienAt  = null;

    private String fileName;
    private BufferedReader in;
    private int errorRow;
    private int errorCol;

    class RC {
        int row = -1;
        int col = -1;

        public RC(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        public void set(final int r, final int c) {
            row = r;
            col = c;
        }

        public boolean matches(final RC other)
        {
            return row == other.row && col == other.col;
        }
    }

    /**
     * Not needed for assignemtn 3
     * @param rows
     * @param cols
     * @param emeralds
     * @param diamonds
     * @param rocks
     */
    /*
    public World(final int rows, final int cols, final int emeralds, final int diamonds, final int rocks) {
        this.rows = rows;
        this.cols = cols;
        this.emeraldsRemaining = emeralds;

        this.diamonds= diamonds;
        this.rocks= rocks;
        //init();
    }*/

    public int getRows(){return this.rows;}
    public int getCols(){return this.cols;}

    public World (String fileName) throws BadFileFormatException {
        this.fileName = fileName;
        this.parseFile();
    }

    public void parseFile() throws BadFileFormatException {
        try {
            this.in = new BufferedReader(new FileReader(new File((this.fileName))));

            String line = in.readLine();
            this.rows = Integer.parseInt(line);
            //System.out.println(this.rows);

            line = in.readLine();
            this.cols = Integer.parseInt(line);
            //System.out.println(this.cols);

            line = in.readLine();
            this.emeraldsRemaining = Integer.parseInt(line);
            //System.out.println(this.emeraldsRemaining);

            this.world = new WorldObject[rows][cols];

            line = in.readLine();
            int boardRow = 0;
            while (line != null){
                for (int i = 0; i < cols; ++i) {
                    switch (line.charAt(i)) {
                        case 'd': ++diamonds; world[boardRow][i] = new Diamond(); break;
                        case 'r': ++rocks; world[boardRow][i] = new Rock(); break;
                        case '.': this.world[boardRow][i] = new Space(); break;
                        case '#': this.world[boardRow][i] = new Dirt(); break;
                        case 'p': playerAt = new RC(boardRow, i); world[playerAt.row][playerAt.col] = new Player(); break;
                        case 'a': alienAt = new RC(boardRow, i); world[alienAt.row][alienAt.col] = new Alien(); break;
                        case 'e': this.world[boardRow][i] = new Emerald(); break;
                        default: this.errorRow = boardRow; this.errorCol = i; throw new BadFileFormatException("Incorrect file format", this.errorRow, this.errorCol);
                    }
                }
                ++boardRow;
                line = in.readLine();
            }
            in.close();
            status = Playing;
            //System.out.println(toString());
        }
        catch (Exception e){
            e.printStackTrace();
            throw new BadFileFormatException("Incorrect file format", this.errorRow , this.errorCol);
        }
    }

    public int status() {return status;}

    /**
     * Not needed for assignment 3
     */
    /*
    private void init() {
        // Initialise the map
        world = new WorldObject[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                world[r][c] = new Dirt();

        // Create list of cells and shuffle
        final List<Integer> cells = new ArrayList<Integer>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final int cell = row * cols + col;
                cells.add(new Integer(cell));
            }
            Collections.shuffle(cells);
        }

        // Add the player
        int cell = cells.get(0).intValue();
        playerAt = new RC(cell/cols, cell%cols);
        world[playerAt.row][playerAt.col] = new Player();

        // Add the alien
        cell = cells.get(1).intValue();
        alienAt = new RC(cell/cols, cell%cols);
        world[alienAt.row][alienAt.col] = new Alien();

        // Add the emeralds
        for (int e = 0; e < emeraldsRemaining+1; e++) {
            cell = cells.get(2+e).intValue();
            world[cell / cols][cell % cols] = new Emerald();
        }

        // Add the diamonds
        for (int d= 0; d < diamonds; d++) {
            cell = cells.get((3+1)+emeraldsRemaining+d).intValue();
            world[cell / cols][cell % cols] = new Diamond();
        }

        //Add the rocks
        for (int r= 0; r < rocks; r++) {
            cell = cells.get((4+1)+emeraldsRemaining+diamonds+r).intValue();
            world[cell / cols][cell % cols] = new Rock();
        }

        status = Playing;  // game is now active
    }*/


    public boolean inBounds(int row, int col) {
        return (row >= 0 && row < rows && col >= 0 && col < cols);
    }
    //returns character from WorldObject
    public char getMove() {
        return new Player().getMove();
    }
    //changed valid chars to w,a,s or d
    public boolean validMove(final char ch) {
        return ch == 'w' || ch == 'a' || ch == 's' || ch == 'd';
    }

    //applies the move and returns status of game
    public int applyMove(final char ch) {
        alienMove();
        playerMove(ch);
        return status;
    }

   //makes the player move
    public void playerMove(final char ch)
    {
        // Check the player's destination
        int nextRow = playerAt.row;
        int nextCol = playerAt.col;
        switch (ch)
        {
            case 'w': nextRow--; break;
            case 's': nextRow++; break;
            case 'a': nextCol--; break;
            case 'd': nextCol++; break;
            default: System.out.println("Unexpected char '" + ch + "'.");
        }
        world[playerAt.row][playerAt.col] = new Space();

        //if he goes out of bounds or walks into an alien
        if (!inBounds(nextRow, nextCol))
        {
            System.out.println("You fell off the world!");
            status = Loss;  // player dies
            return;

            //if it's an alien, game is lost
        } else if (world[nextRow][nextCol].canMove() && !world[nextRow][nextCol].isPlayer()){

            System.out.println("You were eaten!");
            status = Loss;
            return;

            //if the next place is not edible (like a rock), do nothing
        } else if (!world[nextRow][nextCol].isEdible()){

            world[playerAt.row][playerAt.col] = new Player();
            System.out.println("Can't move! Find another way");
            return;

        }//if it's an emerald or diamond or dirt
        else if (world[nextRow][nextCol].isEdible()){

            emeraldsRemaining -= world[nextRow][nextCol].getEmeraldValue();

        }

        // Move the player
        playerAt.set(nextRow, nextCol);
        world[nextRow][nextCol] = new Player();

        //if the required emerald value is achieved,game is won
        if (emeraldsRemaining <= 0) {
            System.out.println("You win!");
            status = Win;  // player wins
        }
    }

    public void alienMove() {
        if (alienAt == null)
            return;  // alien is off the board and no longer active

        // Move the alien in a random direction
        int nextRow = alienAt.row;
        int nextCol = alienAt.col;

        switch (new Alien().getMove()) {
            //could be either one of these cases, u, d , l or r
            case 'd':
                nextRow++;
                break;
            case 'u':
                nextRow--;
                break;
            case 'r':
                nextCol++;
                break;
            case 'l':
                nextCol--;
                break;
        }
        world[alienAt.row][alienAt.col] = new Space();

        if (inBounds(nextRow, nextCol)) {
            // Alien remains in the game
            alienAt.set(nextRow, nextCol);
            world[alienAt.row][alienAt.col] = new Alien();

            if (alienAt.matches(playerAt))
                status = Loss;  // alien kills player
        } else if (!inBounds(nextRow, nextCol)) {
            System.out.println("The Alien fell off the world!");
            alienAt = null;  // alien exits the game


        }//if it's an emerald or diamond
        else if (world[nextRow][nextCol].isEdible() && !world[alienAt.row][alienAt.col].isPlayer()) {
            System.out.println("The Alien got something!");
            //the alien getting an emerald or diamond won't add towards the player's victory
            //emeraldsRemaining -= world[nextRow][nextCol].getEmeraldValue();

            //if the next place is not edible, like a rock
        } else if (!world[nextRow][nextCol].isEdible()) {

            world[alienAt.row][alienAt.col] = new Alien();
        }
    }

    @Override
    public String toString() {
        String str = "Emeralds remaining: " + emeraldsRemaining + "\n";
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                str += this.world[row][col];
                str += " ";
            }
            str += "\n";
        }
        return str;
    }
}