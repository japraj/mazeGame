package model.solver.backtracker;

import model.moveable.Move;
import model.path.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BranchedPath extends Path {

    private LinkedList<List<Move>> branches; // declare as LinkedList so we can access subclass methods
    private List<Move> currentBranch;

    public BranchedPath() {
        branches = new LinkedList<>();
        currentBranch = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: removes the last n nodes from the path and if current node was one of those n nodes, sets current node
    //          to tail of resulting path; throws IllegalArgumentException if length - n <= 0
    public void pop(int n) throws IllegalArgumentException {
        assert path.size() - n > 0;

        for (int i = 0; i < n; i++) path.pop();
    }

    // MODIFIES: this, moves
    // EFFECTS: generates a branch for each possible Move from current branch, preserving order (the branch for the
    //          first element of moves comes before the other elements); reverses moves, throws IllegalArgumentException
    //          if any moves in list are invalid (applying the move to the tail node would produce a member of visited)
    public void generateBranches(List<Move> moves) throws IllegalArgumentException {
        for (Move m : moves)
            if (visited.contains(path.peek().applyMove(m)))
                throw new IllegalArgumentException();

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

        if (currentBranch == null)
            currentBranch = new ArrayList<>(0);

        for (Move move : currentBranch)
            addNode(move);
    }
}
