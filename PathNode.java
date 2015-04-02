/**
 * Class to represent an enemy pathnode.
 * Simply stores its own x and y position.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class PathNode {
    private int x, y;

    /**
     * Constructs a new PathNode with given x and y coordinates.
     *
     * @param x The x coordinate to construct a new PathNode at.
     * @param y The y coordinate to construct a new PathNode at.
     */
    public PathNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of this pathnode.
     *
     * @return The x coordinate of this pathnode.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this pathnode.
     *
     * @return The y coordinate of this pathnode.
     */
    public int getY() {
        return y;
    }
}

