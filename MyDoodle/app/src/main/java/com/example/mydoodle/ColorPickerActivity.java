package com.example.mydoodle;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

public class ColorPickerActivity extends AppCompatActivity {

    private Brush brush = MainActivity.brush;
    private View colorDisplay;
    private SeekBar redSlider, greenSlider, blueSlider, alphaSlider;
    private EditText redET, greenET, blueET, alphaET;
    private ChangeHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        int color = brush.getColor();

        redSlider = (SeekBar)findViewById(R.id.redSlider);
        redSlider.setProgress(Color.red(color));
        redET = (EditText)findViewById(R.id.redEt);
        redET.setText(Color.red(color) + "");

        handler = new ChangeHandler(redET, redSlider);
        redSlider.setOnSeekBarChangeListener(handler);
        redET.addTextChangedListener(handler);

        greenSlider = (SeekBar)findViewById(R.id.greenSlider);
        greenSlider.setProgress(Color.green(color));
        greenET = (EditText)findViewById(R.id.greenEt);
        greenET.setText(Color.green(color) + "");

        handler = new ChangeHandler(greenET, greenSlider);
        greenSlider.setOnSeekBarChangeListener(handler);
        greenET.addTextChangedListener(handler);

        blueSlider = (SeekBar)findViewById(R.id.blueSlider);
        blueSlider.setProgress(Color.blue(color));
        blueET = (EditText)findViewById(R.id.blueET);
        blueET.setText(Color.blue(color) + "");

        handler = new ChangeHandler(blueET, blueSlider);
        blueSlider.setOnSeekBarChangeListener(handler);
        blueET.addTextChangedListener(handler);

        alphaSlider = (SeekBar)findViewById(R.id.alphaSlider);
        alphaSlider.setProgress(Color.alpha(color));
        alphaET = (EditText)findViewById(R.id.alphaET);
        alphaET.setText(Color.alpha(color) + "");

        handler = new ChangeHandler(alphaET, alphaSlider);
        alphaSlider.setOnSeekBarChangeListener(handler);
        alphaET.addTextChangedListener(handler);

        colorDisplay = (View)findViewById(R.id.colorDisplay);
        UpdateBrush();
    }

    //updates brush
    private void UpdateBrush()
    {
        int r, g, b, a;
        r = redSlider.getProgress();
        g = greenSlider.getProgress();
        b = blueSlider.getProgress();
        a = alphaSlider.getProgress();
        int color = Color.argb(a, r, g, b);

        colorDisplay.setBackgroundColor(color);
        brush.setColor(color);
    }

    public void Back(View v)
    {
        finish();
    }

    private class ChangeHandler implements SeekBar.OnSeekBarChangeListener, TextWatcher
    {
        private EditText et;
        private SeekBar seekBar;

        public ChangeHandler(EditText e, SeekBar s)
        {
            et = e;
            seekBar = s;
        }

        //updates seek bar on edit text changes
        @Override
        public void afterTextChanged(Editable s) {
            seekBar.setProgress(EditTextToInt(et));
            UpdateBrush();
        }

        //updates edit text on seek bar changes
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            et.setText(progress + "");
            int pos = et.getText().length();
            et.setSelection(pos);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }

    private int EditTextToInt(EditText et)
    {
        try{
            return Integer.parseInt(et.getText().toString());
        }
        catch (Exception e)
        {
            return 0;
        }
    }
}
