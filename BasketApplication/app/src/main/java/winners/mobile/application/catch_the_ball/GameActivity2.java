package winners.mobile.application.catch_the_ball;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import winners.mobile.application.GameActivity;
import winners.mobile.application.R;

public class GameActivity2 extends AppCompatActivity {

    private TextView scoreLable;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView black;
    private ImageView playoption;

    //Size
    private int frameWidth;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;


    //position
    private int boxX;
    private int orangeX;
    private int orangeY;
    private int blackX;
    private int blackY;

    //Speed
    private int boxSpeed;
    private int orangeSpeed;
    private int pinkSpeed;
    private int blackSpeed;

    //Score
    private int score=0;

    //Initilaize Class
    private Handler handler=new Handler();
    private Timer timer=new Timer();
    private SoundPlayer sound;


    //status Check
    private boolean action_flag=false;
    private boolean start_flag=false;
    private boolean pause_flg=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);

        sound=new SoundPlayer(this);

        scoreLable=(TextView)findViewById(R.id.scoreLabel);
        startLabel=(TextView)findViewById(R.id.startLabel);
        box=(ImageView)findViewById(R.id.box);
        orange=(ImageView)findViewById(R.id.orange);
        black=(ImageView)findViewById(R.id.black);
        playoption=(ImageView)findViewById(R.id.playoption);

        playoption.setEnabled(false);

        //Get Screen Size;
        WindowManager windowManager=getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);

        screenWidth=size.x;
        screenHeight=size.y;
        black.setVisibility(View.GONE);
        orange.setVisibility(View.GONE);
        //Move Out of screen
        black.setY(-80);
        orange.setY(-80);
        scoreLable.setText("Score : 0");
    }

    public void changepos()
    {
        hitCheck();
        if(black.getVisibility() == View.GONE) {
            blackX = ((int) Math.floor(Math.random() * (frameWidth - black.getWidth())));
            orangeX = (int) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
            black.setVisibility(View.VISIBLE);
            orange.setVisibility(View.VISIBLE);
        }
        //orange
        orangeY +=orangeSpeed;
        if( orangeY>=screenHeight) {
            orangeY = 20;
            orangeX = (int)Math.floor(Math.random()*(frameWidth - orange.getWidth()));
        }
        orange.setY(orangeY);
        orange.setX(orangeX);

        //black
        blackY +=blackSpeed;
        if( blackY>=screenHeight) {
            blackY = 20;
            blackX = (int)Math.floor(Math.random()*(frameWidth - black.getWidth()));
        }
        black.setY(blackY);
        black.setX(blackX);

        boxSpeed=Math.round(screenWidth/60F);
        orangeSpeed=Math.round(screenHeight/100F);
        pinkSpeed=Math.round(screenHeight/60F);
        blackSpeed=Math.round(screenHeight/ 100F);

        //Move Box
        if(action_flag==true) {
            boxX += boxSpeed;
        }
        else {
            boxX -= boxSpeed;
        }

        //check box position
        if(boxX<0) boxX=0;

        if(boxX> frameWidth - boxSize) boxX = frameWidth - boxSize;

        box.setX(boxX);

        scoreLable.setText("Score : "+score);
    }

    public void hitCheck(){

        //orange
        int orangeCenterX=orangeX+orange.getWidth()/2;
        int orangeCenterY=orangeY+orange.getHeight()/2;

        if(screenHeight>=orangeCenterY && orangeCenterY<=box.getY()+ boxSize&& box.getY() <= orangeCenterY
                && boxX <= orangeCenterX && orangeCenterX <= boxX + boxSize){
            score+=10;
            orangeY = screenHeight;
            sound.playHitSound();
        }

        //Black
        int blackCenterX=blackX+black.getWidth()/2;
        int blackCenterY=blackY+black.getHeight()/2;

        if(screenHeight>=blackCenterY && blackCenterY<=box.getY()+ boxSize&& box.getY() <= blackCenterY
                && boxX <= blackCenterX && blackCenterX <= boxX + boxSize){
            // Stop Timer!!
            timer.cancel();
            timer = null;
            sound.playOverSound();


            //Show Result
            Intent intent=new Intent(getApplicationContext(),resultActivity.class);
            intent.putExtra("SCORE",score);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void pausePushed(View view) {

        if (pause_flg == false) {

            pause_flg = true;

            // Stop the timer.
            timer.cancel();
            timer = null;

            // Change Button Text.
            playoption.setImageResource(R.drawable.play);


        } else {

            pause_flg = false;

            // Change Button Text.
            playoption.setImageResource(R.drawable.pause);

            // Create and Start the timer.
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changepos();
                        }
                    });
                }
            }, 0, 20);

        }
    }

    public boolean onTouchEvent(MotionEvent me)
    {
        if(start_flag==false) {
            start_flag=true;

            FrameLayout frame =(FrameLayout)findViewById(R.id.frame);
            frameWidth =frame.getWidth();

            boxX=(int)box.getX();

            boxSize=box.getHeight();

            startLabel.setVisibility(View.GONE);

            playoption.setEnabled(true);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changepos();
                        }
                    });
                }
            },0,20);
        }
        else {
            if(me.getAction()==MotionEvent.ACTION_DOWN) {
                action_flag = true;
            }
            else if (me.getAction()==MotionEvent.ACTION_UP) {
                action_flag = false;
            }
        }
        return true;
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

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Game");
        builder.setMessage("Are You Sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent=new Intent(getApplicationContext(), GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
