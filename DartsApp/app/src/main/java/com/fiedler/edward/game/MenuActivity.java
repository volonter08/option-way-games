package com.fiedler.edward.game;

import android.content.Intent;
import android.media.MediaPlayer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

    private MediaPlayer mybackground;
    private static final int SETTINGS = 100; // settings request code

    @Override
    protected void onPause() {
        super.onPause();
        if(mybackground != null) {
            mybackground.release();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mybackground = MediaPlayer.create(this, R.raw.backmusic);
        if(mybackground != null && MusicVal == true) {
            mybackground.start();
            mybackground.setLooping(true);
        }
        if(mybackground != null && MusicVal == false) {
            mybackground.release();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //button for switching to Game
        Button bGame = (Button) findViewById(R.id.buttonGame);
        bGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, GolfGame.class);
                startActivity(intent);
            }
        });


        //button for switching to Settings
        Button bSettings = (Button) findViewById(R.id.buttonSettings);
        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Settings.class);
                startActivityForResult(intent, SETTINGS);
            }
        });


    }

    //return from Settings
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS) {
            if (resultCode == RESULT_OK) {
                String result = new String(data.getCharSequenceExtra("Response").toString());
                if(result.equals("ON")){
                    MusicVal = true;
                }
                else if (result.equals("OFF")){
                    MusicVal = false;
                }
            }
            else {
                //Do nothing
            }
        }
    }


    static boolean MusicVal = true;

}
