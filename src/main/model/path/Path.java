package model.path;

import model.moveable.Move;

import java.util.List;

// A Path in a Maze; always starts with the PathNode (1,1, null)
public class Path {

    private List<PathNode> path;
    private int index;

    // EFFECTS: start the Path with the Position (1,1) and set that as current node
    public Path() {
    }

    // REQUIRES: node must be within bounds of Maze that this Path is associated with, and the value of the cell with
    // specified coordinates in the Maze should be PATH (not wall)
    // MODIFIES: this
    // EFFECTS: add given node to the end of the path
    public void addNode(PathNode nextNode) {

    }

    // MODIFIES: this
    // EFFECTS: produces true if there are more nodes in the path and advances to next node in the path
    public boolean next() {
        return false;
    }

    // EFFECTS: produce position of current node in the path
    public Position getPosition() {
        return null;
    }

    // EFFECTS: produce direction travelled from previous node to get to current one
    public Move getDirection() {
        return null;
    }

    // MODIFIES: this
    // EFFECTS: go back to first node in the path
    public void reset() {

    }

}
