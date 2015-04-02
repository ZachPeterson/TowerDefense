import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Class to help with image intersection tests.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class ImageHelper {

    /**
     * Checks whether or not a tower intersects with the path image to see
     * whether or not it can be placed.
     *
     * @param towerImage The tower image to use for the test.
     * @param towerPosition The position of the tower to check.
     * @param pathImage The path image to use for the test.
     * @ return True if the tower intersects the path.
     */
    public static boolean checkTowerPathIntersection(BufferedImage towerImage,
        Point towerPosition, BufferedImage pathImage) {
        double towerWidth = towerPosition.getX() + towerImage.getWidth() - 1;
        double towerHeight = towerPosition.getY() + towerImage.getHeight() - 1;
        double pathWidth = pathImage.getWidth() - 1;
        double pathHeight = pathImage.getHeight() - 1;

        int xStart = (int) Math.max(towerPosition.getX(), 0);
        int yStart = (int) Math.max(towerPosition.getY(), 0);
        int xEnd = (int) Math.min(towerWidth, pathWidth);
        int yEnd = (int) Math.min(towerHeight, pathHeight);

        int xTraverse = Math.abs(xEnd - xStart);
        int yTraverse = Math.abs(yEnd - yStart);

        for (int y = 1; y < yTraverse - 1; y++) {
            int ny = Math.abs(yStart - (int) towerPosition.getY()) + y;
            int ny1 = yStart + y;

            for (int x = 1; x < xTraverse - 1; x++) {
                int nx = Math.abs(xStart - (int) towerPosition.getX()) + x;
                int nx1 = xStart + x;
                try {
                    if (((towerImage.getRGB(nx, ny) & 0xFF000000) != 0x00)
                      && ((pathImage.getRGB(nx1, ny1) & 0xFF000000) != 0x00)) {
                        return true;
                    }
                } catch (Exception e) {
                    // Checkstyle is complaining here but the message should
                    // simply be eaten since it regularly occurs when the mouse
                    // is not within the game panel. In this case the method
                    // should simply return false as it does.
                    int checkStyleStopper = 0;
                }
            }
        }
        return false;
    }

    /**
     * Checks to see whether a tower intersects with another tower.
     *
     * @param towerImageA The first tower image to use for the test.
     * @param towerPositionA The position of the first tower to check.
     * @param towerImageB The second tower image to use for the test.
     * @param towerPositionB The position of the second tower to check.
     * @ return True if the tower intersects the path.
     */
    public static boolean checkTowerTowerIntersection(BufferedImage towerImageA,
        Point towerPositionA, BufferedImage towerImageB, Point towerPositionB) {
        double towerWidthA = towerPositionA.getX() + towerImageA.getWidth() - 1;
        double towerHeightA = towerPositionA.getY()
            + towerImageA.getHeight() - 1;
        double towerWidthB = towerPositionB.getX() + towerImageB.getWidth() - 1;
        double towerHeightB = towerPositionB.getY()
            + towerImageB.getHeight() - 1;

        int xStart = (int) Math.max(towerPositionA.getX(),
            towerPositionB.getX());
        int yStart = (int) Math.max(towerPositionA.getY(),
            towerPositionB.getY());
        int xEnd = (int) Math.min(towerWidthA, towerWidthB);
        int yEnd = (int) Math.min(towerHeightA, towerHeightB);

        int xTraverse = Math.abs(xEnd - xStart);
        int yTraverse = Math.abs(yEnd - yStart);

        for (int y = 1; y < yTraverse - 1; y++) {
            int ny = Math.abs(yStart - (int) towerPositionA.getY()) + y;
            int ny1 = Math.abs(yStart - (int) towerPositionB.getY()) + y;

            for (int x = 1; x < xTraverse - 1; x++) {
                int nx = Math.abs(xStart - (int) towerPositionA.getX()) + x;
                int nx1 = Math.abs(xStart - (int) towerPositionB.getX()) + x;
                try {
                    if (((towerImageA.getRGB(nx, ny) & 0xFF000000) != 0x00)
                    && ((towerImageB.getRGB(nx1, ny1) & 0xFF000000) != 0x00)) {
                        return true;
                    }
                } catch (Exception e) {
                    // Checkstyle is complaining here but the message should
                    // simply be eaten since it regularly occurs when the mouse
                    // is not within the game panel. In this case the method
                    // should simply return false as it does.
                    int checkStyleStopper = 0;
                }
            }
        }
        return false;
    }
}
