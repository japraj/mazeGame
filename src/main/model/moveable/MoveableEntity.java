package model.moveable;

import model.maze.ImmutableMaze;
import model.path.Position;

// An entity that can move within a maze
public abstract class MoveableEntity {

    private ImmutableMaze maze;
    private Position position;

    // REQUIRES: the value of the cell at initialPos in maze must be PATH
    // EFFECTS: sets the Maze that this entity moves within, and the initial position of the entity within the maze
    public MoveableEntity(ImmutableMaze maze, Position initialPos) {

    }

    // MODIFIES: this
    // EFFECTS: if moving in specified manner does not run into a wall, apply the move
    public void tryMove(Move move) {

    }
}
