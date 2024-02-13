package winners.mobile.application.ui;


import winners.mobile.application.game.SlidePuzzle;
import winners.mobile.application.utils.Move;

public class SlidePuzzleSwipeListener<T> extends OnSwipeListener {

    private final SlidePuzzle<T> puzzle;
    private final SlidePuzzleAdapter<T> adapter;

    public SlidePuzzleSwipeListener(final SlidePuzzle<T> argPuzzle,
                                    final SlidePuzzleAdapter<T> argAdapter) {
        puzzle = argPuzzle;
        adapter = argAdapter;
    }

    @Override
    public boolean onSwipe(Move direction) {

        if(puzzle.move(direction)) {
            adapter.notifyBlankMoved(direction);
        }

        //event used
        return true;
    }
}
