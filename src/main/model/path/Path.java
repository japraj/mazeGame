package model.path;

import model.moveable.Move;

import java.util.*;

// A Path in a Maze; always starts with the PathNode (1,1, null)
public class Path {

    private List<PathNode> path;
    private PathNode tail;
    private int index;

    // EFFECTS: start the Path with the Position (1,1) and set that as current node
    public Path() {
        path = new ArrayList<>();
        tail = new PathNode(1, 1, null);
        path.add(tail);
        index = 0;
    }

    // REQUIRES: posX and posY must be within bounds of Maze that this Path is associated with, the value of the cell
    //           with specified coordinates in the Maze must be PATH (not wall), and the cell must be adjacent to the
    //           previous cell in the path (cannot be equal).
    // MODIFIES: this
    // EFFECTS: add a node with given coordinates to the end of the path, dynamically determining direction attribute
    //          for the node
    public void addNode(int posX, int posY) {
        if (tail.getPosX() == posX) {
            tail = new PathNode(posX, posY, posY < tail.getPosY() ? Move.UP : Move.DOWN);
        } else {
            tail = new PathNode(posX, posY, posX < tail.getPosX() ? Move.LEFT : Move.RIGHT);
        }
        path.add(tail);
    }

    // REQUIRES: If first call to next, then only call if addNode has been called at least once already. Else, only
    //           call if previous call to next produced true, or addNode has been called since prev call
    // MODIFIES: this
    // EFFECTS: produces true if there are more nodes in the path and advances to next node in the path
    public boolean next() {
        index++;
        return index + 1 < path.size();
    }

    // EFFECTS: produce position of current node in the path
    public Position getPosition() {
        return path.get(index);
    }

    // EFFECTS: produce direction travelled from previous node to get to current one
    public Move getDirection() {
        return path.get(index).getDirection();
    }

    // MODIFIES: this
    // EFFECTS: go back to first node in the path
    public void reset() {
        index = 0;
    }

    // EFFECTS: produces the position of the last node in the path
    public Position getTail() {
        return tail;
    }

    // EFFECTS: produce length of this
    public int length() {
        return path.size();
    }

    // EFFECTS: produces true if all nodes in this are present in given path, in same order
    public boolean equals(Path path) {
        // guard clause - if they are of different lengths, then they cannot be equal
        if (this.length() != path.length()) {
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

}
