package com.example.mydoodle;

import java.util.TimerTask;

public class UpdateTimer extends TimerTask {

    private boolean drawing;
    private DrawingLayer layer;

    //while drawing keep invalidating
    public void run()
    {
        if(drawing)
        {
            layer.postInvalidate();
        }
    }

    public void SetLayer(DrawingLayer l)
    {
        layer = l;
    }

    public void setDrawing(boolean d)
    {
        drawing = d;
    }
}
