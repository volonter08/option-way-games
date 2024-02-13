package gahing.tutoril.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        preferences = this.getSharedPreferences("high_score",MODE_PRIVATE);
        TextView scoreLabel=(TextView)findViewById(R.id.scoreLabel);
        TextView highScoreLabel = findViewById(R.id.high_score);
        int score=getIntent().getIntExtra("SCORE",0);
        int highScore= preferences.getInt("value",0);
        if(score> highScore ){
            preferences.edit().putInt("value",score).apply();
        }
        highScoreLabel.setText(highScore+"");
        scoreLabel.setText(score+"");
    }

    public void tryAgain(View view) {
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}