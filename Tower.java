import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Class to represent the basic Tower in the game.
 * Each tower has a certain damage and attack speed.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class Tower {
    protected int cost;
    protected int range;
    private int x;
    private int y;
    protected BufferedImage myImage;
    protected double damage;
    private boolean canAttack;
    protected double attackRate;
    private double timeSinceLastAttack;
    private boolean displayLaser;
    private boolean isLaserFading;
    private int laserAlpha;
    private int laserAlphaStart;
    protected double laserDisplayTime;
    private double currentFadeTime;
    private Point laserTarget;
    private Timer startLasing;
    private Timer startLaserFade;
    private Timer endLaserFade;
    private int myKills;
    protected Color laserColor;
    protected double damagePerKill;

    /**
     * Constructs a new Tower at a given x and y coordinate.
     *
     * @param x The x coordinate to construct the Tower at.
     * @param y The y coordinate to construct the Tower at.
     */
    public Tower(int x, int y) {
        this.x = x;
        this.y = y;

        cost = 300;
        range = 60;

        canAttack = true;
        attackRate = 0.5;
        timeSinceLastAttack = attackRate;

        damage = 15;

        damagePerKill = 0.1;

        displayLaser = false;
        isLaserFading = false;
        laserAlphaStart = 160;
        laserAlpha = 0;
        laserDisplayTime = 0.15;
        currentFadeTime = 0.0;

        startLasing = new Timer();
        startLaserFade = new Timer();
        endLaserFade = new Timer();

        myKills = 0;

        myImage = null;

        laserColor = new Color(255, 255, 255);
    }

    /**
     * Returns the cost of the tower.
     *
     * @return The cost of the tower.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Returns the range of the tower.
     *
     * @return The range of the tower.
     */
    public int getRange() {
        return range;
    }

    /**
     * Returns the x position of the tower.
     *
     * @return The x position of the tower.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y position of the tower.
     *
     * @return The y position of the tower.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the image for the tower.
     *
     * @return The image for the tower.
     */
    public BufferedImage getImage() {
        return myImage;
    }

    /**
     * Main update function for this tower.
     * Requires a Vector of monsters for radius checking and attacking.
     *
     * @param dt The time differential to use.
     * @param monsters Vector containing all of the monsters on the level.
     */
    public void update(double dt, Vector<Monster> monsters) {
        if (displayLaser) {
            if (isLaserFading) {
                currentFadeTime += dt;
                laserAlpha = (int) (laserAlphaStart
                    - (currentFadeTime / (laserDisplayTime / 2))
                    * laserAlphaStart);
            }
        }
        timeSinceLastAttack += dt;
        if (!canAttack && timeSinceLastAttack >= attackRate) {
            canAttack = true;
        }
        if (canAttack) {
            boolean hasAttacked = false;
            int i = 0;
            while (!hasAttacked && i < monsters.size()) {
                if (Math.sqrt(Math.pow((monsters.get(i).getX()
                    + monsters.get(i).getWidth() / 2)
                    - (x + myImage.getWidth() / 2), 2)
                    + Math.pow((monsters.get(i).getY()
                    + monsters.get(i).getHeight() / 2)
                    - (y + myImage.getHeight() / 2), 2)) <= range) {
                    boolean killedMonster = monsters.get(i).takeDamage(damage
                        + damagePerKill * myKills);
                    hasAttacked = true;
                    timeSinceLastAttack = 0.0;
                    canAttack = false;

                    if (killedMonster) {
                        myKills++;
                    }

                    laserTarget = new Point((int) (monsters.get(i).getX()
                        + monsters.get(i).getWidth() / 2),
                        (int) (monsters.get(i).getY()
                        + monsters.get(i).getHeight() / 2));
                    startLasing.schedule(new StartLasingTask(), 0);
                }
                i++;
            }
        }
    }

    /**
     * Renders the tower and all graphics associated with it.
     * This includes the laser and the kill count.
     *
     * @param g The Graphics to draw with.
     */
    public void render(Graphics g) {
        Color oldColor = g.getColor();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        String killNumber = "" + myKills;
        g2.drawString(killNumber, x, y);
        if (displayLaser) {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(new Color(laserColor.getRed(), laserColor.getGreen(),
                laserColor.getBlue(), (laserAlpha > 0 ? laserAlpha : 0)));
            g2.draw(new Line2D.Double(x + myImage.getWidth() / 2,
                y + myImage.getHeight() / 2, laserTarget.getX(),
                laserTarget.getY()));
        }
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillOval(x - range + myImage.getWidth() / 2,
            y - range + myImage.getHeight() / 2, range * 2, range * 2);
        g2.setColor(oldColor);
        g2.drawImage(myImage, x, y, null);
    }

    /**
     * Private timer class to call when the laser should start drawing.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class StartLasingTask extends TimerTask {
        /**
         * Runs this timer.
         * Tells the tower to display the laser, it is not fading, and that
         * the laser alpha should be set to its maximum. Also schedules a fade
         * task to be run later.
         */
        public void run() {
            displayLaser = true;
            isLaserFading = false;
            laserAlpha = laserAlphaStart;

            startLaserFade.schedule(new StartLaserFadeTask(),
                (int) (laserDisplayTime / 2 * 1000));
        }
    }

    /**
     * Private timer class to call when the laser should start fading.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class StartLaserFadeTask extends TimerTask {
        /**
         * Runs this timer.
         * Tells the tower that the laser is fading and resets the fade timer
         * to use for alpha value calculation.
         */
        public void run() {
            isLaserFading = true;
            currentFadeTime = 0.0;

            endLaserFade.schedule(new EndLaserFadeTask(),
                (int) (laserDisplayTime / 2 * 1000));
        }
    }

    /**
     * Private timer class to call when the laser should stop displaying.
     *
     * @author Zachary Peterson
     * @version 1.0
     */
    private class EndLaserFadeTask extends TimerTask {
        /**
         * Runs this timer.
         * Tells the tower that the laser should not display or fade.
         */
        public void run() {
            isLaserFading = false;
            displayLaser = false;
        }
    }
}
