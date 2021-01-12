package com.example.mydoodle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    public static Brush brush;  //the brush

    private int height, width;  //screen size
    private RelativeLayout.LayoutParams params;
    private RelativeLayout scene;
    private UpdateTimer updateTimer;    //invaildate timer
    private DrawingLayer drawingLayer;  //drawing layer

    private LayerInterface layerInterface;  //layer interface
    private ArrayList<Layer2> layerList;    //list of visible layers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Removed to send
        //ask for permission to export image
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }*/

        setContentView(R.layout.activity_main);
        scene = (RelativeLayout)findViewById(R.id.scene);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        brush = new Brush();                //brush
        layerList = new ArrayList<>();      //list of layers

        //start timer
        updateTimer = new UpdateTimer();
        Timer timer = new Timer();
        timer.schedule(updateTimer, 2000, 20);

        StartApp();
        updateTimer.SetLayer(drawingLayer);
    }

    private void StartApp()
    {
        //get screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        //create drawing layer
        drawingLayer = new DrawingLayer(this, width, height);
        drawingLayer.setOnTouchListener(new TouchHandler());
        params = new RelativeLayout.LayoutParams(0,0);
        params.height = height;
        params.width = width;
        drawingLayer.setLayoutParams(params);
        scene.addView(drawingLayer);

        layerInterface = new LayerInterface(this, height, width);
        params = new RelativeLayout.LayoutParams(0,0);
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layerInterface.setLayoutParams(params);

        layerList = layerInterface.getLayers();
        scene.addView(layerInterface.getCurrentLayer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.brush)       //brush
        {
            StartBrushActivity();
        }
        else if (id == R.id.colorPicker)    //color
        {
            StartColorActivity();
        }
        else if(id == R.id.layermenu)       //layer menu
        {
            try {
                ((ViewGroup) layerInterface.getParent()).removeView(layerInterface);
                SortLayers();
            }
            catch (Exception e){
                scene.addView(layerInterface);
            }
        }
        else if(id == R.id.undo)    //undo
        {
            layerInterface.getCurrentLayer().undo();
        }
        else if(id == R.id.redo)    //redo
        {
            layerInterface.getCurrentLayer().redo();
        }
        else if(id == R.id.export)      //export
        {
            //RemoveToSend
            //saveImage(createFinalBitmap(), layerList.get(0).getName());
        }

        return true;
    }

    //sorts order on how layers should appears, adds drawing layer just above selected layer
    private void SortLayers()
    {
        try {
            ((ViewGroup) drawingLayer.getParent()).removeView(drawingLayer);
        }catch (Exception e){}

        for(Layer2 lt : layerList)
            ((ViewGroup) lt.getParent()).removeView(lt);

        layerList = layerInterface.getLayers();
        for(int i = 0; i < layerList.size(); i++){
            if(layerList.get(i).getIsVisible())
            {
                scene.addView(layerList.get(i));
                if(i == layerInterface.getSelectedLayer())
                    scene.addView(drawingLayer);
            }
        }
    }

    //start brush activity
    public void StartBrushActivity()
    {
        Intent next = new Intent(this, BrushActivity.class);
        startActivity(next);
    }

    //start color activity
    public void StartColorActivity()
    {
        Intent next = new Intent(this, ColorPickerActivity.class);
        startActivity(next);
    }

    //touch handler for drawing layer
    private class TouchHandler implements View.OnTouchListener
    {
        public boolean onTouch(View view, MotionEvent event)
        {
            int action = event.getAction();
            float x = event.getX();
            float y = event.getY();

            //start new path for drawing layer
            if (action == MotionEvent.ACTION_DOWN)
            {
                updateTimer.setDrawing(true);
                drawingLayer.StartPath(x, y);
                drawingLayer.AddPath(x, y);
                if(brush.isEraser())
                    layerInterface.getCurrentLayer().ErasePath(drawingLayer);
            }
            //add points to drawing layer
            else if (action == MotionEvent.ACTION_MOVE)
            {
                drawingLayer.AddPath(x, y);
                if(brush.isEraser())
                    layerInterface.getCurrentLayer().ErasePath(drawingLayer);
            }
            //add drawing layers path to selected layer
            else if (action == MotionEvent.ACTION_UP)
            {
                updateTimer.setDrawing(false);
                layerInterface.getCurrentLayer().PaintLayer(drawingLayer);
                drawingLayer.EndPath();
            }

            layerInterface.getCurrentLayer().invalidate();
            return true;
        }
    }
    /* Removed to send
    //creates bitmap to export of all visible layers
    private Bitmap createFinalBitmap(){
        Bitmap tmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas myCanvas = new Canvas(tmp);
        Rect bounds = new Rect(0, 0, width, height);

        for(Layer2 l : layerList)
            myCanvas.drawBitmap(l.getBitmap(),null , bounds, null);

        return tmp;
    }

    //exports the image to storage
    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + image_name+ ".PNG";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            System.out.println(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}
