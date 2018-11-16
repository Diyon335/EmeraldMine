/**
 * @author Diyon
 * This class is used to test my static methods that create a world by reading from a text file
 */
public class Tests {

    public Tests(){}
    /**
     * This method loads the file and calls the parseFile() method from World.java
     * It will throw an IOException if the file cannot be loaded or does not exist
     */
    public static void testsLoadFile(){

        try {
            World world= new World("a.txt");
            world.parseFile();
        } catch (Exception io){
            System.out.println("An Exception was caught: "+io.getMessage());
            io.printStackTrace();
        }
    }
}
