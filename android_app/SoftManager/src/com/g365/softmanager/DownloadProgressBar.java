package com.g365.softmanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * �Զ��������
 * 
 * @author nova ���� 2013��1��7��14:24:44
 */
public class DownloadProgressBar extends View {

	/** ����״̬ */
	public static final int DOWNLOAD_PROGRESSBAR_UPDATE = 1;
	/** ��װ״̬ */
	public static final int DOWNLOAD_PROGRESSBAR_INSTALL = 2;
	/** �������ƶ�״̬ */
	public static final int DOWNLOAD_PROGRESSBAR_MOVE = 3;
	/** ����״̬ */
	public static final int DOWNLOAD_PROGRESSBAR_CONTINUE = 4;

	/** Ĭ������״̬ */
	private int state = DOWNLOAD_PROGRESSBAR_UPDATE;

	/** ���� ������ */
	private Bitmap mBackground;
	/** ���� ���� */
	private Bitmap mBackgroundUpdate;
	/** ������װ */
	private Bitmap mBackgroundInstall;

	/** �������� */
	private Bitmap mBackgroundContinue;

	/** ������ */
	private Bitmap mProgress;
	/** ���� */
	private Bitmap mProgressSanjiao;

	/** ���� ������ */
	private BitmapDrawable bdBackground;
	/** ���� ���� */
	private BitmapDrawable bdBackgroundUpdate;
	/** ���� ��װ */
	private BitmapDrawable bdBackgroundInstall;
	/** �������� */
	private BitmapDrawable bdBackgroundContinue;

	/** ������ */
	private BitmapDrawable bdProgress;
	private BitmapDrawable bdProgressSanjiao;

	/** View �Ŀ� */
	private int mWidth;
	/** View �ĸ� */
	private int mHeight;
	/** ���ǵĿ� */
	private int mSanjiaoWidth;
	/** ������ */
	private int mBackgroundWidth;
	/** ������ */
	private int mBackgroundHeight;
	/** ���ƽ���������Դ�� */
	private int mProgressWidth;
	/** �������� */
	private int mProgressHeight;
	/** ��ǰ���ȿ�� */
	private int curProgressWidth;
	/** ����������� */
	private int maxProgressWidth;

	/** ˮƽ�������Ŵ�С */
	private float scaleSizeX = 1.0f;
	/** ��ֱ�������Ŵ�С */
	private float scaleSizeY = 1.0f;

	/** �����ı���ɫ */
	private int backgroundColor = Color.TRANSPARENT;
	/** �������ĵ�ǰ���� */
	private int progress = 0;
	/** �����������ֵ */
	private int maxProgress = 100;

	/** ����һ��������,�ü���������������������ȵĸı� */
	private OnProgressChangeListener listener;

	public DownloadProgressBar(Context context) {
		super(context);
		init(context);
	}

	public DownloadProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

		/* �Ӳ����ļ���ȡ�û�ָ�������� */
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.DownloadProgressBar);

		// ��typeArray��ȡ��Ӧֵ���ڶ�������ΪĬ��ֵ�����һ��������atts.xml��û�ж��壬���صڶ�������ֵ
		progress = typedArray.getInt(R.styleable.DownloadProgressBar_progress,
				0);
		maxProgress = typedArray.getInt(
				R.styleable.DownloadProgressBar_maxProgress, 100);
		backgroundColor = typedArray.getColor(
				R.styleable.DownloadProgressBar_backgroundColor,
				Color.TRANSPARENT);
		typedArray.recycle();
	}

	/*
	 * ���������ÿؼ��Ĵ�С (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/** �õ�ʵ����ʾ�Ŀ�� */
		mWidth = measureSize(widthMeasureSpec, true);
		/** �õ�ʵ����ʾ�ĸ߶� */
		mHeight = measureSize(heightMeasureSpec, false);
		/** ����X������Ҫ���ŵĴ�С */
		scaleSizeX = (float) mWidth / (float) mBackgroundWidth;
		/** ����Y������Ҫ���ŵĴ�С */
		scaleSizeY = (float) mHeight / (float) mBackgroundHeight;
		System.out.println("����1��" + scaleSizeX + " " + scaleSizeY + "\n"
				+ mWidth + " " + mHeight);
		/** ���ÿؼ���С */
		setMeasuredDimension(mWidth, mHeight);
	}

	/**
	 * �����ؼ���С
	 * 
	 * @param measureSpec
	 *            �����Ĵ�С
	 * @param measureWidth
	 *            true��ʾ������false��ʾ������
	 * @return
	 */
	private int measureSize(int measureSpec, boolean measureWidth) {

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 0;
		if (specMode == MeasureSpec.EXACTLY) {// ��С����ȷָ��
			result = specSize;
		} else {
			result = measureWidth ? mBackgroundWidth : mBackgroundHeight;
			if (specMode == MeasureSpec.AT_MOST) {
				result = measureWidth ? Math.min(mBackgroundWidth, specSize)
						: Math.min(mBackgroundHeight, specSize);
			}
		}

		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		// �����Ż�������
		canvas.scale(scaleSizeX, scaleSizeY);
		// ������ɫ
		canvas.drawColor(backgroundColor);

		if (state == DOWNLOAD_PROGRESSBAR_MOVE) {
			// ���������������
			curProgressWidth = (int) ((float) progress * maxProgressWidth / maxProgress);
			// ���ý�������ͼ��С
			bdProgress.setBounds(0, 0, curProgressWidth, mProgressHeight);
			bdProgress.draw(canvas);// ��������
			canvas.save();
			canvas.translate(curProgressWidth, 0);// ˮƽ�ƶ�����
			bdProgressSanjiao.draw(canvas);
			canvas.restore();
			bdBackground.draw(canvas);
			// ��������ť
		} else if (state == DOWNLOAD_PROGRESSBAR_UPDATE) {
			bdBackgroundUpdate.draw(canvas);
		} else if (state == DOWNLOAD_PROGRESSBAR_CONTINUE) {
			bdBackgroundContinue.draw(canvas);
		} else {// ����װ��ť
			bdBackgroundInstall.draw(canvas);
		}
		canvas.restore();
	}

	/**
	 * ��ʼ������
	 * 
	 * @param context
	 *            ������
	 */
	private void init(Context context) {

		// �������ƶ�״̬
		mBackground = BitmapFactory.decodeResource(getResources(),
				R.drawable.progressbar_bg);
		// ����״̬
		mBackgroundUpdate = BitmapFactory.decodeResource(getResources(),
				R.drawable.download_progressbar_update);
		// ��װ״̬
		mBackgroundInstall = BitmapFactory.decodeResource(getResources(),
				R.drawable.download_progressbar_install);
		// ����״̬
		mBackgroundContinue = BitmapFactory.decodeResource(getResources(),
				R.drawable.download_progressbar_continue);
		mProgress = BitmapFactory.decodeResource(getResources(),
				R.drawable.progress);
		mProgressSanjiao = BitmapFactory.decodeResource(getResources(),
				R.drawable.progress_sanjiao);
		// ������
		bdBackground = new BitmapDrawable(mBackground);
		// ����
		bdBackgroundUpdate = new BitmapDrawable(mBackgroundUpdate);
		// ��װ
		bdBackgroundInstall = new BitmapDrawable(mBackgroundInstall);
		// ����
		bdBackgroundContinue = new BitmapDrawable(mBackgroundContinue);
		bdProgress = new BitmapDrawable(mProgress);
		bdProgressSanjiao = new BitmapDrawable(mProgressSanjiao);

		// ��ȡ��Դ�ļ���ͼƬ�Ŀ�
		mBackgroundWidth = bdBackground.getIntrinsicWidth();
		// ��ȡ��Դ�ļ���ͼƬ�ĸ�
		mBackgroundHeight = bdBackground.getIntrinsicHeight();

		// ��ȡ��Դ�ļ���ͼƬ�Ŀ�
		mProgressWidth = bdProgress.getIntrinsicWidth();
		// ��ȡ��Դ�ļ���ͼƬ�ĸ�
		mProgressHeight = bdProgress.getIntrinsicHeight();

		mSanjiaoWidth = bdProgressSanjiao.getIntrinsicWidth();

		mWidth = mBackgroundWidth;
		mHeight = mBackgroundHeight;
		maxProgressWidth = mBackgroundWidth - mSanjiaoWidth;

		bdBackground.setBounds(0, 0, mBackgroundWidth, mBackgroundHeight);
		bdBackgroundUpdate.setBounds(0, 0, mBackgroundWidth, mBackgroundHeight);
		bdBackgroundInstall
				.setBounds(0, 0, mBackgroundWidth, mBackgroundHeight);
		bdBackgroundContinue.setBounds(0, 0, mBackgroundWidth,
				mBackgroundHeight);
		bdProgressSanjiao.setBounds(0, 0, mSanjiaoWidth, mBackgroundHeight);
	}

	/**
	 * ���ñ�����ɫ
	 * 
	 * @param backgroundColor
	 *            Ҫ���õ���ɫ
	 */
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		invalidate();
	}

	/**
	 * ��õ�ǰ����
	 * 
	 * @return
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * ���õ�ǰ����
	 * 
	 * @param progress
	 *            ����ֵ
	 */
	private void setProgress(int progress) {
		if (progress < 0) {
			this.progress = 0;
		} else if (progress > maxProgress) {
			this.progress = maxProgress;
		} else {
			this.progress = progress;
		}
		if (listener != null) {
			listener.onProgressChanged(this, progress);
		}
		invalidate();
	}

	/**
	 * ���������
	 * 
	 * @return
	 */
	public int getMaxProgress() {
		return maxProgress;
	}

	/**
	 * ����������
	 * 
	 * @return
	 */
	public void setMaxProgress(int maxProgress) {
		if (maxProgress <= 0 || maxProgress > Integer.MAX_VALUE) {
			this.maxProgress = 100;
		} else {
			this.maxProgress = maxProgress;
		}
	}

	/**
	 * ���ý��ȸı������
	 * 
	 * @param listener
	 *            ���ȸı������
	 */
	public void setOnProgressChangeListener(OnProgressChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * ����״̬�ͽ���
	 * 
	 * @param state
	 *            ״̬
	 * @param progress
	 *            ����
	 */
	public void setState(int state, int progress) {

		this.state = state;
		if (state == DOWNLOAD_PROGRESSBAR_MOVE) {
			setProgress(progress);
		} else {
			/** �ػ� */
			invalidate();
		}
	}

	public interface OnProgressChangeListener {

		/**
		 * ���ȸı�
		 * 
		 * @param downloadProgressBar
		 *            ��������DownloadProgressBar
		 * @param progress
		 *            ��ǰ����
		 */
		public void onProgressChanged(DownloadProgressBar downloadProgressBar,
				int progress);

	}
}
