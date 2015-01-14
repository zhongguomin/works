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
 * @author nova ���� 2013��1��25��15:27:54 ����Ч��
 */
public class ViewAnimation extends Animation {

	private int mCenterX;// ��¼View���м�����
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
		// ��ʼ���м�����ֵ
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
