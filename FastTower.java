import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class to represent a fast tower. These towers fire blue lasers, do a little
 * bit more damage at a fast fire rate and gain damage AND fire rate for each
 * kill.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class FastTower extends Tower {

    /**
     * Constructs a new FastTower at the given x and y coordinates.
     *
     * @param x The x coordinate to construct a new FastTower at.
     * @param y The y coordinate to construct a new FastTower at.
     */
    public FastTower(int x, int y) {
        super(x, y);
        attackRate = 0.5;

        cost = 500;
        range = 60;

        damage = 15;

        damagePerKill = 0.2;

        laserDisplayTime = 0.15;

        laserColor = new Color(0, 0, 255);

        try {
            myImage = ImageIO.read(new File("fastTower.png"));
        } catch (IOException e) {
            System.out.println("ERROR: Could not read tower image");
            System.out.println(e.getMessage());
        }
    }
}
