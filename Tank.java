import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class to represent a tank monster.
 * Tanks appear every 5 waves, have massive amounts of health, do lots of
 * damage to the player and move slowly.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class Tank extends Monster {

    /**
     * Constructs a new tank monster.
     * The current wave is used to determine stats for the monster.
     *
     * @param currentWave The current monster wave.
     */
    public Tank(int currentWave) {
        super(currentWave);

        speed = 20.0;
        health = 800.0 * (currentWave / 5);

        damage = 25;
        pointValue = 1000;
        moneyValue = 1000;


        try {
            BufferedImage spriteImage = ImageIO.read(new File("tank.png"));
            mySprite = new Sprite(spriteImage, 2, 1, 2);
        } catch (IOException e) {
            System.out.println("ERROR: Could not read image for monster");
            System.out.println(e.getMessage());
        }
    }
}
