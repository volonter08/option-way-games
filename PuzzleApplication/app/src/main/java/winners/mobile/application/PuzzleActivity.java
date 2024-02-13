package winners.mobile.application;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;

import winners.mobile.application.game.NPuzzle;
import winners.mobile.application.game.Puzzle;
import winners.mobile.application.game.SlidePuzzle;
import winners.mobile.application.ui.OnSwipeListener;
import winners.mobile.application.ui.PuzzleLayoutManager;
import winners.mobile.application.ui.PuzzlePieceDecoration;
import winners.mobile.application.ui.SlidePuzzleAdapter;
import winners.mobile.application.ui.SlidePuzzleSwipeListener;
import winners.mobile.application.utils.AI;
import winners.mobile.application.utils.Move;
import winners.mobile.application.utils.PuzzleAI;
import winners.mobile.application.utils.Screen;

public class PuzzleActivity extends AppCompatActivity
    implements GameEventHandler, View.OnTouchListener{

    private SlidePuzzleAdapter<Integer> adapter;
    private RecyclerView recyclerView;
    private Puzzle<SlidePuzzle<Integer>.Piece> puzzle;
    private GestureDetectorCompat detector;
    private AI<Move> ai;
    private MediaPlayer buttonClick;
    private MediaPlayer gameWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final NPuzzle nPuzzle;
        TextView moveNum = findViewById(R.id.txt_move_num);

        if(extras != null) {
            nPuzzle = new NPuzzle(extras.getInt(getString(R.string.key_row_count)),
                    extras.getInt(getString(R.string.key_column_count)));
        }
        else nPuzzle = new NPuzzle(3, 3);

        puzzle = nPuzzle;
        try {
            adapter = new SlidePuzzleAdapter<>(this, nPuzzle, this, moveNum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        recyclerView = findViewById(R.id.rv_game_area);
        ai = new PuzzleAI(PuzzleActivity.this, puzzle);
        buttonClick = MediaPlayer.create(this, R.raw.button_click);
        gameWin = MediaPlayer.create(this, R.raw.game_win);

        moveNum.setText(String.valueOf(puzzle.getMoveCount()));
        configurePuzzleView();
        activateSwipe();
        onGameStart();
    }

    @Override
    public void onGameStart() {
        puzzle.initialize();
    }

    @Override
    public void onGameFinish() {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.info_puzzle_solved, null);
        View parent = findViewById(R.id.v_swipeable);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        gameWin.start();

        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.performClick();
        return detector.onTouchEvent(motionEvent);
    }

    public void onNewGameClick(View view) {

        buttonClick.start();
        finish();
    }

    public void onHintClick(View view) {

        Move move = ai.predict();
        puzzle.move(move);
        adapter.notifyBlankMoved(move);
    }

    private void configurePuzzleView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new PuzzleLayoutManager(this, puzzle.getColumn()));

        recyclerView.addItemDecoration(new PuzzlePieceDecoration(puzzle.getColumn(),
                Screen.dpToPixel(0, getResources())));

        recyclerView.setAdapter(adapter);
    }

    private void activateSwipe() {

        View swipeable = findViewById(R.id.v_swipeable);
        OnSwipeListener swipeListener =
                new SlidePuzzleSwipeListener<>((SlidePuzzle<Integer>) puzzle, adapter);
        detector = new GestureDetectorCompat(this, swipeListener);
        swipeable.setOnTouchListener(this);
    }
}
