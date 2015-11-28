package com.oo.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.oo.movescaleviewdemo.MainActivity;
import com.oo.movescaleviewdemo.R;



@SuppressWarnings("deprecation")
public class MoveScaleRotateView extends AbsoluteLayout {
	
	public final static int IMAGE_MARGIN = 10;
	public final static int BUTTON_SIZE = 30;
	private ImageView delete_btn,bias_btn,image;
	private RelativeLayout content_layout;
	private Point center;
	private int location[] = new int[2];
	private LayoutParams move_scale_layout_params,delete_params,bias_params,content_params;
	private RelativeLayout.LayoutParams image_params;
	private DisplayMetrics displayMetrics;
	private int size_btn,image_width,imgae_height,content_margin,image_margin;
	private ButtonTouchLisener buttonTouchLisener;
	private int startX,startY,endX,endY,deltaX,deltaY;
	private float angle,startA,endA;
	private double startR,endR,ratioRY,ratioRX;
	private int moveScaleViewTop,moveScaleViewBottom,moveScaleViewRight,moveScaleViewLeft;
	private int moveScaleWidth,moveScaleHeight;
	private MainActivity context;
	private AbsoluteLayout parent;
	private float anglePauseDelta = 3.0f;
    private boolean isFocus;

	public MoveScaleRotateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public MoveScaleRotateView(Context context) {
		super(context);
		init(context);
	}
	
	
	void init(Context context){
		this.context = (MainActivity)context;
		parent = this.context.canvas_layout;
		content_layout = new RelativeLayout(context);
		image  = new ImageView(context);
		delete_btn  = new ImageView(context);
		bias_btn  = new ImageView(context);
		displayMetrics = new DisplayMetrics();
		angle = 0.0f;
		buttonTouchLisener = new ButtonTouchLisener();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		size_btn = Math.round(BUTTON_SIZE * displayMetrics.density);
		content_margin = Math.round(BUTTON_SIZE * displayMetrics.density / 2);
		image_margin = Math.round(IMAGE_MARGIN * displayMetrics.density);
	}
	
	public void setImage(Bitmap bitmap){
		image_width = bitmap.getWidth();
		imgae_height = bitmap.getHeight();
		move_scale_layout_params = (LayoutParams)this.getLayoutParams();
		move_scale_layout_params.height = size_btn  + imgae_height + image_margin * 2;
		move_scale_layout_params.width = size_btn  + image_width + image_margin * 2;
        ratioRY =   move_scale_layout_params.height / Math.hypot(move_scale_layout_params.width,move_scale_layout_params.height);
        ratioRX =move_scale_layout_params.width / Math.hypot(move_scale_layout_params.width,move_scale_layout_params.height);
		addContent(bitmap);
		addButton();
		setLisener();
		reLocationChildView();
	}
	
	
	void addContent(Bitmap bitmap){
		content_params = new LayoutParams(image_margin * 2 + image_width, image_margin * 2 + imgae_height,content_margin,content_margin);
		image_params = new RelativeLayout.LayoutParams(image_width, imgae_height);
		
		image_params.leftMargin = image_margin;
		image_params.rightMargin = image_margin;
		image_params.topMargin = image_margin;
		image_params.bottomMargin = image_margin;
		image.setImageBitmap(bitmap);
		image.setScaleType(ScaleType.FIT_CENTER);
		content_layout.setBackgroundResource(R.drawable.move_scale_content_border);
		content_layout.addView(image, image_params);
		this.addView(content_layout, content_params);
	}
	
	void addButton(){
		delete_params = new LayoutParams(size_btn, size_btn,0,imgae_height + image_margin * 2);
		bias_params = new LayoutParams(size_btn, size_btn,image_width + image_margin * 2,0);
		
		delete_btn.setImageResource(R.drawable.move_scale_image_delete_btn);
		bias_btn.setImageResource(R.drawable.move_scale_image_bias_btn);
		bias_btn.setLayoutParams(bias_params);
		delete_btn.setLayoutParams(delete_params);
		this.addView(bias_btn);
		this.addView(delete_btn);
	}
	
	void setLisener(){
		bias_btn.setClickable(true);
		image.setClickable(true);
		bias_btn.setOnTouchListener(buttonTouchLisener);
		image.setOnTouchListener(buttonTouchLisener);
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					if (context.onFocusView != MoveScaleRotateView.this || !isFocus) {
                        if (context.onFocusView != null)
						    context.onFocusView.unfocus();
						focus();
					}
			}
		});
		delete_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				parent.removeView(MoveScaleRotateView.this);
			}
		});
	}
	
	void moveOrScale(View view,int scaleX,int scaleY,int translationX,int translationY,float rotation){
		
		if (view == bias_btn) {
			setRotation(getAngleByPauseDelta(rotation));
			if ( moveScaleViewBottom - moveScaleViewTop +scaleY * 2 < size_btn * 2) {
				return ;
			}else {
				layout(moveScaleViewLeft-scaleX, moveScaleViewTop-scaleY, moveScaleViewRight+scaleX, moveScaleViewBottom+scaleY);
			}
		} else if (view == image) {
			layout(moveScaleViewLeft+translationX, moveScaleViewTop+translationY, moveScaleViewRight+translationX, moveScaleViewBottom+translationY);
		}
		reLocationChildView();
	}

	void reLocationChildView(){
		 moveScaleWidth = getWidth();
		 moveScaleHeight = getHeight();
		 delete_btn.layout(0,moveScaleHeight-size_btn,size_btn,moveScaleHeight);
		 bias_btn.layout(moveScaleWidth-size_btn,0,moveScaleWidth,size_btn);
		 content_layout.layout(content_margin, content_margin, moveScaleWidth-content_margin, moveScaleHeight-content_margin);
		 image.layout(image_margin, image_margin, content_layout.getWidth()-image_margin, content_layout.getHeight()-image_margin);
		 
	}
	
	void saveParmas(){
		move_scale_layout_params = (LayoutParams)this.getLayoutParams();
		delete_params = (LayoutParams)delete_btn.getLayoutParams();
		bias_params = (LayoutParams)bias_btn.getLayoutParams();
		content_params = (LayoutParams)content_layout.getLayoutParams();
		image_params = (RelativeLayout.LayoutParams)image.getLayoutParams();
		
		move_scale_layout_params.height = getBottom() - getTop();
		move_scale_layout_params.width = getRight() - getLeft();
		move_scale_layout_params.x = getLeft();
		move_scale_layout_params.y = getTop();
		
		delete_params.width = delete_btn.getRight() - delete_btn.getLeft();
		delete_params.height = delete_btn.getBottom() - delete_btn.getTop();
		delete_params.x = delete_btn.getLeft();
		delete_params.y = delete_btn.getTop();
		
		
		bias_params.width = bias_btn.getRight() - bias_btn.getLeft();
		bias_params.height = bias_btn.getBottom() - bias_btn.getTop();
		bias_params.x = bias_btn.getLeft();
		bias_params.y = bias_btn.getTop();
		
		content_params.width = content_layout.getRight() - content_layout.getLeft();
		content_params.height = content_layout.getBottom() - content_layout.getTop();
		content_params.x = content_layout.getLeft();
		content_params.y = content_layout.getTop();
		
		image_params.width = image.getRight() - image.getLeft();
		image_params.height = image.getBottom() - image.getTop();
		
		
	}
	
	private void setCenter(){
		if (center == null) {
			 center = new Point();
		 }
		 getLocationOnScreen(location);
		 center.x = location[0] + (moveScaleViewRight - moveScaleViewLeft) / 2;
		 center.y = location[1] + (moveScaleViewBottom - moveScaleViewTop) / 2;
	}
	
	public void focus(){
		delete_btn.setVisibility(View.VISIBLE);
		bias_btn.setVisibility(View.VISIBLE);
		content_layout.setBackgroundResource(R.drawable.move_scale_content_border);
		parent.removeView(this);
		parent.addView(this);
		context.onFocusView = this;
        isFocus = true;
		
	}
	
	public void unfocus(){
		delete_btn.setVisibility(View.INVISIBLE);
		bias_btn.setVisibility(View.INVISIBLE);
		content_layout.setBackgroundResource(R.drawable.move_scale_content_no_border);
        isFocus = false;
	}
	
	private class ButtonTouchLisener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			
			if (context.onFocusView != null && context.onFocusView != MoveScaleRotateView.this) {
				return false;
			}
			
			switch (arg1.getAction()) {
			case MotionEvent.ACTION_DOWN:
				moveScaleViewTop = MoveScaleRotateView.this.getTop();
				moveScaleViewBottom = MoveScaleRotateView.this.getBottom();
				moveScaleViewRight = MoveScaleRotateView.this.getRight();
				moveScaleViewLeft = MoveScaleRotateView.this.getLeft();
				setCenter();
                startX = (int)arg1.getRawX();
				startY = (int)arg1.getRawY();
				deltaX = startX - center.x;
				deltaY = startY - center.y;
				startR = Math.hypot(deltaX,deltaY);
				startA = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
				angle = getRotation();
				break;

			case MotionEvent.ACTION_MOVE:
				endX = (int)arg1.getRawX();		
				endY = (int)arg1.getRawY();
				deltaX = endX - center.x;
				deltaY = endY - center.y;
				endA = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
				endR = Math.hypot(deltaX,deltaY);
				deltaX = (int)(ratioRX * (endR - startR));
				deltaY = (int)(ratioRY * (endR - startR));
				moveOrScale(arg0,deltaX,deltaY,endX - startX,endY - startY,endA - startA + angle);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				saveParmas();
				break;
				
			default:
				break;
			}
			return false;
		}
		
	}
	
	private float getAngleByPauseDelta(float delta){
		if (Math.abs(delta % 90) < anglePauseDelta) {
			return delta - delta % 90;
		} else if (Math.abs(delta % 90) > 90 - anglePauseDelta) {
			if (delta > 0) {
				return  ((int)(delta / 90) + 1) * 90;
			} else
				return ((int)(delta / 90) - 1) * 90;
		} else
			return delta;
	}

}
