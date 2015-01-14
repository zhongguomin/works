package comns.system;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

/**
 * @类名: CustomAnimation
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-19 上午12:22:28
 * 
 * @描述: 类<code>CustomAnimation</code>是自定动画相关的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class CustomAnimation {

	/** 默认动画时间 */
	public static final long DEFAULT_DURATION = 2000;
	/** 默认移动动画 */
	public static final int MODE_DEFAULT_TRANSLATE = 0;
	/** 默认旋转动画 */
	public static final int MODE_DEFAULT_ROTATE = 1;
	/** 默认大小变化动画 */
	public static final int MODE_DEFAULT_SCALE = 2;
	/** 默认透明度变化动画 */
	public static final int MODE_DEFAULT_ALPHA = 3;

	/**
	 * 获取View的动画
	 * 
	 * @param view
	 *            只有旋转动画需要，其它可指定为 null
	 * @param mode 动画类型
	 * @return
	 */
	public static AnimationSet getAnimationSet(View view, int mode) {

		AnimationSet animationSet = new AnimationSet(true);

		Animation animation = null;
		switch (mode) {
		case MODE_DEFAULT_TRANSLATE:
			animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
					-1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			break;
		case MODE_DEFAULT_ROTATE:
			animation = new Rotate3dAnimation(0.0f, 360.0f,
					view.getWidth() / 2, view.getHeight() / 2, 1000, false);
			break;
		case MODE_DEFAULT_SCALE:
			animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			break;
		default:
			animation = new AlphaAnimation(0.0f, 1.0f);
			break;
		}
		animation.setDuration(DEFAULT_DURATION);
		animation.setFillAfter(false);
		animationSet.addAnimation(animation);

		return animationSet;
	}

	/**
	 * @类名: Rotate3dAnimation
	 * 
	 * @作者: ChellyChi
	 * 
	 * @版本: V1.0
	 * 
	 * @日期: 2012-11-19 上午11:27:07
	 * 
	 * @描述: 类<code>Rotate3dAnimation</code>是在 Z轴平移并围绕 Y轴转动的动画</p>
	 * 
	 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
	 * 
	 *      该源码允许添加、删除和修改。
	 * 
	 * 
	 */
	public static class Rotate3dAnimation extends Animation {

		/** 开始角度 */
		private final float mFromDegrees;
		/** 结束角度 */
		private final float mToDegrees;
		/** 中心点的 X坐标 */
		private final float mCenterX;
		/** 中心点的 Y坐标 */
		private final float mCenterY;
		/** Z轴初始位置 */
		private final float mDepthZ;
		/** 是否需要扭曲 */
		private final boolean mReverse;
		//
		/** 摄像头 */
		private Camera mCamera;

		/**
		 * @param fromDegrees
		 *            起始角度
		 * @param toDegrees
		 *            终止角度
		 * @param centerX
		 *            中心点 X坐标
		 * @param centerY
		 *            中心点 Y坐标
		 * @param depthZ
		 *            Z轴初始偏移距离
		 * @param reverse
		 *            是否由里向外翻转
		 */
		public Rotate3dAnimation(float fromDegrees, float toDegrees,
				float centerX, float centerY, float depthZ, boolean reverse) {

			mFromDegrees = fromDegrees;
			mToDegrees = toDegrees;
			mCenterX = centerX;
			mCenterY = centerY;
			mDepthZ = depthZ;
			mReverse = reverse;
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);

			mCamera = new Camera();
		}

		// 生成Transformation
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {

			Matrix matrix = t.getMatrix();
			float degrees = mFromDegrees
					+ ((mToDegrees - mFromDegrees) * interpolatedTime);// 生成中间角度

			mCamera.save();
			if (mReverse) {
				mCamera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
			} else {
				mCamera.translate(0.0f, 0.0f, mDepthZ
						* (1.0f - interpolatedTime));
			}
			mCamera.rotateY(degrees);
			mCamera.getMatrix(matrix);// 取得变换后的矩阵
			mCamera.restore();

			matrix.preTranslate(-mCenterX, -mCenterY);
			matrix.postTranslate(mCenterX, mCenterY);
		}
	}

}
