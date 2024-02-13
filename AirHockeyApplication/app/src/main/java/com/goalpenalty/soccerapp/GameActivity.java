package com.goalpenalty.soccerapp;
/*
 *  Property of Dmitrii Lykov(Дмитрий Лыков) aka Serious Games
 *  2018
 *
 *  Класс MainActivity
 *  основной Activity: слушает нажатия, взаимодействует с View-компонентами,
 *  принимает сообщения от DrawThread
 */

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.goalpenalty.soccerapp.airhockey2d.Log;
import com.goalpenalty.soccerapp.airhockey2d.MySurfaceView;


public class GameActivity extends AppCompatActivity {

    static public GameActivity context;
    public static boolean backPressed;
    private boolean created;
    private com.goalpenalty.soccerapp.airhockey2d.MySurfaceView surfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.msg("activity creating");
        context = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        if (!this.created) {
            this.created = true;
            this.surfaceView = new MySurfaceView(this);
        }
        if (this.surfaceView == null) {
            Log.msg("surface is gone!");
        }
        setContentView(this.surfaceView);
        backPressed = false;
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.msg("activity Destroying");
    }

    public void onBackPressed() {
        backPressed = true;
    }

}
