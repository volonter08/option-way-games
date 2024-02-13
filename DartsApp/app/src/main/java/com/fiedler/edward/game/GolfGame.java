package com.fiedler.edward.game;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Edward on 11/16/2016.
 */

public class GolfGame extends AppCompatActivity {

    GolfView animation;
    Spinner modeSpinner; //for choosing putting scheme

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golfgame);

        modeSpinner = (Spinner) findViewById(R.id.method);
        animation = (GolfView) findViewById(R.id.v1);
        animation.initHandler();

        modeSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, METHODS));

        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                restart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

    }

    static final String[] METHODS = {
            "Gesture","Tilt"
    };

    public void restart() {
        animation.restart((String) modeSpinner.getSelectedItem());
    }

    @Override
    protected void onResume() { //Start the game on resume
        super.onResume();
        animation.startAnimation();
    }

    @Override
    protected void onPause() { //pause animation upon pause
        super.onPause();
        animation.stopAnimation();
    }

}
