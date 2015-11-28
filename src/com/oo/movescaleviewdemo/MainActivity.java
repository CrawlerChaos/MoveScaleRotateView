package com.oo.movescaleviewdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    
    private static final int MIN_MARGIN_DP = 30;
    
    
    private Bitmap scaleBitmap(View parent,Bitmap bitmap,Context context){
    	float density = context.getResources().getDisplayMetrics().density;
    	int maxWidth = (int)(parent.getWidth() - density * (MIN_MARGIN_DP * 2 + MoveScaleView.IMAGE_MARGIN * 2 + MoveScaleView.BUTTON_SIZE));
    	int maxHeigth = (int)(parent.getHeight() - density * (MIN_MARGIN_DP * 2 + MoveScaleView.IMAGE_MARGIN * 2 + MoveScaleView.BUTTON_SIZE));
    	if(maxWidth < bitmap.getWidth() || maxHeigth < bitmap.getHeight()){
    		float widthRatio = bitmap.getWidth() * 1.0f / maxWidth;
    		float heightRatio = bitmap.getHeight() * 1.0f / maxHeigth ;
    		float ratio =  widthRatio > heightRatio ? widthRatio : heightRatio;
			bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth() / ratio), (int)(bitmap.getHeight() / ratio), true);
    	}
    	return bitmap;
    }
    
    void addView(Bitmap bitmap) {
    	if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defalut_img);
		}
    	bitmap = scaleBitmap(canvas_layout, bitmap, this);
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
