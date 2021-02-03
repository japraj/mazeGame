package model.path;

import model.moveable.Move;

// A position with x, y in a maze
public class Position {

    private int posX;
    private int posY;

    // REQUIRES: x, y must be non-negative and less than size, where size is the side-length of the maze that this
    //           position is associated with
    // EFFECTS: initialize this position as (x, y)
    public Position(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    // EFFECTS: produce x coordinate of this position
    public int getPosX() {
        return posX;
    }

    // EFFECTS: produce y coordinate of this position
    public int getPosY() {
        return posY;
    }

    // EFFECTS: increment/decrement posX or posY depending on move (produces a new position - does not modify this)
    public Position applyMove(Move move) {
        switch (move) {
            case UP:
                return new Position(posX, posY - 1);
            case DOWN:
                return new Position(posX, posY + 1);
            case LEFT:
                return new Position(posX - 1, posY);
            case RIGHT:
                return new Position(posX + 1, posY);
            default:
                return new Position(posX, posY);
        }
    }

    // EFFECTS: produce true if given object is Position and o and this have same posX and posY
    @Override
    public boolean equals(Object o) {
        return o instanceof Position && this.posX == ((Position) o).getPosX() && this.posY == ((Position) o).getPosY();
    }

    // EFFECTS: produce a unique hashcode for this
    @Override
    public int hashCode() {
        return Integer.parseInt(posX + "" + posY);
    }

}
