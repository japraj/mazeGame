package model.path;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

// This class handles the logic for path painting; in order to minimize the number of paint calls to Graphics context
// objects (in particular, this class determines what cells need to be wiped & which need to be painted by computing
// a diff between what has already been placed and what needs to be placed)
public class PathEngine {

    private List<PathNode> placed;
    private List<PathNode> toWipe;
    private List<PathNode> toPlace;

    // EFFECTS: initializes this with 0 paths currently placed
    public PathEngine() {
        placed = new Stack<>();
    }

    // MODIFIES: this
    // EFFECTS: finds the deepest common node in current path and the argument, and computes the minimum number of
    //          cells that need to be placed and wiped (or if current path is empty, just sets it to b the current path)
    //          when possible, always wipes/replaces the tail because direction may have changed
    public void addPath(Path path) {
        // if this is the first path, then we init vars because there is no need to calculate diff between an empty list
        // and the given path
        if (placed.size() == 0) {
            toWipe = new LinkedList<>();
            toPlace = path.getNodes();
        } else {
            toWipe = Path.subtract(placed, path.getNodes(), false);
            toPlace = path.subtract(placed, true);
            placed.removeAll(toWipe);
        }
        placed.addAll(toPlace);
    }

    // EFFECTS: produces a list of all cells that are currently placed
    public List<PathNode> getPlaced() {
        return placed;
    }

    // EFFECTS: produces a list of cells that need to be wiped
    public List<PathNode> getToWipe() {
        return toWipe;
    }

    // EFFECTS: produces a list of cells that ned to be painted
    public List<PathNode> getToPlace() {
        return toPlace;
    }
}
