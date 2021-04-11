package model.moveable;

import model.maze.ImmutableMaze;
import model.path.Position;

// An entity that can move within a maze and keeps track of its path
public class Player extends MoveableEntity {

    // EFFECTS: initialize the player at position (1,1)
    public Player(ImmutableMaze maze) {
        super(maze, new Position(1, 1));
    }

    // EFFECTS: initialize the player with given path and position
    public Player(ImmutableMaze maze, Position pos) {
        super(maze, pos);
    }

}
