package model.solver.backtracker;

import model.moveable.Move;
import model.path.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BranchedPath extends Path {

    private LinkedList<List<Move>> branches;
    private List<Move> currentBranch;

    public BranchedPath() {
        branches = new LinkedList<>();
        currentBranch = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: removes the last n nodes from the path and if current node was one of those n nodes, sets current node
    //          to tail of resulting path
    public void pop(int n) {
        assert path.size() - n > 0;

        for (int i = 0; i < n; i++) path.pop();
    }

    // MODIFIES: this
    // EFFECTS: generates a branch for each possible Move from current branch
    public void generateBranches(List<Move> moves) {
        Iterator<Move> moveIterator = moves.iterator();
        Move m;
        while (moveIterator.hasNext()) {
            m = moveIterator.next();
            if (path.contains(path.peek().applyMove(m)))
                moveIterator.remove();
        }

        List<Move> temp;
        for (Move move : moves) {
            temp = new ArrayList<>(currentBranch);
            temp.add(move);
            branches.add(temp);
        }
    }

    // MODIFIES: this
    // EFFECTS: goes to next generated branch (most recently generated branches first)
    public void nextBranch() {
        pop(currentBranch.size());
        currentBranch = branches.pollLast();

        if (currentBranch == null) currentBranch = new ArrayList<>(0);

        for (Move move : currentBranch) addNode(move);
    }
}
