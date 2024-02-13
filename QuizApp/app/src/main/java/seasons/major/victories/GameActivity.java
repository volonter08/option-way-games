package seasons.major.victories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import seasons.major.victories.game.quizapplication.QuizActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        CardView play = findViewById(R.id.cardview1);
        // Set a click listener on that View
        play.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(GameActivity.this, QuizActivity.class);
                // Start the new activity
                startActivity(playIntent);
            }
        });
    }
}