package com.vivienkeegan.minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Presenter {
    private Model model;
    private View view;
    private int gridHeight;
    private int gridWidth;
    private int minesToClear;
    private boolean gameStarted;

    public Presenter(Model m, View v) {
        model = m;
        view = v;
    }

    public void start() {
        gridHeight = model.getNumRows();
        gridWidth = model.getNumColumns();
        int bombs = model.getBombsLeft();
        minesToClear = (gridHeight*gridWidth) - bombs;
        gameStarted = false;

        view.init(gridHeight, gridWidth, bombs);
        view.initTimer(new TimerListener());

        for (int row=0; row<gridHeight; row++) {
            for (int col=0; col<gridWidth; col++) {
                view.addCellMouseListener(row, col, new CellListener());
                view.addCellActionListener(row, col, new CellActionListener());
            }
        }

        view.addNewMenuItemListener(new NewGameActionListener());
        view.addNewBtnListener(new NewGameActionListener());

        view.center();
        view.show();
    }

    private void startNewGame() {
        model = new Model();
        gridHeight = model.getNumRows();
        gridWidth = model.getNumColumns();
        int bombs = model.getBombsLeft();
        minesToClear = (gridHeight*gridWidth) - bombs;
        gameStarted = false;
        view.setBombs(bombs);

        view.addNewBtnListener(new NewGameActionListener());

        for (int row=0; row<gridHeight; row++) {
            for (int col=0; col<gridWidth; col++) {
                view.addCellMouseListener(row, col, new CellListener());
                view.addCellActionListener(row, col, new CellActionListener());
            }
        }
    }

    private void revealAdjacentCells(Object cell) {
        int row = view.getFieldRow(cell);
        int col = view.getFieldColumn(cell);

        if (row > 0) {
            if (col > 0) {
                view.clickCell(row - 1, col - 1);
            }
            if (col < gridWidth-1) {
                view.clickCell(row-1, col+1);
             }
            view.clickCell(row-1, col);
        }

        if (col > 0) {
            view.clickCell(row, col-1);
        }

        if (col < gridWidth-1) {
            view.clickCell(row, col+1);
        }

        if (row < gridHeight-1) {
            if (col > 0) {
                view.clickCell(row+1, col-1);
            }
            if (col < gridWidth-1) {
                view.clickCell(row+1, col+1);
           }
            view.clickCell(row+1, col);
        }
    }

    private void check() {
        if (minesToClear == 0) {
            view.winGame();
            startNewGame();
        }
    }

    /** Listeners */
    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            model.addSecondElapsed();
            view.updateTimer(model.getSecondsElapsed());
        }
    }

    private class CellListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            if (!gameStarted) {
                view.startGame();
                gameStarted = true;
            }
            Object source = e.getSource();
            int row = view.getFieldRow(source);
            int col = view.getFieldColumn(source);

            if (SwingUtilities.isRightMouseButton(e)) {
                view.toggleFlag(source);
                model.toggleCellFlag(row, col);
            } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                FieldCell cell = model.getCell(row, col);
                if (cell.isFlagged()) { return; }
                try {
                    if (view.revealCell(source, cell.getAdjacentBombs(), cell.hasBomb())) {
                        if (cell.hasBomb()) {
                            view.loseGame();
                            startNewGame();
                            return;
                        }
                        minesToClear--;
                    }
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }

                if (cell.getAdjacentBombs() == 0) {
                    revealAdjacentCells(source);
                }
            } else if (SwingUtilities.isLeftMouseButton(e) &&
                e.getClickCount() == 2 &&
                view.isCleared(e.getSource())) {
                revealAdjacentCells(source);
            }
            check();
        }

        public void mouseEntered(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
    }

    private class CellActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if (!gameStarted) {
                view.startGame();
                gameStarted = true;
            }
            Object source = ae.getSource();
            int row = view.getFieldRow(source);
            int col = view.getFieldColumn(source);

            FieldCell cell = model.getCell(row, col);

            try {
                if (view.revealCell(source, cell.getAdjacentBombs(), cell.hasBomb())) {
                    if (cell.hasBomb()) { view.loseGame(); startNewGame(); return; }
                    minesToClear--;
                }
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }

            if (cell.getAdjacentBombs() == 0) {
                revealAdjacentCells(source);
            }
        }
    }

    private class NewGameActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.stopTimer();
            view.startNewGame();
            startNewGame();
        }
    }
}
