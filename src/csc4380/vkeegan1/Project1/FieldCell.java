package csc4380.vkeegan1.Project1;

public class FieldCell {
    private int adjacentBombs;
    private boolean isFlagged;
    private boolean hasBomb;

    public FieldCell() {
        adjacentBombs = 0;
        isFlagged = false;
        hasBomb = false;
    }

    /** Getters */
    public int getAdjacentBombs() { return adjacentBombs; }
    public boolean isFlagged() { return isFlagged; }
    public boolean hasBomb() { return hasBomb; }

    /** Plant a bomb in the cell. Return true if successful. */
    public boolean plantBomb() {
        if (hasBomb) {
            return false;
        } else {
            hasBomb = true;
            return true;
        }
    }

    public void addAdjacentBomb() {
        adjacentBombs++;
    }

    public void toggleFlag() {
        isFlagged = !isFlagged;
    }
}
