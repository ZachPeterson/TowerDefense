import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class to represent a basic monster in the game.
 * All other monster types are derived from this base class.
 * Monsters follow a predefined path, take damage from towers, and do a certain
 * amount of damage to the player if they go off the left side of the screen.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class Monster {
    protected Sprite mySprite;
    private double x, y;
    protected double speed;
    private int nodeIndex;
    private double nodeArrivalDistance;
    protected double health;
    private boolean alive;
    protected int damage;
    protected int pointValue;
    protected int moneyValue;

    /**
     * Construct a new monster with stats based off of the current wave.
     *
     * @param currentWave The current monster wave.
     */
    public Monster(int currentWave) {
        PathNode startingNode = PathNodeList.getInstance().getNode(0);

        if (startingNode != null) {
            nodeIndex = 0;
            x = startingNode.getX();
            y = startingNode.getY();
        } else {
            System.out.println("ERROR: Could not get starting pathnode for"
                   + " monster");
        }

        nodeArrivalDistance = 2.0;

        speed = 40.0 +  2.0 * currentWave;

        health = 50.0 + 4.0 * currentWave;
        alive = true;

        damage = 20;

        pointValue = 100;
        moneyValue = 20;

        try {
            BufferedImage spriteImage = ImageIO.read(new File("monster.png"));
            mySprite = new Sprite(spriteImage, 2, 1, 4);
        } catch (IOException e) {
            System.out.println("ERROR: Could not read image for monster");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns whether the monster is currently alive or not.
     *
     * @return Whether the monster is currently alive or not.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Returns the current x position of the monster.
     *
     * @return The current x position of the monster.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the current y position of the monster.
     *
     * @return The current y position of the monster.
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the width of the monster's image.
     * Takes into account transformations for the sprite.
     *
     * @return The width of the monster's image.
     */
    public int getWidth() {
        return mySprite.getImage().getWidth();
    }

    /**
     * Returns the height of the monster's image.
     * Takes into account transformations for the sprite.
     *
     * @return The height of the monster's image.
     */
    public int getHeight() {
        return mySprite.getImage().getHeight();
    }

    /**
     * Deals damage to the monster. If the damage killed the monster, then the
     * method returns true so that the killing tower can add a kill to its
     * score.
     *
     * @param damage The damage to deal to the monster.
     * @return Whether or not the damage taken killed the monster.
     */
    public boolean takeDamage(double damage) {
        if (alive && health > 0) {
            health -= damage;
            if (health <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Main update for the monster. Handles all position changing, health
     * checking and other fun stuff.
     *
     * @param dt The time differential since the last frame.
     */
    public void update(double dt) {
        if (x + mySprite.getImage().getWidth() < 0) {
            MessageQueue.getInstance().push(
                new Message<Integer>(MessageRecipient.GAME_PANEL,
                MessageType.DEAL_DAMAGE, damage));
            alive = false;
        }
        if (health <= 0) {
            alive = false;
            MessageQueue.getInstance().push(
                new Message<Integer>(MessageRecipient.GAME_PANEL,
                MessageType.ADD_SCORE, pointValue));
            MessageQueue.getInstance().push(
                new Message<Integer>(MessageRecipient.GAME_PANEL,
                MessageType.ADD_MONEY, moneyValue));
        }
        if (alive) {
            PathNode currentNode
                = PathNodeList.getInstance().getNode(nodeIndex);
            if (currentNode != null) {
                double distanceToNode
                    = Math.sqrt(Math.pow(currentNode.getX() - x, 2)
                        + Math.pow(currentNode.getY() - y, 2));
                if (distanceToNode <= nodeArrivalDistance) {
                    nodeIndex++;
                    currentNode = PathNodeList.getInstance().getNode(nodeIndex);
                    if (currentNode != null) {
                        distanceToNode =
                            Math.sqrt(Math.pow(currentNode.getX() - x, 2)
                                + Math.pow(currentNode.getY() - y, 2));
                    } else {
                        x = -1000;
                        y = -1000;
                        alive = false;
                    }
                }
                if (currentNode != null) {
                    double directionX
                        = (currentNode.getX() - x) / distanceToNode;
                    double directionY
                        = (currentNode.getY() - y) / distanceToNode;
                    x += (directionX * speed * dt);
                    y += (directionY * speed * dt);
                }
            } else {
                x = -1000;
                y = -1000;
                alive = false;
            }
        }

        double directionX
            = PathNodeList.getInstance().getNode(nodeIndex).getX() - x;
        double directionY
            = PathNodeList.getInstance().getNode(nodeIndex).getY() - y;

        double rotation = Math.atan(directionY / directionX);

        if (directionX < 0) {
            rotation += Math.PI;
        } else if (directionY < 0) {
            rotation += Math.PI * 2;
        }
        mySprite.setRotation(rotation);
        mySprite.update(dt);
        mySprite.setPosition((int) x, (int) y);
    }

    /**
     * Renders the monster.
     *
     * @param g The current Graphics to draw with.
     */
    public void render(Graphics g) {
        mySprite.render(g);
    }

    /**
     * Returns whether this monster equals the other monster.
     * Implemented for use in the monster storage Vectors.
     *
     * @param obj The other object to check for equality.
     * @return True if this monster equals the other.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Monster)) {
            return false;
        }
        return (((Monster) obj).x == x
            && ((Monster) obj).y == y
            && ((Monster) obj).speed == speed
            && ((Monster) obj).nodeIndex == nodeIndex
            && ((Monster) obj).nodeArrivalDistance == nodeArrivalDistance
            && ((Monster) obj).health == health
            && ((Monster) obj).alive == alive);
    }

    /**
     * Returns the hashcode for this object.
     *
     * @Return The hashcode for this object.
     */
    @Override
    public int hashCode() {
        return (int) (x + 2 * y + 3 * speed + 4 * nodeIndex
            + 5 * nodeArrivalDistance + 6 * health + 7 * ((alive) ? 1 : 0));
    }
}
