package com.hypersoft.flyinghagiwagi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class FlyingHagiWagi extends View {
    private Bitmap[] hagiWagi = new Bitmap[2];// slika hagija koji leti
    private int hagiX = 10; // X osa hagija koji leti
    private int hagiY; // Y osa hagija koji leti
    private int hagiSpeed;  // dodaje prilikom padanja
    private boolean touch =false; // da li je pritisnut ekran

    private int canvasWidth,canvasHeight;

    private int yellowX,yellowY,yellowSpeed = 16; // žuta loptica
    private Paint yellowPaint = new Paint();

    private int greenX,greenY,greenSpeed = 20;  //zelena loptica
    private Paint greenPaint = new Paint();

    private int redX,redY,redSpeed = 25;  //crvena loptica
    private Paint redPaint = new Paint();
    private int score,lifeCounterOfHagi;
    private Bitmap backgroundImage;
    private Paint scorePaint = new Paint();// služi da da boju, stil,...
    private Bitmap[] life = new Bitmap[2];// srca, životi

    PlayMusic playMusic;
    Vibrator vibrator;

    public FlyingHagiWagi(Context context){
        super(context);
        hagiWagi[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hagi_vagi);
        hagiWagi[1] = BitmapFactory.decodeResource(getResources(),R.drawable.hagi_vagi4);

        backgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.pozadina1);

        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);

        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);
        hagiY = 550;
        score = 0;
        lifeCounterOfHagi = 3;
        playMusic = new PlayMusic();

        // get the VIBRATOR_SERVICE system service
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);


    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvasHeight = getHeight();
        canvasWidth = getHeight();

        canvas.drawBitmap(backgroundImage,0,0,null);

        int minHagiY = hagiWagi[0].getHeight(); //donja ivica hagija koji leti
        int maxHagiY = canvasHeight-hagiWagi[0].getHeight()*3;//gornja ivica
        hagiY = hagiY + hagiSpeed;
        if (hagiY <= minHagiY)
            hagiY = minHagiY;
        if (hagiY >= maxHagiY)
            hagiY = maxHagiY;
        hagiSpeed = hagiSpeed +2;// ako ne klikćeš hagi stalno po malo pada
        if (touch){
            canvas.drawBitmap(hagiWagi[1],hagiX,hagiY,null);
            touch = false;
        }else {
            canvas.drawBitmap(hagiWagi[0],hagiX,hagiY,null);
        }


        if (hitBallChecker(yellowX,yellowY)){
            playMusic.playSound();
            score = score + 10; // povećava rezultat i lopticu resetuje
            yellowX = -100;
        }
        yellowX = yellowX -yellowSpeed;//loptica se kreće sa desne na levo
        if (yellowX<0){
            yellowX = canvasWidth +21;
            yellowY = (int) Math.floor(Math.random()*(maxHagiY-minHagiY))+minHagiY; //slučajan broj po Y osi
        }

        canvas.drawCircle(yellowX,yellowY,25,yellowPaint);

        if (hitBallChecker(greenX,greenY)){
            playMusic.playSound();
            score = score + 20; // povećava rezultat i lopticu resetuje
            greenX = -100;
        }
        greenX = greenX - greenSpeed;//loptica se kreće sa desne na levo
        if (greenX<0){
            greenX = canvasWidth +21;
            greenY = (int) Math.floor(Math.random()*(maxHagiY-minHagiY))+minHagiY; //slučajan broj po Y osi
        }

        canvas.drawCircle(greenX,greenY,25,greenPaint);

        if (hitBallChecker(redX,redY)){
            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            vibrator.vibrate(100);
            redX = -100;
            lifeCounterOfHagi--;
            if (lifeCounterOfHagi == 0){
                Toast.makeText(getContext(),"Game over",Toast.LENGTH_SHORT).show();
                Intent gameOverIntent = new Intent(getContext(),GameOverActivity.class);    // startuje novi Activity,završnu(Game Over)
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                gameOverIntent.putExtra("score",score);
                getContext().startActivity(gameOverIntent);



            }
        }
        redX = redX -greenSpeed;//loptica se kreće sa desne na levo
        if (redX<0){
            redX = canvasWidth +21;
            redY = (int) Math.floor(Math.random()*(maxHagiY-minHagiY))+minHagiY; //slučajan broj po Y osi
        }

        canvas.drawCircle(redX,redY,30,redPaint);

        canvas.drawText("Score: "+score,20,60,scorePaint);

        for (int i=0;i<3;i++){
            int x = (int) (580 + life[0].getWidth() * 1.5 * i);
            int y = 30;
            if (i<lifeCounterOfHagi)
                canvas.drawBitmap(life[0],x,y,null);// srca
            else
                canvas.drawBitmap(life[1],x,y,null);// prazno srce
        }
    }
    public boolean hitBallChecker(int x,int y){
        if (hagiX < x && x < (hagiX+hagiWagi[0].getWidth()) && hagiY < y && y < (hagiY+hagiWagi[0].getHeight())){
            return true;
        }
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            touch = true;
            hagiSpeed = -22; // kad klikneš hagi se podiže
        }
        return true;
    }
}
