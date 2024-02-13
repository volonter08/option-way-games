package winners.mobile.application.game;


import winners.mobile.application.utils.Index2D;

public interface GameBoard<E extends BoardPiece> extends Game {
    int getRow();
    int getColumn();
    E getPiece(Index2D pos);
}
