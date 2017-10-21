package practice.nazmul.practice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by nazmul on 8/26/15.
 */
public class MyBringBack extends View {

        Bitmap gBall;
        float changingY;
         Typeface font;

    public MyBringBack(Context context) {   // Constructor
        super(context);
        //TODO Auto-generated constructor

        gBall = BitmapFactory.decodeResource(getResources(),R.drawable.greenball); //Declare bitmap variable
        changingY =0;
        font = Typeface.createFromAsset(context.getAssets(),"G-Unit.ttf");


    }
// This mathod drow the bitmap
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(android.R.color.white);//Back
        Paint textPaint = new Paint();
        textPaint.setARGB(50,254,10,50); // here ...setARGB(Transparency,R,G,B)
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(50);
        textPaint.setTypeface(font);
        canvas.drawText("mybringback",canvas.getWidth()/2,200,textPaint);
        canvas.drawBitmap(gBall, (canvas.getWidth() / 2), changingY, null);// set the position of image
        if(changingY < canvas.getHeight()){
            changingY += 10; //It will increment 10 pixel

        }else {
            changingY = 0;
        }


        Rect middleRect = new Rect();
        middleRect.set(0, 400, canvas.getWidth(), 550);
        Paint ourBlue = new Paint();
        ourBlue.setColor(Color.BLUE);
        canvas.drawRect(middleRect,ourBlue);
        invalidate();
    }

}
