package model.path;

import model.moveable.Move;

import java.util.*;

// A Path in a Maze; always starts with the PathNode (1,1, null)
public class Path implements Iterable<PathNode> {

    protected Stack<PathNode> path;
    protected Set<Position> visited;
    protected boolean noDuplicatesNodes;

    // EFFECTS: start the Path with the Position (1,1) and set that as current node; allows dupes, has no branches, and
    //          has default index
    public Path() {
        path = new Stack<>();
        visited = new HashSet<>();

        PathNode origin = new PathNode(1, 1, null);
        path.add(origin);
        visited.add(origin);

        noDuplicatesNodes = false;
    }

    // EFFECTS: start the Path with the Position (1,1) and set that as current node, and use given flag to determine
    //          whether dupes are allowed
    public Path(boolean noDuplicateNodes) {
        this();
        this.noDuplicatesNodes = noDuplicateNodes;
    }

    // MODIFIES: this
    // EFFECTS: If node (x, y) is already in Path and this was initialized to prohibit duplicates, do nothing. Else add
    //          node (x, y) to the end of the path, dynamically determining direction attribute for the node, and
    //          recording position as visited
    public void addNode(int posX, int posY) {
        if (noDuplicatesNodes && containsNode(new Position(posX, posY)))
            return;

        PathNode tail = path.peek();
        tail = tail.getPosX() == posX ? new PathNode(posX, posY, posY < tail.getPosY() ? Move.UP : Move.DOWN)
                                      : new PathNode(posX, posY, posX < tail.getPosX() ? Move.LEFT : Move.RIGHT);

        path.add(tail);
        visited.add(tail);
    }

    // MODIFIES: this
    // EFFECTS: If given node obtained by applying given move to tail is already in path and this was initialized to
    //          allow no dupes, do nothing. Else, add the node obtained by moving from tail node in specified direction
    //          to end of path, record resulting position as visited, and reset iteration index.
    public void addNode(Move direction) {
        if (noDuplicatesNodes && containsNode(path.peek().applyMove(direction)))
            return;

        PathNode tail = path.peek().applyMove(direction);
        path.add(tail);
        visited.add(tail);
    }

    // EFFECTS: produce node with specified coordinates; throws IllegalArgumentException if !contains(x, y)
    public PathNode getNode(int x, int y) throws IllegalArgumentException {
        for (PathNode p : path)
            if (p.equals(x, y)) return p;

        throw new IllegalArgumentException();
    }

    // EFFECTS: produce true if this node is in current path
    public boolean containsNode(Position pos) {
        return path.contains(pos);
    }

    // EFFECTS: produce true if this node is in current path
    public boolean containsNode(int x, int y) {
        return containsNode(new Position(x, y));
    }

    // MODIFIES: this
    // EFFECTS: records that pos has been visited
    public void addVisited(Position pos) {
        visited.add(pos);
    }

    // EFFECTS: produce true if this node has been seen in this path before (even if it is not present in the current
    //          path)
    public boolean visitedNode(Position pos) {
        return visited.contains(pos);
    }

    // EFFECTS: produces a list of nodes that are in 'a' but not in 'b', starting from the end of 'a' until a node that
    //          is present in both (with same position and direction) is found; includes the common node iff
    //          includeCommon is true
    public static List<PathNode> subtract(List<PathNode> a, List<PathNode> b, boolean includeCommon) {
        List<PathNode> difference = new LinkedList<>();
        // iterate over p's last n nodes til we find a common node or run out of nodes for one of the elements; we will
        // always find a node because at the very least, both this and p share the common node (1, 1, null)
        PathNode thisNode;
        for (int i = 1; i < a.size(); i++) {
            thisNode = a.get(a.size() - i);
            if (b.contains(thisNode) && b.get(b.indexOf(thisNode)).getDirection() == thisNode.getDirection()) {
                if (includeCommon) {
                    difference.add(thisNode);
                }
                return difference;
            } else {
                difference.add(thisNode);
            }
        }
        return difference;
    }

    // EFFECTS: produces a list of nodes that are in this but not in p, starting from the end of this until a node that
    //          is present in both (with same position and direction) is found; includes the common node iff
    //          includeCommon is true
    public List<PathNode> subtract(List<PathNode> p, boolean includeCommon) {
        return subtract(path, p, includeCommon);
    }

    // EFFECTS: produces the list of pathNodes
    public List<PathNode> getNodes() {
        return path;
    }

    // EFFECTS: produces true if object is a Path and all nodes in this are present in given path, in same order
    @Override
    public boolean equals(Object o) {
        // if o is not of type Path, these objects are not equal
        if (!(o instanceof Path)) {
            return false;
        }

        // guard clause - if they are of different lengths or end in different places, then they cannot be equal
        Path path = (Path) o;
        if (this.getLength() != path.getLength() || !this.path.peek().equals(path.getTail())) {
            return false;
        }

        Position pos;
        Iterator<PathNode> pathIter = path.iterator();
        for (PathNode pathNode : this.path) {
            pos = pathIter.next();
            if (pos.getPosX() != pathNode.getPosX() || pos.getPosY() != pathNode.getPosY()) {
                return false;
            }
        }

        return true;
    }

    // EFFECTS: produces the position of the last node in the path
    public PathNode getTail() {
        return path.peek();
    }

    // EFFECTS: produce length of this
    public int getLength() {
        return path.size();
    }

    // EFFECTS: returns an iterator for this
    @Override
    public Iterator<PathNode> iterator() {
        return path.iterator();
    }

}
