package com.example.test1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

class VolumeControlView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener{

    private double angle = 0.0;
    private KnobListener listener;
    float x,y;
    float mx,my;


    public interface KnobListener {
         void onChanged(double angle);
    }

    public void setKnobListener(KnobListener lis){
        listener = lis;
    }

    public VolumeControlView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setImageResource(R.drawable.knob);
        this.setOnTouchListener(this);
    }

    private double getAngle(float x, float y){
        mx = x - (getWidth()/2.0f);
        my = (getHeight()/2.0f) - y;

        double degree = Math.atan2(mx,my) * 180.0/3.141592;
        return degree;
    }

    private double initangle =0.0,prevangle=0.0;
    public boolean onTouch(View v, MotionEvent event){
        x = event.getX(0);
        y = event.getY(0);
        angle = getAngle(x,y);
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            initangle = angle - prevangle;
            angle = prevangle;
        }else {
            angle -= initangle;
            prevangle = angle;
        }
        invalidate();
        listener.onChanged(angle);
        return true;
    }

    protected void onDraw(Canvas c){
        c.save();
        c.rotate((float)angle,getWidth()/2,getHeight()/2);
        super.onDraw(c);
        c.restore();
    }
}

public class MainActivity extends AppCompatActivity {
    TextView text_red,text_green,text_blue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_red= (TextView) findViewById(R.id.textview_red);
        text_green= (TextView) findViewByI