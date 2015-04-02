import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class to represent a mega tower. These towers fire green lasers, do a TON
 * of damage at a VERY SLOW fire rate and gain nothing for each kill.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class MegaTower extends Tower {

    /**
     * Constructs a new MegaTower at the given x and y coordinates.
     *
     * @param x The x coordinate to construct a new MegaTower at.
     * @param y The y coordinate to construct a new MegaTower at.
     */
    public MegaTower(int x, int y) {
        super(x, y);
        attackRate = 5.0;

        range = 200;
        cost = 2000;

        damage = 1000;

        damagePerKill = 0.0;

        laserDisplayTime = 0.15;

        laserColor = new Color(0, 255, 0);

        try {
            myImage = ImageIO.read(new File("megaTower.png"));
        } catch (IOException e) {
            System.out.println("ERROR: Could not read tower image");
            System.out.println(e.getMessage());
        }
    }
}
