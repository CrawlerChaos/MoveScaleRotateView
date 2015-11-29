package com.oo.movescaleviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.oo.view.CanvasLayout;

public class MainActivity extends Activity {
    public CanvasLayout canvas_layout;
    private Button addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvas_layout = (CanvasLayout)findViewById(R.id.canvas_layout);
        addImage = (Button)findViewById(R.id.addImage);
        addImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				canvas_layout.addMSRView(null);
			}
		});
    }
}
