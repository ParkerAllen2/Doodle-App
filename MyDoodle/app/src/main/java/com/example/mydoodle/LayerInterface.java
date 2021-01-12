package com.example.mydoodle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class LayerInterface extends ScrollView {

    final int DP = (int)(getResources().getDisplayMetrics().density);
    final int rowHeight = 60 * DP;  //height of tabs
    private Context context;

    //list of tabs for layers
    private ArrayList<LayerTab> layerTabs = new ArrayList<>();
    LinearLayout linearLayout;

    private int height, width;  //screen size
    private int selectedLayer;  //selected layer
    private int layerCounter = 1;   //number of layers made


    //constructor
    public LayerInterface(Context context, int height, int width)
    {
        super(context);
        this.context = context;
        this.height = height;
        this.width = width;

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        linearLayout.setPadding(0, 75 * DP, 0, 5 * DP);

        ScrollView.LayoutParams params = new ScrollView.LayoutParams(0,0);
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.MATCH_PARENT;
        linearLayout.setLayoutParams(params);

        CreateButtons();    //pluse and minus buttons

        selectedLayer = 0;
        layerTabs.add(new LayerTab("Background"));
        layerTabs.get(selectedLayer).row.setBackgroundColor(Color.rgb(0,150,140));
        linearLayout.addView(layerTabs.get(0).row);

        this.addView(linearLayout);
    }

    //adds new layer tab to layer tabs
    public void addLayer(){
        UnDisplayLayerTabs();

        layerTabs.add(new LayerTab("Layer " + layerCounter));
        layerCounter++;
        DisplayLayersTabs();
    }

    //removes selected layer tab from layer tabs
    public void removeLayer(){
        if(layerTabs.size() > 1){
            UnDisplayLayerTabs();
            layerTabs.remove(selectedLayer);

            if(selectedLayer >= layerTabs.size())
                selectedLayer = layerTabs.size() - 1;

            layerTabs.get(selectedLayer).row.setBackgroundColor(Color.rgb(0,150,140));
            DisplayLayersTabs();
        }
    }

    //return true if it can swap, swaps with order with layer above it
    public boolean swapUp(){
        if(selectedLayer < layerTabs.size() - 1)
        {
            layerTabs.add(selectedLayer + 1, layerTabs.remove(selectedLayer));
            selectedLayer++;
            UnDisplayLayerTabs();
            DisplayLayersTabs();
            return true;
        }
        return false;
    }

    //return true if it can swap, swaps with order with layer below it
    public boolean swapDown(){
        if(selectedLayer > 0)
        {
            layerTabs.add(selectedLayer - 1, layerTabs.remove(selectedLayer));
            selectedLayer--;
            UnDisplayLayerTabs();
            DisplayLayersTabs();
            return true;
        }
        return false;
    }

    //remove all layer tabs from view
    private void UnDisplayLayerTabs(){
        for(LayerTab lt : layerTabs)
            ((ViewGroup) lt.row.getParent()).removeView(lt.row);
    }

    //add all layer tabs to view
    private void DisplayLayersTabs(){
        for(int i = layerTabs.size() - 1; i >= 0; i--)
            linearLayout.addView(layerTabs.get(i).row);
    }

    //return Layer 2 of current layer
    public Layer2 getCurrentLayer()
    {
        return layerTabs.get(selectedLayer).layer;
    }

    //returns the place in list of selected layer
    public int getSelectedLayer(){
        return selectedLayer;
    }

    //return all visible layers
    public ArrayList<Layer2> getLayers(){
        ArrayList<Layer2> rtn = new ArrayList<>();
        for(LayerTab lt : layerTabs)
            if(lt.layer.getIsVisible())
                rtn.add(lt.layer);

        return rtn;
    }

    //creates add layer and remove layer buttons
    private void CreateButtons()
    {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
        params.width = LayoutParams.MATCH_PARENT;
        params.height = 40 * DP;
        params.setMargins(5 * DP, 5 * DP, 5 * DP, 0);
        row.setLayoutParams(params);

        //add button
        Button addButton = new Button(context);
        addButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        addButton.setText("+");
        addButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        addButton.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addLayer();
            }
        });

        params = new LinearLayout.LayoutParams(0, 0);
        params.width = 0;
        params.height = LayoutParams.MATCH_PARENT;
        params.weight = 1;
        params.rightMargin = 2 * DP;
        addButton.setLayoutParams(params);
        row.addView(addButton);

        //remove button
        Button removeButton = new Button(context);
        removeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        removeButton.setText("-");
        removeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        removeButton.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeLayer();
            }
        });

        params = new LinearLayout.LayoutParams(0, 0);
        params.width = 0;
        params.height = LayoutParams.MATCH_PARENT;
        params.weight = 1;
        params.leftMargin = 2 * DP;
        removeButton.setLayoutParams(params);
        row.addView(removeButton);

        linearLayout.addView(row);
    }

    //inner class fo layer tabs
    private class LayerTab extends LinearLayout
    {
        Layer2 layer;   //stored layer
        EditText nameOfLayer;   //name of layer
        LinearLayout row;

        //constructor
        @SuppressLint("ClickableViewAccessibility")
        public LayerTab(String name)
        {
            super(context);

            layer = new Layer2(context, width, height, name);
            RelativeLayout.LayoutParams paramsR = new RelativeLayout.LayoutParams(0,0);
            paramsR.height = height;
            paramsR.width = width;
            layer.setLayoutParams(paramsR);

            row = new LinearLayout(context);
            row.setOrientation(HORIZONTAL);
            row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            row.setOnTouchListener(new TouchHandler());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            params.width = FrameLayout.LayoutParams.MATCH_PARENT;
            params.height = rowHeight;
            params.setMargins(5 * DP, 5 * DP, 5 * DP, 0);
            row.setLayoutParams(params);

            //visibility checkbox
            final CheckBox visibleToggle = new CheckBox(context);
            visibleToggle.setChecked(true);
            visibleToggle.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    layer.setIsVisible(visibleToggle.isChecked());
                }
            });

            params = new LinearLayout.LayoutParams(0, 0);
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
            visibleToggle.setLayoutParams(params);
            row.addView(visibleToggle);


            //edit text for layer name
            nameOfLayer = new EditText(context);
            nameOfLayer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            nameOfLayer.setText(layer.getName());
            nameOfLayer.addTextChangedListener(new TextChangeHandler());

            params = new LinearLayout.LayoutParams(0, 0);
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
            nameOfLayer.setLayoutParams(params);
            row.addView(nameOfLayer);
        }

        private class TextChangeHandler implements TextWatcher{

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                layer.setName(nameOfLayer.getText().toString());
            }
        }

        //touch handler for swaping layers
        private class TouchHandler implements View.OnTouchListener{
            private int starty;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                int y = (int)event.getRawY();

                if(action == MotionEvent.ACTION_DOWN){
                    layerTabs.get(selectedLayer).row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    selectedLayer = layerTabs.indexOf(LayerTab.this);
                    layerTabs.get(selectedLayer).row.setBackgroundColor(Color.rgb(0,150,140));
                    starty = y;
                }
                else if(action == MotionEvent.ACTION_MOVE){
                    DragSwap(y);
                }

                return true;
            }

            private void DragSwap(int y){
                if(y - starty > 60 && swapDown()){        //swap down
                    starty += 60;
                    DragSwap(y);
                }
                else if(y - starty < -60 && swapUp()){    //swap up
                    starty -= 60;
                    DragSwap(y);
                }
            }
        }
    }
}
