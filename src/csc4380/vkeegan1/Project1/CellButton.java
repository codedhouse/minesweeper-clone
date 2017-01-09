package csc4380.vkeegan1.Project1;

import javax.swing.*;
import java.awt.*;

public class CellButton extends JButton {
    private int locationRow;
    private int locationColumn;
    private boolean isCleared;
    private boolean isFlagged;

    public CellButton(int row, int col) {
        super();
        locationRow = row;
        locationColumn = col;
        isCleared = false;
        isFlagged = false;
    }

    // Getters
    public int getRow() { return locationRow; }
    public int getColumn() { return locationColumn; }
    public boolean isCleared() { return isCleared; }
    public boolean isFlagged() { return isFlagged; }

    // Setters
    public void setText(int adjacentBombs) {
        this.setFont(new Font("Verdana", Font.BOLD, 25));

        switch (adjacentBombs) {
            case 1: this.setForeground(Color.BLUE); break;
            case 2: this.setForeground(Color.GREEN); break;
            case 3: this.setForeground(Color.RED); break;
            case 4: this.setForeground(new Color(0,0,128)); break;
            case 5: this.setForeground(new Color(138,51,36)); break;
            case 6: this.setForeground(Color.CYAN); break;
            case 7: this.setForeground(Color.BLACK); break;
            case 8: this.setForeground(Color.GRAY); break;
            default: this.setText(""); return;
        }

        this.setText(String.valueOf(adjacentBombs));
    }

    public void clear() {
        isCleared = true;
    }

    public void toggleFlag() {
        isFlagged = !isFlagged;
    }
}
