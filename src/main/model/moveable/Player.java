package model.moveable;

import model.maze.ImmutableMaze;
import model.path.Path;
import model.path.Position;

// An entity that can move within a maze and keeps track of its path
public class Player extends MoveableEntity {

    private Path path;

    // EFFECTS: initialize the player at position (1,1)
    public Player(ImmutableMaze maze) {
        super(maze, new Position(1, 1));
        path = new Path();
    }

    // MODIFIES: this
    // EFFECTS: if moving in specified manner does not run into a wall, apply the move AND add it to current path
    @Override
    public void tryMove(Move move) {
        if (isValid(move)) {
            position = position.applyMove(move);
            path.addNode(move);
        }
    }

    // EFFECTS: produce path player has taken so far
    public Path getPath() {
        return path;
    }
}
