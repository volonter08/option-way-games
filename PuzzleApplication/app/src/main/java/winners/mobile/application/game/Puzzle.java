package winners.mobile.application.game;

import androidx.annotation.NonNull;

import winners.mobile.application.utils.Move;

public interface Puzzle<E extends BoardPiece> extends GameBoard<E> {

    Move getLastMove();
    int getMoveCount();
    boolean move(@NonNull final Move move);
    boolean checkMove(@NonNull final Move move);
    boolean checkMove(int yMove, int xMove);
    void reset();
    boolean isSolved();
    float[][] getInputBoard();
}
