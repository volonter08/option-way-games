package seasons.major.victories.game.quizapplication;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import seasons.major.victories.R;
import seasons.major.victories.databinding.ActivityQuizBinding;

public class QuizActivity extends AppCompatActivity {

    String doBetter = "You can do better!" + System.getProperty("line.separator") + System.getProperty("line.separator") + "Try again?";
    String poor = "Brush up your knowledge, maybe?";
    String congrats = "Well done!" + System.getProperty("line.separator") + "You are awesome!";
    // Question 1
    int final_score = 0;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityQuizBinding binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fab = binding.fab;
        List<String> listNames = new LinkedList<>();
        try {
            BufferedReader readerListNames = new BufferedReader(new InputStreamReader(getAssets().open("list_name.txt")));
            String name = readerListNames.readLine();
            while(name!=null){
                listNames.add(name);
                name = readerListNames.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Boolean> trueAnswers = new ArrayList<>(listNames.size());
        boolean[] userAnswers = new boolean[listNames.size()];
        try {
            BufferedReader readerListAnswers = new BufferedReader(new InputStreamReader(getAssets().open("list_true_answers.txt")));
            String name = readerListAnswers.readLine();
            while(name!=null){
                trueAnswers.add(Boolean.parseBoolean(name));
                name = readerListAnswers.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        QuizAdapter quizAdapter = new QuizAdapter(listNames,userAnswers);
        binding.recycleView.setAdapter(quizAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for( int i = 0; i < trueAnswers.size();i++){
                    if (trueAnswers.get(i) == userAnswers[i])
                        final_score++;
                }
                //Gets the instance of the LayoutInflater, uses the context of this activity
                LayoutInflater inflater = (LayoutInflater) QuizActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
                View layout = inflater.inflate(R.layout.popup, null);

                if (final_score <= 10) {
                    ((TextView) layout.findViewById(R.id.popup)).setText(poor);
                } else if (final_score > 10 && final_score <= 15) {
                    ((TextView) layout.findViewById(R.id.popup)).setText(doBetter);
                } else {
                    ((TextView) layout.findViewById(R.id.popup)).setText(congrats);
                }
                //Get the devices screen density to calculate correct pixel sizes
                float density = QuizActivity.this.getResources().getDisplayMetrics().density;
                // create a focusable PopupWindow with the given layout and correct size
                final PopupWindow pw = new PopupWindow(layout, (int) density * 350, (int) density * 400, true);
                pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                pw.setTouchInterceptor(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            pw.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
                pw.setOutsideTouchable(true);
                // display the pop-up in the center
                pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                Context context = getApplicationContext();
                CharSequence text = "Your Score: " + final_score;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                final_score = 0;
            }
        });
    }
}