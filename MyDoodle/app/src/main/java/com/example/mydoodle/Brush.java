package com.example.mydoodle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.widget.SeekBar;

public class Brush {

    //varaibles to determine paint
    private int size;
    private int color;
    private int hardness;
    private boolean circle;
    private boolean square;
    private boolean eraser;

    //constructor
    public Brush()
    {
        size = 30;
        color = Color.BLACK;
        hardness = 100;
        circle = true;
        square = false;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int s)
    {
        size = s;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int c)
    {
        color = c;
    }

    public int getHardness()
    {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    public boolean isCircle()
    {
        return circle;
    }

    public boolean isSquare()
    {
        return square;
    }

    public void setCircle(boolean c)
    {
        circle = c;
        square = !c;
    }

    public void setSquare(boolean s)
    {
        square = !s;
        circle = s;
    }

    public boolean isEraser()
    {
        return eraser;
    }

    public void setEraser(boolean e)
    {
        eraser = e;
    }

    //returns rect to draw
    public Rect getRect(int x, int y)
    {
        int half = size / 2;
        return  new Rect(x - half, y - half, x + half, y + half);
    }

    //were the blur starts
    public float getBlurRadius()
    {
        if(hardness >= 100)
            return 0.01f;

        float n = (float)size * ((100 - (float)hardness) / 100);
        return n;
    }
}
