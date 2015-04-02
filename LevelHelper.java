import java.awt.image.BufferedImage;
import java.util.Vector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * Class to help with loading level elements such as background images and
 * PathNode lists.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class LevelHelper {
    /**
     * Retrieves a background image from a specified level file.
     *
     * @param levelFile The file that the level data is contained in.
     * @param levelIndex The level number to load from.
     * @return The background image for the specified level.
     */
    public static BufferedImage loadBackgroundFromLevelFile(File levelFile,
        int levelIndex) {
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(levelFile);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Could not find given level file");
            System.out.println(e.getMessage());
        }

        String currentLine;

        if (fileInput != null) {
            while (fileInput.hasNextLine()) {
                currentLine = fileInput.nextLine();

                if (currentLine.charAt(0) == '<'
                    && currentLine.indexOf('/') == -1
                    && Integer.parseInt(currentLine.substring(
                        currentLine.indexOf('<') + 1,
                        currentLine.indexOf('>'))) == levelIndex) {
                    String nextLine = fileInput.nextLine();

                    while (!nextLine.substring(0, 2).equals("</")
                        && fileInput.hasNextLine()) {
                        if (nextLine.indexOf(' ') != 0) {
                            if (nextLine.substring(0,
                                nextLine.indexOf(' ')).equals(
                                    "backgroundImage")) {
                                try {
                                    BufferedImage returnImage
                                        = ImageIO.read(new File(
                                            nextLine.substring(
                                                nextLine.indexOf(' ') + 1,
                                                nextLine.length())));
                                    return returnImage;
                                } catch (IOException e) {
                                    System.out.println("ERROR: Could not read"
                                            + " background image for level "
                                            + levelIndex);
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                        nextLine = fileInput.nextLine();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Loads a path image from a given level file for the specified level.
     *
     * @param levelFile The level file to load data from.
     * @param levelIndex The level to load data for.
     * @return The loaded path image.
     */
    public static BufferedImage loadPathImageFromLevelFile(
        File levelFile, int levelIndex) {
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(levelFile);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Could not find given level file");
            System.out.println(e.getMessage());
        }

        String currentLine;

        if (fileInput != null) {
            while (fileInput.hasNextLine()) {
                currentLine = fileInput.nextLine();

                if (currentLine.charAt(0) == '<'
                    && currentLine.indexOf('/') == -1
                    && Integer.parseInt(currentLine.substring(
                        currentLine.indexOf('<') + 1, currentLine.indexOf('>')))
                            == levelIndex) {
                    String nextLine = fileInput.nextLine();

                    while (!nextLine.substring(0, 2).equals("</")
                        && fileInput.hasNextLine()) {
                        if (nextLine.indexOf(' ') != 0) {
                            if (nextLine.substring(0,
                                nextLine.indexOf(' ')).equals("pathImage")) {
                                try {
                                    BufferedImage returnImage = ImageIO.read(
                                        new File(nextLine.substring(
                                            nextLine.indexOf(' ') + 1,
                                            nextLine.length())));
                                    return returnImage;
                                } catch (IOException e) {
                                    System.out.println("ERROR: Could not read"
                                        + " path image for level "
                                        + levelIndex);
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                        nextLine = fileInput.nextLine();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Loads a PathNodeList from a given level file for the specified level.
     *
     * @param levelFile The level file to load data from.
     * @param levelIndex The level number to load data from.
     * @return The PathNodeList for the specified level.
     */
    public static Vector<PathNode> loadPathNodeListFromLevelFile(
        File levelFile, int levelIndex) {
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(levelFile);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Could not find given level file");
            System.out.println(e.getMessage());
        }

        String currentLine;

        if (fileInput != null) {
            while (fileInput.hasNextLine()) {
                currentLine = fileInput.nextLine();

                if (currentLine.charAt(0) == '<'
                    && currentLine.indexOf('/') == -1
                    && Integer.parseInt(currentLine.substring(
                        currentLine.indexOf('<') + 1,
                        currentLine.indexOf('>'))) == levelIndex) {
                    String nextLine = fileInput.nextLine();

                    while (!nextLine.substring(0, 2).equals("</")
                        && fileInput.hasNextLine()) {
                        if (nextLine.indexOf(' ') != 0) {
                            if (nextLine.substring(0,
                                nextLine.indexOf(' '))
                                .equals("pathNodeListFile")) {
                                File pathFile = null;
                                pathFile = new File(nextLine.substring(
                                    nextLine.indexOf(' ') + 1,
                                    nextLine.length()));

                                if (pathFile != null) {
                                    Scanner pathInput = null;

                                    try {
                                        pathInput = new Scanner(pathFile);
                                    } catch (FileNotFoundException e) {
                                        System.out.println(
                                            "ERROR: Could not load file "
                                            + nextLine.substring(
                                                nextLine.indexOf(' ') + 1,
                                                nextLine.length())
                                            + " for level " + levelIndex);
                                        System.out.println(e.getMessage());
                                    }

                                    Vector<PathNode> loadedNodes =
                                        new Vector<PathNode>();
                                    String readLine;

                                    while (pathInput.hasNextLine()) {
                                        readLine = pathInput.nextLine();

                                        int x = Integer.parseInt(
                                            readLine.substring(0,
                                                readLine.indexOf(',')));
                                        int y = Integer.parseInt(
                                            readLine.substring(
                                                readLine.indexOf(',') + 1,
                                                readLine.length()));

                                        PathNode nodeToAdd = new PathNode(x, y);

                                        loadedNodes.add(nodeToAdd);
                                    }

                                    return loadedNodes;
                                }
                            }
                        }
                        nextLine = fileInput.nextLine();
                    }
                }
            }
        }
        return null;
    }
}
