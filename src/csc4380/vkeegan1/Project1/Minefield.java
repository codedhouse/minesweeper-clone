package csc4380.vkeegan1.Project1;

import java.util.Random;

public class Minefield {
    private FieldCell[][] minefield;

    public Minefield(int rows, int columns) {
        minefield = new FieldCell[rows][columns];

        for (int row = 0; row < minefield.length; row++) {
            for (int col = 0; col < minefield[row].length; col++) {
                minefield[row][col] = new FieldCell();
            }
        }
    }

    // Getters
    public FieldCell getCell(int row, int col) { return minefield[row][col]; }

    public void toggleCellFlag(int row, int col) {
        minefield[row][col].toggleFlag();
    }

    public void plantBombs(int bombs) {
        Random random = new Random();

        while (bombs > 0) {
            int row = random.nextInt(minefield.length);
            int col = random.nextInt(minefield[0].length);

            if (minefield[row][col].plantBomb()) {
                markAdjacentCells(row, col);
                bombs--;
            }
        }
    }

    private void markAdjacentCells(int row, int col) {
        if (row > 0) {
            if (col > 0) {
                minefield[row-1][col-1].addAdjacentBomb();
            }
            if (col < minefield[0].length-1) {
                minefield[row-1][col+1].addAdjacentBomb();
            }
            minefield[row-1][col].addAdjacentBomb();
        }

        if (col > 0) {
            minefield[row][col-1].addAdjacentBomb();
        }

        if (col < minefield[0].length-1) {
            minefield[row][col+1].addAdjacentBomb();
        }

        if (row < minefield.length-1) {
            if (col > 0) {
                minefield[row+1][col-1].addAdjacentBomb();
            }
            if (col < minefield[0].length-1) {
                minefield[row+1][col+1].addAdjacentBomb();
            }
            minefield[row+1][col].addAdjacentBomb();
        }
    }
}
