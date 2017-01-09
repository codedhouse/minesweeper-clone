package com.vivienkeegan.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;

public class View {
    private Timer timer;
    private int fieldWidth;
    private int fieldHeight;
    private int bombs;

    /** Main GUI components */
    private JFrame window;
    private JMenuItem newGameMenuItem;
    private JMenuItem exitMenuItem;
    private JLabel bombsLeftDisplay;
    private JButton newGameBtn;
    private JLabel timerDisplay;
    private CellButton[][] buttonGrid;
    private JPanel fieldPanel;
    private JPanel progressPanel;
    private ImageIcon bombIcon;
    private ImageIcon flagIcon;

    /** Constants */
    private static final String WINDOW_TITLE = "Project 1, vkeegan1 (Vivien Keegan)";
    private static final String GAME_MENU = "Game";
    private static final String NEW_GAME = "New";
    private static final String EXIT = "Quit";
    private static final String MINES_LEFT = "Bombs:";
    private static final String TIMER = "Time(s):";
    private static final String INITIAL_TIME = "0";

    public View() {
        window = new JFrame(WINDOW_TITLE);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.getContentPane().setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu(GAME_MENU);
        newGameMenuItem = new JMenuItem(NEW_GAME);
        exitMenuItem = new JMenuItem(EXIT);
        gameMenu.add(newGameMenuItem);
        gameMenu.add(exitMenuItem);
        menuBar.add(gameMenu);
        window.setJMenuBar(menuBar);

        // Icons
        ImageIcon bomb = new ImageIcon(getClass().getResource("img/bomb.png"));
        Image img = bomb.getImage();
        Image smallBomb = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        bombIcon = new ImageIcon(smallBomb);

        ImageIcon flag = new ImageIcon(getClass().getResource("img/flag.png"));
        Image flagImg = flag.getImage();
        Image smallFlag = flagImg.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        flagIcon = new ImageIcon(smallFlag);
    }

    public void init(int height, int width, int numBombs) {
        fieldHeight = height;
        fieldWidth = width;
        bombs = numBombs;

        // Top Panel
        progressPanel = new JPanel();
        progressPanel.add(new JLabel(MINES_LEFT));
        bombsLeftDisplay = new JLabel(String.valueOf(bombs));
        bombsLeftDisplay.setForeground(Color.red);
        progressPanel.add(bombsLeftDisplay);
        newGameBtn = new JButton(NEW_GAME);
        progressPanel.add(newGameBtn);
        progressPanel.add(new JLabel(TIMER));
        timerDisplay = new JLabel(INITIAL_TIME);
        timerDisplay.setForeground(Color.red);
        progressPanel.add(timerDisplay);
        window.add(progressPanel);

        // Button grid
        buttonGrid = new CellButton[fieldHeight][fieldWidth];
        for (int row = 0; row < fieldHeight; row++) {
            for (int col = 0; col < fieldWidth; col++) {
                CellButton button = new CellButton(row, col);
                button.setMargin(new Insets(0,0,0,0));
                button.setPreferredSize(new Dimension(50,50));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                buttonGrid[row][col] = button;
            }
        }

        // Minefield panel
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(fieldHeight, fieldWidth, 0, 0));
        for (int row = 0; row < fieldHeight; row++) {
            for (int col = 0; col < fieldWidth; col++) {
                fieldPanel.add(buttonGrid[row][col]);
            }
        }
        window.add(fieldPanel);
        window.setResizable(true);
    }

    public void startNewGame() {
        window.remove(progressPanel);
        window.remove(fieldPanel);
        init(fieldHeight, fieldWidth, bombs);
        window.pack();
    }

    // Getters
    public int getFieldRow(Object source) {
        return ((CellButton) source).getRow();
    }

    public int getFieldColumn(Object source) {
        return ((CellButton) source).getColumn();
    }

    // Setters
    public void initTimer(ActionListener listener) {
        timer = new Timer(1000, listener);
    }

    public void startGame() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void setBombs(int count) {
        bombs = count;
        bombsLeftDisplay.setText(String.valueOf(count));
    }

    public void toggleFlag(Object source) {
        CellButton btn = (CellButton) source;
        if (!btn.isCleared()) { btn.toggleFlag(); }
        if (btn.isFlagged()) {
            bombs--;
            btn.setIcon(flagIcon);
        } else {
            bombs++;
            btn.setIcon(null);
        }
        bombsLeftDisplay.setText(String.valueOf(bombs));
    }

    public boolean revealCell (Object source, int adjacentBombs, boolean hasBomb)
            throws IOException
    {
        CellButton btn = (CellButton) source;
        if (btn.isCleared() || btn.isFlagged()) return false;

        btn.setBorder(BorderFactory.createLoweredBevelBorder());
        if (!hasBomb) {
            btn.setText(adjacentBombs);
        }

        else {
            btn.setOpaque(false);
            btn.setIcon(bombIcon);
        }

        btn.clear();
        return true;
    }

    public boolean isCleared(Object source) {
        return ((CellButton) source).isCleared();
    }

    public void clickCell(int row, int col) {
        CellButton btn = buttonGrid[row][col];

        if (!btn.isCleared() && !btn.isFlagged()) { buttonGrid[row][col].doClick(); }
    }

    /** Center the window */
    public void center() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screen.getWidth() - window.getWidth()) / 2);
        int y = (int) ((screen.getHeight() - window.getHeight()) / 2);
        window.setLocation(x, y);
    }

    /** Modifiers */
    public void updateTimer(int secs) {
        timerDisplay.setText(String.valueOf(secs));
    }

    public void loseGame() {
        JOptionPane.showMessageDialog(window, "You lost.");
        stopTimer();
        startNewGame();
    }

    public void winGame() {
        stopTimer();
        JOptionPane.showMessageDialog(window, "You won in " + timerDisplay.getText() + " seconds!");
        startNewGame();
    }

    /** Add Listeners */
    private void addListeners() {
        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void addCellMouseListener(int row, int col, MouseListener listener) {
        buttonGrid[row][col].addMouseListener(listener);
    }

    public void addCellActionListener(int row, int col, ActionListener listener) {
        buttonGrid[row][col].addActionListener(listener);
    }

    public void addNewMenuItemListener(ActionListener listener) {
        newGameMenuItem.addActionListener(listener);
    }

    public void addNewBtnListener(ActionListener listener) {
        newGameBtn.addActionListener(listener);
    }

    public void show() {
        addListeners();
        window.pack();
        window.setVisible(true);
    }

    public void test() {
        JFrame window2 = new JFrame();
        JButton button = new JButton("Name That Student");
        final JLabel lblStudentName = new JLabel("");

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblStudentName.setText("Steve");
            }
        });

        window2.add(button);
        window2.add(lblStudentName);
        window2.pack();
        window2.setVisible(true);
    }
}
