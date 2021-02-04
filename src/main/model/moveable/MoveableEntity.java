package model.moveable;

import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.path.Position;

// An entity that can move within a maze
public abstract class MoveableEntity {

    protected ImmutableMaze maze;
    protected Position position;

    // REQUIRES: the value of the cell at initialPos in maze must be PATH
    // EFFECTS: sets the Maze that this entity moves within, and the initial position of the entity within the maze
    public MoveableEntity(ImmutableMaze maze, Position initialPos) {
        this.maze = maze;
        position = initialPos;
    }

    // EFFECTS: produce true if applying given move does not run this entity into a wall
    protected boolean isValid(Move move) {
        return maze.getCell(position.applyMove(move)) == Maze.PATH;
    }

    // MODIFIES: this
    // EFFECTS: if moving in specified manner does not run into a wall, apply the move
    public abstract void tryMove(Move move);

    // EFFECTS: produce current position of this entity
    public Position getPosition() {
        return position;
    }
}
