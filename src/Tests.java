import java.io.IOException;

/**
 * @author Diyon
 * This class is used to test my static methods that create a world by reading from a text file
 */
public class Tests {
    /**
     * This method loads the file and calls the parseFile() method from World.java
     * It will throw an IOException if the file cannot be loaded or does not exist
     *
     * Corrected code retrieved from author: Cameron Browne
     */

    public static void testsLoadFile(final String fileName) {
        try {
            System.out.println("Testing file: "+fileName+"...");
            World world = new World(fileName);
            System.out.println("Successfully loaded world:\n"+world);
        }
        catch (BadFileFormatException bf){
            System.out.println(bf);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * Test loading all test files.
     */

    public static void testLoadFiles() {
        final String[] fileNames = {
                        "a.txt", "bad-file1.txt", "bad-file2.txt", "bad-file3.txt",
                        "bad-file4.txt", "bad-file5.txt", "bad-file6.txt"};
        for (String fileName : fileNames)
            Tests.testsLoadFile(fileName);
    }

}

