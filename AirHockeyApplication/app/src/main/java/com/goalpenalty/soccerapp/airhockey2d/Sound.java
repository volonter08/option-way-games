package com.goalpenalty.soccerapp.airhockey2d;

import android.media.SoundPool;

import com.goalpenalty.soccerapp.GameActivity;
import com.goalpenalty.soccerapp.R;

public final class Sound {
    static int click;
    static int collide1;
    static int collide2;
    static int goal1;
    static int goal2;
    static int main;
    static int pause;
    static int resume;
    static final SoundPool sp;

    static {
        sp = new SoundPool(5, 3, 0);
    }

    public static void loadAll() {
        collide1 = sp.load(GameActivity.context, R.raw.collide2, 1);
        collide2 = sp.load(GameActivity.context, R.raw.collide3, 1);
        click = sp.load(GameActivity.context, R.raw.click1, 1);
        goal1 = sp.load(GameActivity.context, R.raw.com_goal, 1);
        goal2 = sp.load(GameActivity.context, R.raw.player_goal, 1);
        main = sp.load(GameActivity.context, R.raw.starting_main, 1);
        resume = sp.load(GameActivity.context, R.raw.resumed, 1);
        pause = sp.load(GameActivity.context, R.raw.paused, 1);
    }

    public static void play(int id) {
        sp.play(id, 1.0f, 1.0f, 0, 0, 1.0f);
    }
}
