package com.vivienkeegan.minesweeper;

public class Model {
    private Minefield minefield;
    private int numRows;
    private int numColumns;
    private int numBombs;
    private int bombsLeft;
    private int secondsElapsed;

    public Model() {
        numRows = 9;
        numColumns = 9;
        minefield = new Minefield(numRows, numColumns);
        numBombs = 10;
        bombsLeft = numBombs;
        secondsElapsed = 0;

        minefield.plantBombs(numBombs);
    }

    // Getters
    public int getNumColumns() { return numColumns; }
    public int getNumRows() { return numRows; }
    public int getBombsLeft() { return bombsLeft; }
    public int getSecondsElapsed() { return secondsElapsed; }
    public FieldCell getCell(int row, int col) { return minefield.getCell(row, col); }

    // Setters
    public void addSecondElapsed() {
        secondsElapsed++;
    }
    public void toggleCellFlag(int row, int col) { minefield.toggleCellFlag(row, col); }

}
