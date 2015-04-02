import java.util.Vector;

/**
 * Class to store all of the current PathNodes for enemy navigation.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class PathNodeList {
    private Vector<PathNode> nodeList;
    private static PathNodeList instance = new PathNodeList();

    /**
     * Constructs a new PathNodeList.
     */
    private PathNodeList() {
    }

    /**
     * Gets the static instance of the PathNodeList.
     *
     * @return The static instance of the PathNodeList.
     */
    public static PathNodeList getInstance() {
        return instance;
    }

    /**
     * Sets the list data for the PathNodeList.
     *
     * @param newNodeList The new node list to use. Passed in during
     * changeLevel().
     */
    public void setNodeList(Vector<PathNode> newNodeList) {
        nodeList = newNodeList;
    }

    /**
     * Gets a PathNode at a given index.
     *
     * @param index The index to get a PathNode from.
     * @return PathNode at a given index.
     */
    public PathNode getNode(int index) {
        if (index < nodeList.size()) {
            return nodeList.get(index);
        } else {
            return null;
        }
    }
}
