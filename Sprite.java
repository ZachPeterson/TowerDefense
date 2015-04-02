import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * Class to represent a draw sprite.
 * Sprites can be animated using cell animation, rotated and translated.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class Sprite {
    private BufferedImage myImage;
    private int x;
    private int y;
    private int xCells;
    private int yCells;
    private int fps;
    private int currentXCell;
    private int currentYCell;
    private double updateTime;
    private double timePerFrame;
    private double rotation;

    /**
     * Constructs a new Sprite with a given image, cells in the x direction,
     * cells in the y direction and frames per second.
     *
     * @param myImage The spritesheet to associate with this sprite.
     * @param cellsInXDirection How many cells there are in the x direction.
     * @param cellsInYDirection How many cells there are in the y direction.
     * @param framesPerSecond The frame rate for this sprite.
     */
    public Sprite(BufferedImage myImage, int cellsInXDirection,
        int cellsInYDirection, int framesPerSecond) {
        this.myImage = myImage;
        this.x = 0;
        this.y = 0;
        this.xCells = cellsInXDirection;
        this.yCells = cellsInYDirection;
        this.fps = framesPerSecond;
        this.currentXCell = 0;
        this.currentYCell = 0;
        this.updateTime = 0.0;
        this.timePerFrame = 1.0 / framesPerSecond;
        this.rotation = 0.0;
    }

    /**
     * Updates the frame region for this sprite based on a given time
     * differential.
     *
     * @param dt The time differential to use for this frame.
     */
    public void update(double dt) {
        updateTime += dt;

        if (updateTime >= timePerFrame) {
            currentXCell++;
            updateTime = 0.0;

            if (currentXCell >= xCells) {
                currentXCell = 0;
                currentYCell++;

                if (currentYCell >= yCells) {
                    currentYCell = 0;
                }
            }
        }
    }

    /**
     * Sets the position for this sprite.
     *
     * @param newX The new x position for this sprite.
     * @param newY The new y position for this sprite.
     */
    public void setPosition(int newX, int newY) {
        x = newX;
        y = newY;
    }

    /**
     * Sets the rotation for this sprite.
     *
     * @param newRotation The new rotation for this sprite.
     */
    public void setRotation(double newRotation) {
        rotation = newRotation;
    }

    /**
     * Gets the image associated with this sprite for drawing position
     * calculations and collision tests.
     *
     * @return The images associated with this sprite, takes rotation and
     * current animation frame into account.
     */
    public BufferedImage getImage() {
        BufferedImage tempImage
            = myImage.getSubimage(currentXCell * (myImage.getWidth() / xCells),
                                  currentYCell * (myImage.getHeight() / yCells),
                                  myImage.getWidth() / xCells,
                                  myImage.getHeight() / yCells);
        AffineTransform tx = AffineTransform.getRotateInstance(
                rotation, tempImage.getWidth() / 2, tempImage.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx,
                AffineTransformOp.TYPE_BILINEAR);
        return op.filter(tempImage, null);
    }

    /**
     * Draws this sprite.
     *
     * @param g The Graphics to draw with.
     */
    public void render(Graphics g) {
        g.drawImage(getImage(), x, y, null);
    }
}
