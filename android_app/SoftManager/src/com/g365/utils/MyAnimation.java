package com.g365.utils;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.g365.softmanager.R;

/**
 * 
 * @author nova
 *  ������
 * ���� 2013��2��4��15:21:36
 */
public class MyAnimation {
 
	/** �Ŵ󶯻� */
	private Animation enlargeAnimation;
	/** ��С���� */
	private Animation reduceAnimation;
	/** ��ת */
	private ViewAnimation rotate;
	/** ƽ�ƶ��� */
	private Animation translateAnimation;
	/** ��������*/
	private TextView tv;
    /** ��������ͼ*/
	private ImageView imageView_softbg;
	/** ��ͷͼƬ*/
	private ImageView imageView_arrows;
    /**������ */
	Context c;
	/** ��ͼ��������  */
	AnimationSet animup;
	/** Сͼ �������� */
	private AnimationSet set;
	
	public MyAnimation(Context context, ImageView view, ImageView view1,
			TextView textView, long duration){
		imageView_softbg = view;
		imageView_arrows = view1;
		tv = textView;
		c = context;
		initSuoxiao(duration);
		initTranslate(duration);
		initRotate(view, duration, context);
		initDangda(duration, context);
		initTranslate(duration);
		initAnimationSet(duration);
	}
	
	public void startAnimation(Context context) {
		imageView_softbg.setBackgroundResource(R.drawable.ld_h);
		imageView_softbg.startAnimation(animup);
		imageView_arrows.startAnimation(translateAnimation);
		tv.setAnimation(set);
		set.startNow();
	}
	
	/** ��ͼ�Ŵ� */
	private void initDangda(long duration, final Context context) {
		enlargeAnimation = new ScaleAnimation(0.8f, 1f, 0.8f, 1f,
				ScaleAnimation.RELATIVE_TO_SELF, 200f);
		enlargeAnimation.setFillAfter(true);
		enlargeAnimation.setDuration(duration);
		enlargeAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				
			}
		});
	}
	
	/** ��ͼ��С */
	private void initSuoxiao(long duration) {
		animup = new AnimationSet(true);
		reduceAnimation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f,
				ScaleAnimation.RELATIVE_TO_SELF, 200f);
		reduceAnimation.setFillAfter(true);
		reduceAnimation.setDuration(duration);
		//��С������ӵ�����������
		animup.addAnimation(reduceAnimation);
		rotate = new ViewAnimation(imageView_softbg, duration, c);
		rotate.setDuration(duration);
		rotate.setStartOffset(duration);
		rotate.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
//				imageView_softbg.setBackgroundColor(c.getResources().getColor(
//						R.color.animation));
				imageView_softbg.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.hd_h));
				imageView_softbg.startAnimation(enlargeAnimation);
			}
		});
		//��ת������ӵ�����������
		animup.addAnimation(rotate);
	}

	/**  TextView��϶��� */
	private void initAnimationSet(long duration) {
		set = new AnimationSet(true);
		Animation animation1 = new TranslateAnimation(-20, -16, 0, 0);
		Animation animation2 = new AlphaAnimation(0.3f, 1f);
		Animation animation3 = new ScaleAnimation(0.3f, 1f, 0.3f, 1f, 50f, 50f);
		set.addAnimation(animation1);
		set.addAnimation(animation2);
		set.addAnimation(animation3);
		set.setDuration(duration);
		set.setFillAfter(true);
	}

	/** Сͼƽ��*/
	private void initTranslate(long duration) {
		translateAnimation = new TranslateAnimation(0, -8, 0, 0);
		translateAnimation.setDuration(duration);
		translateAnimation.setFillAfter(true);
	}

	/**
	 * ��ͼ��ת
	 * 
	 * @param linear
	 * @param duration
	 * @param context
	 */
	private void initRotate(final ImageView imageView, long duration,
			final Context context) {

		rotate = new ViewAnimation(imageView, duration, context);
		rotate.setFillAfter(true);
		rotate.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				//��ת����ʾ��ͼƬ
//				imageView.setBackgroundColor(c.getResources().getColor(
//						R.color.animation));
				imageView.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.hd_h));
				//��ת�����������Ŵ�Ч��
				imageView.startAnimation(enlargeAnimation);
			}
		});

	}
}
