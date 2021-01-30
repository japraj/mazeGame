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
    }

    // MODIFIES: this
    // EFFECTS: if moving in specified manner does not run into a wall, apply the move AND add it to current path
    @Override
    public void tryMove(Move move) {
        super.tryMove(move);
    }
}
