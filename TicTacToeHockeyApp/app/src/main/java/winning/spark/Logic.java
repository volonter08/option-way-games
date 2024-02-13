package winning.spark;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Logic {
    private int turn = 0;
    public static String firstMark = "X";
    public static String secondMark = "O";
    public final int SIZE = 3;
    private String[][] matrix = new String[SIZE][SIZE];

    public Logic() {
    }

    public Logic(String[][] matrix) {
        this.matrix = matrix;
    }

    public boolean isFilled() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matrix[i][j] != firstMark && matrix[i][j] != secondMark) return false;
            }
        }
        return true;
    }

    public boolean checkWin(String XorO) {
        String expect = null;
        for (int index = 0; index < SIZE; index++) {
            expect += XorO;
        }
        String result = null;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matrix[i][j] != null) {
                    result += matrix[i][j];
                }
            }
            if (expect.equals(result)) {
                return true;
            } else result = null;
        }

        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                if (matrix[i][j] != null) {
                    result += matrix[i][j];
                }
            }
            if (expect.equals(result)) {
                return true;
            } else result = null;
        }

        for (int j = 0, i = 0; j < SIZE; j++, i++) {
            if (matrix[i][j] != null) {
                result += matrix[i][j];
            }
        }
        if (expect.equals(result)) {
            return true;
        } else result = null;

        for (int i = SIZE - 1, j = 0; j < SIZE; j++, i--) {
            if (matrix[i][j] != null) {
                result += matrix[i][j];
            }
        }
        return expect.equals(result);
    }

    public void clickOnButton(String buttonTag) {
        if (matrix[Character.digit(buttonTag.charAt(0), 10)][Character.digit(buttonTag.charAt(1), 10)] == null
                && !checkWin(firstMark)
                && !checkWin(secondMark)) {
            matrix[Character.digit(buttonTag.charAt(0), 10)][Character.digit(buttonTag.charAt(1), 10)] = getValue();
            setTurn(getTurn() + 1);
        }
    }

    public int clickOnButtonWithAI() {
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!(Objects.equals(matrix[i][j], firstMark)) && !(Objects.equals(matrix[i][j], secondMark))) {
                    matrix[i][j] = secondMark;
                    if (checkWin(secondMark)) return i * SIZE + j;
                    matrix[i][j] = null;
                    temp.add(i * SIZE + j);
                }
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!(Objects.equals(matrix[i][j], firstMark)) && !(Objects.equals(matrix[i][j], secondMark))) {
                    matrix[i][j] = firstMark;
                    if (checkWin(firstMark)) {
                        matrix[i][j] = secondMark;
                        return i * SIZE + j;
                    }
                    matrix[i][j] = null;
                }
            }
        }
        int random = temp.get(new Random().nextInt(temp.size()));
        matrix[random / 3][random % 3] = secondMark;
        return random;
    }

    public void changeSide() {
        if (turn == 0) {
            firstMark = firstMark.equals("X") ? "O" : "X";
            secondMark = secondMark.equals("O") ? "X" : "O";
        }
    }

    public void changeSide(String side) {
        firstMark = side;
        secondMark = firstMark.equals("O") ? "X" : "O";
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public void clearMatrix() {
        matrix = new String[SIZE][SIZE];
        setTurn(0);
    }

    public String getValue() {
        return turn % 2 == 0 ? firstMark : secondMark;
    }

    public int getTurn() {
        return this.turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
