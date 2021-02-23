package model.path;

import model.moveable.Move;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Saveable;

import java.util.*;

// A Path in a Maze; always starts with the PathNode (1,1, null)
public class Path implements Saveable {

    private List<PathNode> path;
    private PathNode tail;
    private Set<Position> visited;
    private int index;
    private LinkedList<List<Move>> branches; // declare as LinkedList so we can access subclass methods
    private List<Move> currentBranch;
    private boolean noDuplicatesNodes;

    // EFFECTS: start the Path with the Position (1,1) and set that as current node; allows dupes, has no branches, and
    //          has default index
    public Path() {
        path = new ArrayList<>();
        tail = new PathNode(1, 1, null);
        path.add(tail);
        visited = new HashSet<>();
        visited.add(tail);
        index = 0;
        branches = new LinkedList<>();
        currentBranch = new ArrayList<>();
        noDuplicatesNodes = false;
    }

    // EFFECTS: creates a Path with given pathNodes and visited; allows dupes, has no branches, and default index
    public Path(List<PathNode> path, Set<Position> visited) {
        this.path = path;
        tail = path.get(path.size() - 1);
        this.visited = visited;
        index = 0;
        branches = new LinkedList<>();
        currentBranch = new ArrayList<>();
        noDuplicatesNodes = false;

    }

    // EFFECTS: start the Path with the Position (1,1) and set that as current node, and use given flag to determine
    //          whether dupes are allowed
    public Path(boolean noDuplicateNodes) {
        this();
        this.noDuplicatesNodes = noDuplicateNodes;
    }

    // REQUIRES: posX and posY must be within bounds of Maze that this Path is associated with, the value of the cell
    //           with specified coordinates in the Maze must be PATH (not wall), and the cell must be adjacent to the
    //           previous cell in the path
    // MODIFIES: this
    // EFFECTS: add a node with given coordinates to the end of the path, dynamically determining direction attribute
    //          for the node and keeps track that we have seen this position. If given node is already in cell and this
    //          was initialized to allow no dupes, do nothing
    public void addNode(int posX, int posY) {
        if (noDuplicatesNodes && containsNode(new Position(posX, posY))) {
            return;
        }
        if (tail.getPosX() == posX) {
            tail = new PathNode(posX, posY, posY < tail.getPosY() ? Move.UP : Move.DOWN);
        } else {
            tail = new PathNode(posX, posY, posX < tail.getPosX() ? Move.LEFT : Move.RIGHT);
        }
        path.add(tail);
        visited.add(tail);
    }

    // REQUIRES: Cell with position obtained by moving from tail node in specified direction must be within bounds of
    //           Maze that this Path is associated with, and the corresponding cell in the associated Maze must be PATH
    // MODIFIES: this
    // EFFECTS: add the node obtained by moving from tail node in specified direction to end of path and keeps track
    //          that we have seen this position.  If given node is already in cell and this was initialized to allow no
    //          dupes, do nothing
    public void addNode(Move direction) {
        if (noDuplicatesNodes && containsNode(tail.applyMove(direction))) {
            return;
        }
        tail = tail.applyMove(direction);
        path.add(tail);
        visited.add(tail);
    }

    // REQUIRES: containsNode(x, y) should produce true
    // EFFECTS: produce node with specified coordinates
    public PathNode getNode(int x, int y) {
        return path.stream().filter(n -> n.equals(x, y)).toArray(PathNode[]::new)[0];
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

    // REQUIRES: If first call to next, then only call if addNode has been called at least once already. Else, only
    //           call if previous call to next produced true, or addNode has been called since prev call
    // MODIFIES: this
    // EFFECTS: produces true if there are more nodes in the path and advances to next node in the path
    public boolean next() {
        index++;
        return index + 1 < path.size();
    }

    // MODIFIES: this
    // EFFECTS: go back to first node in the path
    public void reset() {
        index = 0;
    }

    // REQUIRES: length - n must be >= 1
    // MODIFIES: this
    // EFFECTS: removes the last n nodes from the path and if current node was one of those n nodes, sets current node
    //          to tail of resulting path
    public void pop(int n) {
        for (int i = 0; i < n; i++) {
            path.remove(path.size() - 1);
            if (index > path.size() - 1) {
                index--;
            }
        }
        tail = path.get(path.size() - 1);
    }

    // REQUIRES: all moves must be valid, meaning that the Position produced by applying the move to the tail node is
    //           not a member of visited, and the corresponding cell in the Maze should be PATH
    // MODIFIES: this
    // EFFECTS: generates a branch for each possible Move from current branch, preserving order (the branch for the
    //          first element of moves comes before the other elements)
    public void generateBranches(List<Move> moves) {
        Collections.reverse(moves);
        List<Move> temp;
        for (Move move : moves) {
            temp = new ArrayList<>(currentBranch);
            temp.add(move);
            branches.addFirst(temp);
        }
    }

    // MODIFIES: this
    // EFFECTS: goes to next generated branch (most recently generated branches first)
    public void nextBranch() {
        pop(currentBranch.size());
        currentBranch = branches.pollFirst();
        if (currentBranch == null) {
            currentBranch = new ArrayList<>(0);
        }
        for (Move move : currentBranch) {
            addNode(move);
        }
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
        if (this.getLength() != path.getLength() || !tail.equals(path.getTail())) {
            return false;
        }

        Position pos;
        for (PathNode pathNode : this.path) {
            pos = path.getPosition();
            if (pos.getPosX() != pathNode.getPosX() || pos.getPosY() != pathNode.getPosY()) {
                return false;
            }
            path.next();
        }

        return true;
    }

    // EFFECTS: produce position of current node in the path
    public Position getPosition() {
        return path.get(index);
    }

    // EFFECTS: produce direction travelled from previous node to get to current one
    public Move getDirection() {
        return path.get(index).getDirection();
    }

    // EFFECTS: produces the position of the last node in the path
    public Position getTail() {
        return tail;
    }

    // EFFECTS: produce length of this
    public int getLength() {
        return path.size();
    }

    // EFFECTS: produces a JSON representation of this
    @Override
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        JSONArray arr = new JSONArray();
        for (PathNode p : path) {
            arr.put(p.toJson());
        }
        obj.put("path", arr);

        arr = new JSONArray();
        for (Position p : visited) {
            arr.put(p.toJson());
        }
        obj.put("visited", arr);

        return obj;
    }

}
