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
 * @����: CustomAnimation
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-19 ����12:22:28
 * 
 * @����: ��<code>CustomAnimation</code>���Զ�������ص���</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class CustomAnimation {

	/** Ĭ�϶���ʱ�� */
	public static final long DEFAULT_DURATION = 2000;
	/** Ĭ���ƶ����� */
	public static final int MODE_DEFAULT_TRANSLATE = 0;
	/** Ĭ����ת���� */
	public static final int MODE_DEFAULT_ROTATE = 1;
	/** Ĭ�ϴ�С�仯���� */
	public static final int MODE_DEFAULT_SCALE = 2;
	/** Ĭ��͸���ȱ仯���� */
	public static final int MODE_DEFAULT_ALPHA = 3;

	/**
	 * ��ȡView�Ķ���
	 * 
	 * @param view
	 *            ֻ����ת������Ҫ��������ָ��Ϊ null
	 * @param mode ��������
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
	 * @����: Rotate3dAnimation
	 * 
	 * @����: ChellyChi
	 * 
	 * @�汾: V1.0
	 * 
	 * @����: 2012-11-19 ����11:27:07
	 * 
	 * @����: ��<code>Rotate3dAnimation</code>���� Z��ƽ�Ʋ�Χ�� Y��ת���Ķ���</p>
	 * 
	 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
	 * 
	 *      ��Դ��������ӡ�ɾ�����޸ġ�
	 * 
	 * 
	 */
	public static class Rotate3dAnimation extends Animation {

		/** ��ʼ�Ƕ� */
		private final float mFromDegrees;
		/** �����Ƕ� */
		private final float mToDegrees;
		/** ���ĵ�� X���� */
		private final float mCenterX;
		/** ���ĵ�� Y���� */
		private final float mCenterY;
		/** Z���ʼλ�� */
		private final float mDepthZ;
		/** �Ƿ���ҪŤ�� */
		private final boolean mReverse;
		//
		/** ����ͷ */
		private Camera mCamera;

		/**
		 * @param fromDegrees
		 *            ��ʼ�Ƕ�
		 * @param toDegrees
		 *            ��ֹ�Ƕ�
		 * @param centerX
		 *            ���ĵ� X����
		 * @param centerY
		 *            ���ĵ� Y����
		 * @param depthZ
		 *            Z���ʼƫ�ƾ���
		 * @param reverse
		 *            �Ƿ��������ⷭת
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

		// ����Transformation
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {

			Matrix matrix = t.getMatrix();
			float degrees = mFromDegrees
					+ ((mToDegrees - mFromDegrees) * interpolatedTime);// �����м�Ƕ�

			mCamera.save();
			if (mReverse) {
				mCamera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
			} else {
				mCamera.translate(0.0f, 0.0f, mDepthZ
						* (1.0f - interpolatedTime));
			}
			mCamera.rotateY(degrees);
			mCamera.getMatrix(matrix);// ȡ�ñ任��ľ���
			mCamera.restore();

			matrix.preTranslate(-mCenterX, -mCenterY);
			matrix.postTranslate(mCenterX, mCenterY);
		}
	}

}
