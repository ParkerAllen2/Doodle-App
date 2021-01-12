package com.example.mydoodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

public class Layer2  extends View {
    Brush brush = MainActivity.brush;   //reference to brush

    private Bitmap myBitmap;    //it's stored bitmap
    private Canvas myCanvas;    //cnvas to draw on bitmap
    private int width, height;  //screen size
    private Rect bounds;        //screen size
    private String name;        //name of layer
    private boolean isVisible;  //if layer is visible

    private ArrayList<Draws> draws; //list of paths

    private ArrayList<Draws> undoneDraws;   //list of undone paths


    //constructor
    public Layer2(Context context, int w, int h, String name)
    {
        super(context);

        width = w;
        height = h;

        myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        myCanvas = new Canvas(myBitmap);
        bounds = new Rect(0, 0, width, height);

        this.name = name;
        isVisible = true;
        draws = new ArrayList<>();
        undoneDraws = new ArrayList<>();
    }

    //invalidate - draw stored bitmap
    public void onDraw(Canvas canvas)
    {
        canvas.drawBitmap(myBitmap, null, bounds, null);
    }

    //clear bitmap
    public void ClearBitmap()
    {
        myBitmap.eraseColor(Color.TRANSPARENT);
    }

    //draws drawing layes path to bitmap and store path in draws
    public void PaintLayer(DrawingLayer layer)
    {
        draws.add(new Draws(layer.getPath(), layer.getMyPaint()));
        myCanvas.drawPath(layer.getPath(), layer.getMyPaint());
    }

    //draw onto bitmap without adding to draws, used when erasing
    public void ErasePath(DrawingLayer layer){
        myCanvas.drawPath(layer.getPath(), layer.getMyPaint());
    }

    //remove from draws and add to undone draws, then redraw paths
    public void undo(){
        if(draws.size() > 0)
        {
            undoneDraws.add(draws.remove(draws.size() - 1));

            if(undoneDraws.size() > 20)
                undoneDraws.remove(0);

            RedrawPaths();
        }
    }

    //remove from undone draws and add to draws, then redraw paths
    public void redo(){
        if(undoneDraws.size() > 0)
        {
            draws.add(undoneDraws.remove(undoneDraws.size() - 1));
            RedrawPaths();
        }
    }

    //clears bitmap and redraw all draws
    private void RedrawPaths()
    {
        ClearBitmap();
        for(int i = 0; i < draws.size(); i++){
            myCanvas.drawPath(draws.get(i).path, draws.get(i).paint);
        }
        invalidate();
    }

    public String getName(){
        return name;
    }

    public void setName(String s)
    {
        name = s;
    }

    public boolean getIsVisible(){
        return isVisible;
    }

    public void setIsVisible(boolean v){
        isVisible = v;
    }

    public Bitmap getBitmap(){
        return myBitmap;
    }

    //inner class to store paths and paint used for path
    private class Draws{
        Path path;
        Paint paint;

        public Draws(Path p, Paint p2){
            path = p;
            paint = p2;
        }
    }
}
