package com.g365.utils;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * 
 * @author nova 日期 2013年1月25日15:27:54 动画效果
 */
public class ViewAnimation extends Animation {

	private int mCenterX;// 记录View的中间坐标
	private int mCenterY;
	private Camera camera = new Camera();
	private long duration;
	private ImageView imageView_softbg;
	private Context context;

	public ViewAnimation(ImageView imageView, long duration, Context context) {
		this.duration = duration;
		this.imageView_softbg = imageView;  
		this.context = context;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		// 初始化中间坐标值
		mCenterX = 2*width / 5;
		mCenterY = 2*height / 5;
		setDuration(duration);
		setFillAfter(true);
		setInterpolator(new LinearInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final Matrix matrix = t.getMatrix();
		camera.save();
		camera.rotateY(interpolatedTime * 2 * 90);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-mCenterX, -mCenterY);
		matrix.postTranslate(mCenterX, mCenterY);
	}
	
	
}
