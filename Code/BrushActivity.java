package com.example.mydoodle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;

public class BrushActivity extends AppCompatActivity {

    private Brush brush = MainActivity.brush;
    private SeekBar sizeSlider, hardnessSlider;
    private EditText sizeET, hardnessET;
    private RadioButton circleRB, squareRB;
    private CheckBox eraserCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush);

        sizeET = (EditText)findViewById(R.id.sizeET);
        sizeET.setText(brush.getSize() + "");
        sizeSlider = (SeekBar)findViewById(R.id.sizeSlider);
        sizeSlider.setProgress(brush.getSize());

        hardnessET = (EditText)findViewById(R.id.hardnessET);
        hardnessET.setText(brush.getHardness() + "");
        hardnessSlider = (SeekBar)findViewById(R.id.hardnessSlider);
        hardnessSlider.setProgress(brush.getHardness());

        circleRB = (RadioButton)findViewById(R.id.circleRB);
        circleRB.setChecked(brush.isCircle());
        squareRB = (RadioButton)findViewById(R.id.squareRB);
        squareRB.setChecked(brush.isSquare());

        eraserCB = (CheckBox)findViewById(R.id.eraserCB);
        eraserCB.setChecked(brush.isEraser());

        ChangeHandler sizeHandler = new ChangeHandler(sizeET, sizeSlider);
        sizeET.addTextChangedListener(sizeHandler);
        sizeSlider.setOnSeekBarChangeListener(sizeHandler);

        ChangeHandler hardnessHandler = new ChangeHandler(hardnessET, hardnessSlider);
        hardnessET.addTextChangedListener(hardnessHandler);
        hardnessSlider.setOnSeekBarChangeListener(hardnessHandler);
    }

    //updates brush
    private void UpdateBrush()
    {
        brush.setSize(sizeSlider.getProgress());
        brush.setHardness(hardnessSlider.getProgress());
        brush.setCircle(circleRB.isChecked());
        brush.setEraser(eraserCB.isChecked());
    }

    public void ChangeShape(View v)
    {
        UpdateBrush();
    }

    //updates brush and finish activity
    public void Back(View v)
    {
        UpdateBrush();
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
            seekBar.setProgress((int)EditTextToDouble(et));
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

    private double EditTextToDouble(EditText et)
    {
        try{
            return Double.parseDouble(et.getText().toString());
        }
        catch (Exception e)
        {
            return 0;
        }
    }
}
