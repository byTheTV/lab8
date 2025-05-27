package GUI.components;

import java.util.Random;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.I18NProvider;

public class TicTacToeComponent extends VerticalLayout {
    private Button[][] buttons = new Button[3][3];
    private boolean isPlayerTurn = true;
    private boolean gameOver = false;
    private final I18NProvider i18NProvider;
    private final Random random = new Random();

    public TicTacToeComponent(I18NProvider i18NProvider) {
        this.i18NProvider = i18NProvider;
        setWidth("100%");
        setSpacing(true);
        setPadding(true);
        
        // Add title
        Div title = new Div();
        title.setText("Tic Tac Toe");
        title.getStyle()
            .set("font-size", "1.2em")
            .set("font-weight", "bold")
            .set("margin-bottom", "10px");
        add(title);

        // Create game board
        for (int i = 0; i < 3; i++) {
            HorizontalLayout row = new HorizontalLayout();
            row.setSpacing(true);
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setWidth("60px");
                button.setHeight("60px");
                final int rowIndex = i;
                final int col = j;
                
                button.addClickListener(e -> {
                    if (!gameOver && isPlayerTurn && button.getText().isEmpty()) {
                        makeMove(button, rowIndex, col);
                        if (!gameOver) {
                            isPlayerTurn = false;
                            makeComputerMove();
                        }
                    }
                });
                
                buttons[i][j] = button;
                row.add(button);
            }
            add(row);
        }

        // Add reset button
        Button resetButton = new Button("New Game");
        resetButton.addClickListener(e -> resetGame());
        add(resetButton);
    }

    private void makeMove(Button button, int row, int col) {
        button.setText("X");
        button.getStyle().set("color", "blue");
        
        if (checkWin("X")) {
            showGameOver("You won!");
            return;
        }
        
        if (isBoardFull()) {
            showGameOver("It's a draw!");
        }
    }

    private void makeComputerMove() {
        if (gameOver) return;

        // Try to find winning move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    buttons[i][j].setText("O");
                    if (checkWin("O")) {
                        buttons[i][j].getStyle().set("color", "red");
                        showGameOver("Computer won!");
                        return;
                    }
                    buttons[i][j].setText("");
                }
            }
        }

        // Try to block player's winning move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    buttons[i][j].setText("X");
                    if (checkWin("X")) {
                        buttons[i][j].setText("O");
                        buttons[i][j].getStyle().set("color", "red");
                        isPlayerTurn = true;
                        if (checkWin("O")) {
                            showGameOver("Computer won!");
                        }
                        return;
                    }
                    buttons[i][j].setText("");
                }
            }
        }

        // Make random move
        while (true) {
            int row = random.nextInt(3);
            int col = random.nextInt(3);
            if (buttons[row][col].getText().isEmpty()) {
                buttons[row][col].setText("O");
                buttons[row][col].getStyle().set("color", "red");
                if (checkWin("O")) {
                    showGameOver("Computer won!");
                } else if (isBoardFull()) {
                    showGameOver("It's a draw!");
                }
                isPlayerTurn = true;
                break;
            }
        }
    }

    private boolean checkWin(String symbol) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(symbol) &&
                buttons[i][1].getText().equals(symbol) &&
                buttons[i][2].getText().equals(symbol)) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (buttons[0][j].getText().equals(symbol) &&
                buttons[1][j].getText().equals(symbol) &&
                buttons[2][j].getText().equals(symbol)) {
                return true;
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][2].getText().equals(symbol)) {
            return true;
        }

        if (buttons[0][2].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][0].getText().equals(symbol)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showGameOver(String message) {
        gameOver = true;
        Div gameOverDiv = new Div();
        gameOverDiv.setText(message);
        gameOverDiv.getStyle()
            .set("font-weight", "bold")
            .set("margin-top", "10px");
        add(gameOverDiv);
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].getStyle().remove("color");
            }
        }
        gameOver = false;
        isPlayerTurn = true;
        removeAll();
        add(new Div("Tic Tac Toe"));
        
        // Recreate game board
        for (int i = 0; i < 3; i++) {
            HorizontalLayout row = new HorizontalLayout();
            row.setSpacing(true);
            for (int j = 0; j < 3; j++) {
                row.add(buttons[i][j]);
            }
            add(row);
        }
        add(new Button("New Game", e -> resetGame()));
    }
} 