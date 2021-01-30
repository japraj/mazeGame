package model.path;

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

    // REQUIRES: newX must be non-negative and less than size, where size is the side-length of the maze that this
    //           position is associated with
    // MODIFIES: this
    // EFFECTS: update x coordinate
    public void setPosX(int newX) {
        this.posX = newX;
    }

    // REQUIRES: newY must be non-negative and less than size, where size is the side-length of the maze that this
    //           position is associated with
    // MODIFIES: this
    // EFFECTS: update y coordinate
    public void setPosY(int newY) {
        this.posY = newY;
    }

    // REQUIRES: newX, newY must be non-negative and less than size, where size is the side-length of the maze that this
    //           position is associated with
    // MODIFIES: this
    // EFFECTS: set this position to (newX, newY)
    public void setPosition(int newX, int newY) {
        setPosX(newX);
        setPosY(newY);
    }

}
