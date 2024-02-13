package com.fiedler.edward.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.core.view.GestureDetectorCompat;

import java.util.Random;

/**
 * Created by Edward on 11/18/2016.
 */

public class GolfView extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener{

    private int width, height;
    private boolean done;
    private boolean surfaceAvailable;
    private int strokeCounter = 0;
    private MyBitmap golfball;
    private MyBitmap hole;
    private Random random = new Random();
    private SurfaceHolder holder;

    private enum Mode{
        GESTURE,
        TILT
    }
    private Mode method = Mode.GESTURE;//default is gesture

    //variables for gestures---
    private GestureDetectorCompat gestureDetector;
    private Paint paint = new Paint();
    private Point start, end; //for drawing velocity line
    //---------------

    //variables for accelerometer--
    private SensorManager sensMan;
    private Sensor sensor;
    private SensorEventListener sensListener;
    private float[] sensorData = new float[3];

    private static final int MAX_VELOCITY = 25;
    //-------------------

    //variables for MediaPlayer--
    private MediaPlayer swing;
    private MediaPlayer holeIn;
    //---------------------------

    public GolfView(Context context){
        super(context);
        gestureDetector = new GestureDetectorCompat(context, this);
        holder = getHolder();
        holder.addCallback(this);
        initGame("Gesture");
        sensMan = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        initSensor();
    }

    public GolfView(Context context, AttributeSet attrs){
        super(context, attrs);
        gestureDetector = new GestureDetectorCompat(context, this);
        holder = getHolder();
        holder.addCallback(this);
        initGame("Gesture");
        sensMan = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        initSensor();
    }

    public Handler handler;
    public void initHandler()
    {
        handler = new Handler();
    }

    @Override //do this so invalidate will call our doDraw()
    public void onDraw(Canvas canvas){
        doDraw(canvas);
    }

    //---------WORKING WITH GESTURES HERE
    public boolean onTouchEvent(MotionEvent event) { //start the onTouch
        if(method == Mode.GESTURE) {//disable if in tilt mode
            this.gestureDetector.onTouchEvent(event);
            invalidate();

            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_CANCEL) { //when you release the finger invalidate the view
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (start != null && end != null) {
                            hitBall(start, end);
                            if(swing != null) {
                                swing.start();
                            }
                            strokeCounter++;
                        }
                        start = end = null;
                        invalidate();
                    }
                }, 1000);
            }
        }
        return true;
    }

    //-----------Method for converting velocity line to speed for the golf ball
    private void hitBall(Point start, Point end){
        //determine the direction the ball will go
        int xSign = 0, ySign = 0;
        if (start.x > end.x){
            xSign = 1;
        } else if (end.x > start.x){
            xSign = -1;
        } else //start = end
        {
            xSign = 0;
        }

        if (start.y > end.y){
            ySign = 1;
        } else if (end.y > start.y){
            ySign = -1;
        } else //start = end
        {
            ySign = 0;
        }

        //determine the distance the ball will travel
        int xDistance = (int)Math.round(Math.abs(end.x - start.x)); //calculate the distance of the line in x direction
        int yDistance = (int)Math.round(Math.abs(end.y - start.y)); //calculate the distance of the line in y direction
        //Log.d("GolfView", "Distance of line: " + distance + "\n");

        if(xDistance > 0){
            golfball.setXVelocity(xSign*(int)(xDistance/25));
            golfball.xDistToGo = xDistance;
        }
        else //distance = 0
        {
            golfball.setXVelocity(0);
            golfball.xDistToGo = 0;
        }

        if(yDistance > 0){
            golfball.setYVelocity(ySign*(int)(yDistance/25));
            golfball.yDistToGo = yDistance;
        }
        else //distance = 0
        {
            golfball.setYVelocity(0);
            golfball.yDistToGo = 0;
        }
    }//---------END hitBall()

    //Method for determining velocity line start and end points
    private void gestureData(MotionEvent e1, MotionEvent e2) {

        start = new Point((int) e1.getX(), (int) e1.getY());
        end = new Point((int) e2.getX(), (int) e2.getY());
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //Do nothing
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(method == Mode.GESTURE){
            gestureData(e1, e2);
        }

        return true; //this will be the most used gesture
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //Do nothing here
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
       return true;
    }

    //---------END GESTURES

    //---------WORKING WITH DRAWING HERE
    class MyBitmap {
        Bitmap bitmap;
        int xVelocity = 0, yVelocity = 0;
        int xPos, yPos;
        int xDistToGo = 0;
        int yDistToGo = 0;
        int w, h;

        MyBitmap(int resId){
            bitmap = BitmapFactory.decodeResource(getResources(), resId);
            w = bitmap.getWidth();
            h = bitmap.getHeight();
        }


        void setBounds(int left, int top){
            xPos = left;
            yPos = top;
        }

        void setXVelocity(int xIN){
            this.xVelocity = xIN;
        }

        void setYVelocity( int yIN){
            this.yVelocity = yIN;
        }

        void moveGesture(){
            if(xDistToGo > 0 || yDistToGo > 0){
                if (xPos + w  >= width && xVelocity > 0 || xPos < 0 && xVelocity < 0) xVelocity = -xVelocity; //make bounce in x direction
                if (yPos + h >= height && yVelocity > 0 || yPos < 0 && yVelocity < 0) yVelocity = -yVelocity; //make bounce in y direction

                xPos += xVelocity;
                yPos += yVelocity;//increment position in correct direction

                xDistToGo = xDistToGo - Math.abs(xVelocity);
                yDistToGo = yDistToGo - Math.abs(yVelocity);

                if (xDistToGo <=0){ xDistToGo = 0;}
                if (yDistToGo <=0){ yDistToGo = 0;}

                if(xVelocity >0){
                    xVelocity--;
                } else if( xVelocity < 0){
                    xVelocity++;
                }

                if(yVelocity >0){
                    yVelocity--;
                }else if( yVelocity < 0){
                    yVelocity++;
                }

            }

        }

        void moveTilt(){
            int gx = -(int)Math.round(sensorData[0]); //TODO check for portrait
            int gy = (int)Math.round(sensorData[1]);

            xVelocity += gx;
            yVelocity += gy;
            xVelocity = Math.min(Math.max(xVelocity, -MAX_VELOCITY), MAX_VELOCITY);
            yVelocity = Math.min(Math.max(yVelocity, -MAX_VELOCITY), MAX_VELOCITY);

            if (xPos + w  >= width && xVelocity > 0 || xPos < 0 && xVelocity < 0) xVelocity = -xVelocity; //make bounce in x direction
            if (yPos + h >= height && yVelocity > 0 || yPos < 0 && yVelocity < 0) yVelocity = -yVelocity; //make bounce in y direction

            xPos += xVelocity;
            yPos += yVelocity;//increment position in correct direction

        }

        void draw(Canvas canvas){
            canvas.drawBitmap(bitmap, xPos, yPos, null); //draw in the correct spot
        }

    }// ---------END MyBitMap

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) { //when activity is initialized
        surfaceAvailable = true;
        startAnimation();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) { //doesn't call on screen rotate
        this.width = width;
        this.height = height;
        stopAnimation();
        synchronized (holder) {
            positionObjs();
        }
        startAnimation();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        surfaceAvailable = false;
        stopAnimation();
    }

    private void initGame(String meth){
        if (meth.equals("Gesture")){method = Mode.GESTURE;}
        else if (meth.equals("Tilt")){method = Mode.TILT;}
        strokeCounter = 0;
        golfball = null;
        hole = null;
        golfball = new MyBitmap(R.drawable.golfball);
        hole = new MyBitmap(R.drawable.golfhole);
    }

    private void initSensor(){
        sensor = sensMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor !=null){
            sensListener = new SensorEventListener() {

                @Override
                public void onSensorChanged(SensorEvent event) {
                    for(int i = 0; i<3; i++){
                        sensorData[i] = event.values[i];
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
        }
    }

    public void restart(String mode){
        if (width > 0 && height > 0) {
            stopAnimation();
            synchronized (holder) {
                initGame(mode);
                positionObjs();
                if(sensor!=null){initSensor();}
            }
            startAnimation();
        }
    }

    public void startAnimation() {
        done = false;
        if(sensListener!=null){
            sensMan.registerListener(sensListener, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
        swing = MediaPlayer.create(getContext(), R.raw.golfswing);
        holeIn = MediaPlayer.create(getContext(), R.raw.inhole);
        if (surfaceAvailable) {
            startRenderingThread();
        }
    }

    private void startRenderingThread() {
       new Thread(new Runnable() {
            public void run() {
                while (!done) {

                    Canvas c = null;
                    try {
                        c = holder.lockCanvas();
                        synchronized (holder) {
                            doDraw(c);
                            if (checkForScore(golfball, hole) == true){
                                if(holeIn != null){
                                    holeIn.start();
                                }
                                ShowVictoryMSG();
                                positionObjs();
                                strokeCounter = 0;
                            }
                        }
                    } finally {
                        if (c != null) {
                            holder.unlockCanvasAndPost(c);
                        }
                    }


                }
            }
        }).start();
    }

    public void stopAnimation() {
        done = true;
        if(sensListener!=null){
            sensMan.unregisterListener(sensListener);
        }
        if(swing != null){swing.release();}
        if(swing != null){holeIn.release();}
    }

    private void positionObjs() {

        int x = random.nextInt(width - 2 * golfball.bitmap.getWidth()) + golfball.bitmap.getWidth();
        int y = random.nextInt(height - 2 * golfball.bitmap.getHeight()) + golfball.bitmap.getHeight();
        golfball.setBounds(x, y);
        golfball.setXVelocity(0);
        golfball.setYVelocity(0);

        int xHole = random.nextInt(width - 2 * hole.bitmap.getWidth()) + hole.bitmap.getWidth();
        int yHole = random.nextInt(height - 2 * hole.bitmap.getHeight()) + hole.bitmap.getHeight();
        hole.setBounds(xHole, yHole);
        hole.setXVelocity(0);
        hole.setYVelocity(0);
    }

    //check if the center of the ball passes over the hole
    private boolean checkForScore(MyBitmap ball, MyBitmap goal){
        int XcenterOfBall = ball.xPos + (int)Math.round(ball.w / 2);
        int YcenterOfBall = ball.yPos + (int)Math.round(ball.h / 2);

        if (XcenterOfBall > hole.xPos && XcenterOfBall < (hole.xPos + hole.w)){
            if (YcenterOfBall > hole.yPos && YcenterOfBall < (hole.yPos + hole.h)){
                return true;
            }
        }
        return false;
    }

    //display a toast
    private void ShowVictoryMSG(){

        if(method == Mode.GESTURE) {
            if(strokeCounter == 1){
                handler.post(new Runnable(){
                    public void run(){
                        Toast.makeText(getContext(), "Hole in One!", Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                final int safeCounter = strokeCounter;
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getContext(), "You won in " + safeCounter + " strokes", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else //method == Mode.TILT
        {
            handler.post(new Runnable(){
                public void run(){
                    Toast.makeText(getContext(), "You won!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    protected void doDraw(Canvas canvas) {

        canvas.drawColor(getResources().getColor(R.color.colorBackground));
        if(method == Mode.GESTURE){golfball.moveGesture();}
        if(method == Mode.TILT){golfball.moveTilt();}
        golfball.draw(canvas);
        hole.draw(canvas); //The hole never moves like the ball does

        drawVelocityLine(canvas);
    }

    protected void drawVelocityLine(Canvas canvas){
        if (start != null && end != null) {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
        }
    }

}



