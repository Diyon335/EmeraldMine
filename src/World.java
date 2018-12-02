import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Author: Cameron Browne (based on code by Marc Lanctot).
 * @author cambolbro
 * Note: if you use this code, add your name and ID to this header!
 *
 * Diyon - i6176139
 */

//-----------------------------------------------------------------------------

public class World {
    private int rows, cols;  // dimension of world in rows and columns
    private WorldObject[][] world; //initialise the world

    private int emeraldsRemaining;  // target number of emeralds
    //add diamonds and rocks
    private int diamonds;
    private int rocks;

    private final Random rng = new Random();

    public static final int Playing = 0;  // game in progress
    public static final int Win     = 1;  // player win
    public static final int Loss    = 2;  // player loss
    private int status;

    public static final int Off = -1;  // off-board cell

    /**
     * These variables below are for Assignment 3
     */
    private String fileName; //inpute file name
    //stores the location of where an error might occur
    private int errorRow;
    private int errorCol;

    /**
     * The following class can store a location
     */
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

        /**
         *
         * @param other - Checks if two locations are the same
         * @return - returns a boolean
         */
        public boolean matches(final RC other)
        {
            return row == other.row && col == other.col;
        }
    }

    private RC playerAt = null;
    private RC alienAt  = null;
    private RC supported = null;

    /**
     * Creates a world
     * @param rows - amount of rows
     * @param cols - amount of columns
     * @param emeralds - "" emeralds
     */

    public World(int rows, int cols, int emeralds) {
        this.rows = rows;
        this.cols = cols;
        this.emeraldsRemaining = emeralds;

        init();
    }

    public String getWorldObject(int r, int c){return this.world[r][c].toString();}

    /**
     * @return - returns rows or columns
     */

    public int getRows(){return rows;}
    public int getCols(){return cols;}

    public WorldObject[][] getWorld() {return this.world;}

    /**
     * Creates a world after reading a file
     * @param fileName - name of the input file
     * @throws BadFileFormatException - gets thrown if file does not follow a specific format
     */

    public World (String fileName) throws BadFileFormatException, IOException {
        this.parseFile(fileName);
    }

    /**
     * Parses the file from text to object so the game can be played
     * @throws BadFileFormatException - gets thrown if the file is not of a specific format
     */
    public void parseFile(String fileName) throws BadFileFormatException, IOException {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

        this.rows = Integer.parseInt(reader.readLine());
        this.cols = Integer.parseInt(reader.readLine());
        this.emeraldsRemaining = Integer.parseInt(reader.readLine());
        world = new WorldObject[rows][cols];

        int row = 0;
        int emeraldsInWorld = 0;
        String line = reader.readLine();

        while(line!=null){
            if (row>=rows){throw new BadFileFormatException("Too many rows",row,-1);}

            if (line.length() < cols){throw new BadFileFormatException("Too few columns (" + line.length() + ")", row, -1);}

            if (line.length() > cols){throw new BadFileFormatException("Too many columns (" + line.length() + ")", row, -1);}

            for (int col = 0; col < cols; col++)
            {
                // Create the object at this grid cell
                final char ch = line.charAt(col);
                world[row][col] = WorldObject.createFromChar(ch);
                if (world[row][col] == null)
                    throw new BadFileFormatException("Invalid character: " + ch, row, col);


                // Everything good so far, update the emerald count (if any)
                emeraldsInWorld += world[row][col].getEmeraldValue();
            }
            line = reader.readLine();
            row++;
        }

        // Do a final check of the world dimensions
        if (row != rows)
            throw new BadFileFormatException("Not enough rows (" + rows + ")", -1, -1);

        if (emeraldsInWorld < emeraldsRemaining)
            throw new BadFileFormatException("Not enough emeralds in the world: " + emeraldsInWorld, -1, -1);

        reader.close();
    }

    /**
     * @param exceptRow - unaffected row
     * @param exceptCol - unaffected col
     * @return - returns true if an object with mass falls on the player. false if not
     */
    private boolean applyGravity(int exceptRow, int exceptCol){

        boolean fellOnPlayer = false;

        for (int row = rows-2; row >= 0; row--)
            for (int col = 0; col < cols; col++)
            {
                if (row == exceptRow && col == exceptCol)
                    continue;  // do not apply gravity here

                int rowBelow = row+1;  // because rows are indexed bottom up

                if (world[row][col].hasMass() && world[rowBelow][col].isVulnerable())
                {
                    // Object with mass drops a row
                    if (world[rowBelow][col].isPlayer())
                    {
                        fellOnPlayer = true;  // player squashed
                    }
                    else if (world[rowBelow][col].isMonster())
                    {
                        System.out.println("Alien squashed!");
                        alienAt = null;  // remove the alien from the game
                    }

                    // Move this object down a row
                    world[rowBelow][col] = world[row][col];
                    world[row][col] = new Space();
                }
            }
        return fellOnPlayer;
    }

    public int status() {return status;}

    /**
     * Not needed for assignment 3
     * Creates a world if the first World constructor is called
     */

    private void init() {
        // Initialise the map
        world = new WorldObject[rows][cols];
        // Create list of cells and shuffle
        final List<WorldObject> cells = new ArrayList<>();

        cells.add(new Player());
        cells.add(new Alien());

        // Add the emeralds
        for (int e = 0; e < emeraldsRemaining+1; e++)
            cells.add(new Emerald());

        // Add the diamonds
        final int numDiamonds = 3;
        emeraldsRemaining += 3 * numDiamonds;
        for (int d = 0; d < numDiamonds; d++)
            cells.add(new Diamond());

        // Add some rocks
        final int numRocks = 4 + rng.nextInt(3);
        for (int r = 0; r < numRocks; r++)
            cells.add(new Rock());

        // Fill the rest with dirt
        while (cells.size() < rows * cols)
            cells.add(new Dirt());

        // Shuffle objects and put in world array
        Collections.shuffle(cells);
        for (int n = 0; n < cells.size(); n++)
        {
            int row = n / cols;
            int col = n % cols;

            world[row][col] = cells.get(n);

            if (world[row][col].isPlayer())
                playerAt = new RC(row, col);
            else if (world[row][col].isMonster())
                alienAt = new RC(row, col);
        }

        status = Playing;  // game is now active
    }

    /**
     * Checks if the specific position is in the bounds of the game
     * @param row - row of interest
     * @param col - column of interest
     * @return - returns true or false
     */
    public boolean inBounds(int row, int col) {
        return (row >= 0 && row < rows && col >= 0 && col < cols);
    }
    //returns character from WorldObject
    public char getMove() {
        return new Player().getMove();
    }
    //changed valid chars to w,a,s or d
    public boolean validMove(final char ch) {
        return ch == 'u' || ch == 'l' || ch == 'd' || ch == 'r';
    }

    /**
     * All moves are applied here
     * @param ch - character: u,d,l or r
     * @return - returns status of the game
     */
    public int applyMove(final char ch) {

        playerMove(ch);
        if (alienAt != null)
        {
            final char chAlien = world[alienAt.row][alienAt.col].getMove();
            System.out.println("Alien steps '" + chAlien + "'.");
            alienMove(chAlien);
        }

        // Apply gravity
        if
        (applyGravity
                (
                        supported == null ? -1 : supported.row,
                        supported == null ? -1 : supported.col
                )
        )
            status = Loss;  // player killed by something dropping on them

        return status;
    }

    /**
     * Player's move is made here
     * @param ch - character: u,d,l or r
     */
    public void playerMove(final char ch) {
        supported = null;

        final RC playerNext = stepTo(playerAt, ch);
        if (!inBounds(playerNext.row, playerNext.col) || world[playerNext.row][playerNext.col].isMonster()) {
            // Player dies
            status = Loss;
            world[playerAt.row][playerAt.col] = new Space();  // remove the player from this world
            return;
        }

        if (world[playerNext.row][playerNext.col].isEdible()) {
            // Stepping into an edible cell, decrease by its point value
            emeraldsRemaining -= world[playerNext.row][playerNext.col].getEmeraldValue();

            // Do not go below 0
            if (emeraldsRemaining <= 0)
                emeraldsRemaining = 0;

            // Check for a win
            if (emeraldsRemaining == 0)
                status = Win;

            // Move the player
            world[playerAt.row][playerAt.col] = new Space();
            playerAt.set(playerNext.row, playerNext.col);
            world[playerAt.row][playerAt.col] = new Player();

            // Check whether player supports an object with their head
            final int rowAbove = playerAt.row - 1;
            if (inBounds(rowAbove, playerAt.col) && world[rowAbove][playerAt.col].hasMass()) {
                // Player supports the object above
                supported = new RC(rowAbove, playerAt.col);
            }
        } else {
            // Can't step into an inedible object
            System.out.println("There is an obstacle in the way.");
        }
    }

    /**
     * Alien makes a random move here
     */

    public void alienMove(final char ch) {
        if (alienAt == null)
            return;  // alien is off the board and no longer active

        final RC alienNext = stepTo(alienAt, ch);

        if (!inBounds(alienNext.row, alienNext.col)) {
            world[alienAt.row][alienAt.col] = new Space();
            alienAt = null;  // alien exits the game

        } else if (world[alienNext.row][alienNext.col].isEdible()) {
            // Alien can move there
            if (world[alienNext.row][alienNext.col].isPlayer())
                status = Loss;  // alien will step onto player

            world[alienAt.row][alienAt.col] = new Space();
            alienAt.set(alienNext.row, alienNext.col);
            world[alienAt.row][alienAt.col] = new Alien();
        }
    }

    /**
     * @return Coordinate of step in the specified direction.
     */
    public RC stepTo(final RC from, final char dirn)
    {
        int dRow = 0;
        int dCol = 0;
        switch (dirn)
        {
            case 'u': dRow--; break;
            case 'd': dRow++; break;
            case 'l': dCol--; break;
            case 'r': dCol++; break;
            default: System.out.println("** World.stepTo(): Unexpected dirn '" + dirn + "'.");
        }
        return new RC(from.row+dRow, from.col+dCol);
    }

    /**
     * Prints a string representation of the game
     * @return - string
     */
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