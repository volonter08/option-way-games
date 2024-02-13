package capture.ball.faster;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    Button  btnReset;
    ImageButton btnRockImg, btnPaperImg, btnScissorsImg;
    TextView  txtWinCount, txtTieCount, txtLooseCount;
    ImageView winnerImg, humanChoiceImg, computerChoiceImg;

    int winCount, tieCount, looseCount;



    enum Choice {ROCK, PAPER, SCISSORS}
    enum Winner {HUMAN, COMPUTER, TIE}

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        winCount = 0;
        looseCount = 0;
        tieCount = 0;



        btnReset = findViewById(R.id.btnReset);

        btnRockImg = findViewById(R.id.btnRockImg);
        btnPaperImg = findViewById(R.id.btnPaperImg);
        btnScissorsImg = findViewById(R.id.btnScissorsImg);

        txtWinCount = findViewById(R.id.txtWinCount);
        txtTieCount = findViewById(R.id.txtTieCount);
        txtLooseCount = findViewById(R.id.txtLooseCount);

        winnerImg = findViewById(R.id.winnerImg);
        humanChoiceImg = findViewById(R.id.humanChoiceImg);
        computerChoiceImg = findViewById(R.id.computerChoiceImg);

        btnRockImg.setOnClickListener(v -> {
            playGame(Choice.ROCK);
        });

        btnPaperImg.setOnClickListener(v -> {
            playGame(Choice.PAPER);
        });

        btnScissorsImg.setOnClickListener(v -> {
            playGame(Choice.SCISSORS);
        });

        btnReset.setOnClickListener(v -> {
            winCount = 0;
            tieCount = 0;
            looseCount = 0;

            txtWinCount.setText("" + winCount);
            txtTieCount.setText("" + tieCount);
            txtLooseCount.setText("" + looseCount);

            winnerImg.setImageResource(R.drawable.thinkingimg);
            humanChoiceImg.setVisibility(View.INVISIBLE);
            computerChoiceImg.setVisibility(View.INVISIBLE);
        });
    }

    void playGame(Choice humanChoice) {
        Choice computerChoice;

        Winner gameResult = Winner.TIE;

        int number = random.nextInt(3);
        computerChoice = Choice.values()[number];
        if (computerChoice == Choice.ROCK){
            computerChoiceImg.setVisibility(View.VISIBLE);
            computerChoiceImg.setImageResource(R.drawable.rocklogo);
        }

        if (computerChoice == Choice.PAPER){
            computerChoiceImg.setVisibility(View.VISIBLE);
            computerChoiceImg.setImageResource(R.drawable.paperlogo);
        }

        if (computerChoice == Choice.SCISSORS){
            computerChoiceImg.setVisibility(View.VISIBLE);
            computerChoiceImg.setImageResource(R.drawable.scissorslogo);
        }
        switch (humanChoice) {
            case ROCK:
                humanChoiceImg.setVisibility(View.VISIBLE);
                humanChoiceImg.setImageResource(R.drawable.rocklogo);
                break;
            case PAPER:
                humanChoiceImg.setVisibility(View.VISIBLE);
                humanChoiceImg.setImageResource(R.drawable.paperlogo);
                break;
            case SCISSORS:
                humanChoiceImg.setVisibility(View.VISIBLE);
                humanChoiceImg.setImageResource(R.drawable.scissorslogo);
                break;
            default: break;
        }
        if (humanChoice == computerChoice){
            gameResult = Winner.TIE;
            tieCount += 1;
            txtTieCount.setText("" + tieCount);

        }

        if (humanChoice == Choice.ROCK && computerChoice == Choice.PAPER){
            gameResult = Winner.COMPUTER;
            looseCount += 1;
            txtLooseCount.setText("" + looseCount);
        }
        if (humanChoice == Choice.ROCK && computerChoice == Choice.SCISSORS){
            gameResult = Winner.HUMAN;
            winCount += 1;
            txtWinCount.setText("" + winCount);
        }

        if (humanChoice == Choice.PAPER && computerChoice == Choice.ROCK){
            gameResult = Winner.HUMAN;
            winCount += 1;
            txtWinCount.setText("" + winCount);
        }
        if (humanChoice == Choice.PAPER && computerChoice == Choice.SCISSORS){
            gameResult = Winner.COMPUTER;
            looseCount += 1;
            txtLooseCount.setText("" + looseCount);
        }

        if (humanChoice == Choice.SCISSORS && computerChoice == Choice.ROCK){
            gameResult = Winner.COMPUTER;
            looseCount += 1;
            txtLooseCount.setText("" + looseCount);
        }
        if (humanChoice == Choice.SCISSORS && computerChoice == Choice.PAPER){
            gameResult = Winner.HUMAN;
            winCount += 1;
            txtWinCount.setText("" + winCount);
        }

        switch (gameResult) {
            case TIE:
                winnerImg.setImageResource(R.drawable.tieimg);
                break;
            case HUMAN:
                winnerImg.setImageResource(R.drawable.winimg);
                break;
            case COMPUTER:
                winnerImg.setImageResource(R.drawable.looseimg);
                break;
            default:
                break;
        }
    }


}