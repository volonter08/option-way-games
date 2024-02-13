package gahing.tutoril.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private Spinner spinner;
    private ImageView imageView1,imageView2,imageView3;
    private String[] selector = { "one", "two", "three" };
    private int[] dices = {R.drawable.ic_dice1_flat,R.drawable.ic_dice2_flat,R.drawable.ic_dice3_flat,R.drawable.ic_dice4_flat, R.drawable.ic_dice5_flat, R.drawable.ic_dice6_flat};
    private int rollNum = 1;
    private int score = 0;
    private TextView scoreLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        spinner = findViewById(R.id.selector);
        imageView1 = findViewById(R.id.dice1);
        imageView2 = findViewById(R.id.dice2);
        imageView3 = findViewById(R.id.dice3);
        scoreLabel = findViewById(R.id.scoreLabel);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, selector);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                    rollNum = 1;
                }
                if(i==1)
                {
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.GONE);
                    rollNum = 2;
                }
                if(i==2)
                {
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    rollNum = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onShakeClick(View view) {
        Random random = new Random();
        int dice1 = -1;
        int dice2 = -1;
        int dice3 = -1;

        switch (rollNum)
        {
            case 1:
                dice1 = random.nextInt(6);
                imageView1.setImageResource(dices[dice1]);
                break;
            case 2:
                dice1 = random.nextInt(6);
                dice2 = random.nextInt(6);
                imageView1.setImageResource(dices[dice1]);
                imageView2.setImageResource(dices[dice2]);
                break;
            case 3:
                dice1 = random.nextInt(6);
                dice2 = random.nextInt(6);
                dice3 = random.nextInt(6);
                imageView1.setImageResource(dices[dice1]);
                imageView2.setImageResource(dices[dice2]);
                imageView3.setImageResource(dices[dice3]);
                break;
        }
        if(dice1== 5 || dice2== 5 || dice3==5) {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            score = 0;
            startActivity(intent);
        }
        else {
            score += dice1 + dice2 + dice3 + 3;
        }
        scoreLabel.setText("Score:" + score);
    }

}
