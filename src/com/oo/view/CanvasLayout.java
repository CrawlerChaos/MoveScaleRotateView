package com.oo.view;

import com.oo.movescaleviewdemo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsoluteLayout;

@SuppressWarnings("deprecation")
public class CanvasLayout extends AbsoluteLayout{
	
	private MoveScaleRotateView moveScaleView;
    public MoveScaleRotateView onFocusView;
    private static final int MIN_MARGIN_DP = 30;
    private Context context;

	public CanvasLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	private Bitmap scaleBitmap(View parent,Bitmap bitmap,Context context){
    	float density = context.getResources().getDisplayMetrics().density;
    	int maxWidth = (int)(parent.getWidth() - density * (MIN_MARGIN_DP * 2 + MoveScaleRotateView.IMAGE_MARGIN * 2 + MoveScaleRotateView.BUTTON_SIZE));
    	int maxHeigth = (int)(parent.getHeight() - density * (MIN_MARGIN_DP * 2 + MoveScaleRotateView.IMAGE_MARGIN * 2 + MoveScaleRotateView.BUTTON_SIZE));
    	if(maxWidth < bitmap.getWidth() || maxHeigth < bitmap.getHeight()){
    		float widthRatio = bitmap.getWidth() * 1.0f / maxWidth;
    		float heightRatio = bitmap.getHeight() * 1.0f / maxHeigth ;
    		float ratio =  widthRatio > heightRatio ? widthRatio : heightRatio;
			bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth() / ratio), (int)(bitmap.getHeight() / ratio), true);
    	}
    	return bitmap;
    }
    
    public void addMSRView(Bitmap bitmap) {
    	if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defalut_img);
		}
    	bitmap = scaleBitmap(this, bitmap, context);
        moveScaleView = new MoveScaleRotateView(context,this);
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
        
        layoutParams.x = (getWidth() - viewWidth) / 2;
        layoutParams.y = (getHeight() - viewHeight) / 2;
        addView(moveScaleView);
}
	
}
