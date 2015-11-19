package com.oo.movescaleviewdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;

import com.oo.view.MoveScaleView;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	private MoveScaleView moveScaleView;
    public MoveScaleView onFocusView;
    public AbsoluteLayout canvas_layout;
    private Button addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvas_layout = (AbsoluteLayout)findViewById(R.id.canvas_layout);
        addImage = (Button)findViewById(R.id.addImage);
        addImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				addView(null);
			}
		});
    }
    
    void addView(Bitmap bitmap) {
        moveScaleView = new MoveScaleView(this);
        if (onFocusView != null) {
            onFocusView.unfocus();
            onFocusView = null;
        }
        onFocusView = moveScaleView;
		AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT, 0, 0);

        moveScaleView.setLayoutParams(layoutParams);
        moveScaleView.setImage(bitmap);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        moveScaleView.measure(w, h);
        int viewHeight = moveScaleView.getMeasuredHeight();
        int viewWidth = moveScaleView.getMeasuredWidth();
        layoutParams.x = (canvas_layout.getWidth() - viewWidth) / 2;
        layoutParams.y = (canvas_layout.getHeight() - viewHeight) / 2;
        canvas_layout.removeAllViews();
        canvas_layout.addView(moveScaleView);
}
}
