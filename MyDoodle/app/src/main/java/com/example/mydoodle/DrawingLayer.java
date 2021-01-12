package com.example.mydoodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.View;

public class DrawingLayer extends View {

    Brush brush = MainActivity.brush;   //reference to brush

    private int width, height;  //size of screen
    private Paint myPaint;      //paint used to draw
    private Rect bounds;        //Rect of screen
    private Path path;          //path it is making

    //constructor
    public DrawingLayer(Context context, int w, int h)
    {
        super(context);

        width = w;
        height = h;

        path = new Path();
        bounds = new Rect(0, 0, width, height);

        myPaint = new Paint();
    }

    //invaildated draw - draw its path onto it's canvas
    public void onDraw(Canvas canvas)
    {
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(myBitmap);
        myCanvas.drawPath(path, myPaint);
        canvas.drawBitmap(myBitmap, null, bounds, null);
    }

    //called at start of draw - create path and new paint
    public void StartPath(float x, float y)
    {
        path.moveTo(x, y);
        setMyPaint();
    }

    //called as drawing - add points to path
    public void AddPath(float x, float y)
    {
        path.lineTo(x, y);
    }

    //called at end of draw - makes new path
    public void EndPath()
    {
        path = new Path();
    }

    //makes new paint based of brush settings
    private void setMyPaint()
    {
        myPaint = new Paint();
        myPaint.setColor(brush.getColor());
        myPaint.setAntiAlias(true);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeJoin(Paint.Join.ROUND);
        if(brush.isCircle())
            myPaint.setStrokeCap(Paint.Cap.ROUND);
        else
            myPaint.setStrokeCap(Paint.Cap.SQUARE);

        if(brush.isEraser()){
            myPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            myPaint.setStrokeWidth(brush.getSize());
        }
        else
        {
            myPaint.setStrokeWidth(brush.getSize() - brush.getBlurRadius());
            myPaint.setXfermode(null);
            myPaint.setMaskFilter(new BlurMaskFilter(brush.getBlurRadius(), BlurMaskFilter.Blur.NORMAL));
        }
    }

    public Path getPath()
    {
        return path;
    }

    public Paint getMyPaint()
    {
        return myPaint;
    }
}
