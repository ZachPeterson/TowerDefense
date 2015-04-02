import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class to represent a basic tower. These towers fire red lasers, do a decent
 * amount of damage at a moderate fire rate and gain damage for each kill.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class BasicTower extends Tower {

    /**
     * Constructs a new BasicTower at the given x and y coordinates.
     *
     * @param x The x coordinate to construct a new BasicTower at.
     * @param y The y coordinate to construct a new BasicTower at.
     */
    public BasicTower(int x, int y) {
        super(x, y);
        attackRate = 1.0;

        range = 100;
        cost = 400;

        damage = 20;

        damagePerKill = 0.2;

        laserDisplayTime = 0.15;

        laserColor = new Color(255, 0, 0);

        try {
            myImage = ImageIO.read(new File("basicTower.png"));
        } catch (IOException e) {
            System.out.println("ERROR: Could not read tower image");
            System.out.println(e.getMessage());
        }
    }
}
