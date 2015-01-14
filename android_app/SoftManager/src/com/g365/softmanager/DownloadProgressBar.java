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
 * 自定义进度条
 * 
 * @author nova 日期 2013年1月7日14:24:44
 */
public class DownloadProgressBar extends View {

	/** 升级状态 */
	public static final int DOWNLOAD_PROGRESSBAR_UPDATE = 1;
	/** 安装状态 */
	public static final int DOWNLOAD_PROGRESSBAR_INSTALL = 2;
	/** 进度条移动状态 */
	public static final int DOWNLOAD_PROGRESSBAR_MOVE = 3;
	/** 继续状态 */
	public static final int DOWNLOAD_PROGRESSBAR_CONTINUE = 4;

	/** 默认升级状态 */
	private int state = DOWNLOAD_PROGRESSBAR_UPDATE;

	/** 背景 进度条 */
	private Bitmap mBackground;
	/** 背景 升级 */
	private Bitmap mBackgroundUpdate;
	/** 背景安装 */
	private Bitmap mBackgroundInstall;

	/** 背景继续 */
	private Bitmap mBackgroundContinue;

	/** 进度条 */
	private Bitmap mProgress;
	/** 三角 */
	private Bitmap mProgressSanjiao;

	/** 背景 进度条 */
	private BitmapDrawable bdBackground;
	/** 背景 升级 */
	private BitmapDrawable bdBackgroundUpdate;
	/** 背景 安装 */
	private BitmapDrawable bdBackgroundInstall;
	/** 背景继续 */
	private BitmapDrawable bdBackgroundContinue;

	/** 进度条 */
	private BitmapDrawable bdProgress;
	private BitmapDrawable bdProgressSanjiao;

	/** View 的宽 */
	private int mWidth;
	/** View 的高 */
	private int mHeight;
	/** 三角的宽 */
	private int mSanjiaoWidth;
	/** 背景宽 */
	private int mBackgroundWidth;
	/** 背景高 */
	private int mBackgroundHeight;
	/** 绘制进度条的资源宽 */
	private int mProgressWidth;
	/** 进度条高 */
	private int mProgressHeight;
	/** 当前进度宽度 */
	private int curProgressWidth;
	/** 进度条最大宽度 */
	private int maxProgressWidth;

	/** 水平方向缩放大小 */
	private float scaleSizeX = 1.0f;
	/** 垂直方向缩放大小 */
	private float scaleSizeY = 1.0f;

	/** 画布的背景色 */
	private int backgroundColor = Color.TRANSPARENT;
	/** 进度条的当前进度 */
	private int progress = 0;
	/** 进度条的最大值 */
	private int maxProgress = 100;

	/** 定义一个监听器,该监听器负责监听进度条进度的改变 */
	private OnProgressChangeListener listener;

	public DownloadProgressBar(Context context) {
		super(context);
		init(context);
	}

	public DownloadProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

		/* 从布局文件获取用户指定的属性 */
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.DownloadProgressBar);

		// 从typeArray获取相应值，第二个参数为默认值，如第一个参数在atts.xml中没有定义，返回第二个参数值
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
	 * 测量并设置控件的大小 (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		/** 得到实际显示的宽度 */
		mWidth = measureSize(widthMeasureSpec, true);
		/** 得到实际显示的高度 */
		mHeight = measureSize(heightMeasureSpec, false);
		/** 计算X方向需要缩放的大小 */
		scaleSizeX = (float) mWidth / (float) mBackgroundWidth;
		/** 计算Y方向需要缩放的大小 */
		scaleSizeY = (float) mHeight / (float) mBackgroundHeight;
		System.out.println("缩放1：" + scaleSizeX + " " + scaleSizeY + "\n"
				+ mWidth + " " + mHeight);
		/** 设置控件大小 */
		setMeasuredDimension(mWidth, mHeight);
	}

	/**
	 * 测量控件大小
	 * 
	 * @param measureSpec
	 *            测量的大小
	 * @param measureWidth
	 *            true表示测量宽，false表示测量高
	 * @return
	 */
	private int measureSize(int measureSpec, boolean measureWidth) {

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 0;
		if (specMode == MeasureSpec.EXACTLY) {// 大小被精确指定
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
		// 先缩放画布比例
		canvas.scale(scaleSizeX, scaleSizeY);
		// 画背景色
		canvas.drawColor(backgroundColor);

		if (state == DOWNLOAD_PROGRESSBAR_MOVE) {
			// 计算出进度条长度
			curProgressWidth = (int) ((float) progress * maxProgressWidth / maxProgress);
			// 设置进度条画图大小
			bdProgress.setBounds(0, 0, curProgressWidth, mProgressHeight);
			bdProgress.draw(canvas);// 画进度条
			canvas.save();
			canvas.translate(curProgressWidth, 0);// 水平移动画布
			bdProgressSanjiao.draw(canvas);
			canvas.restore();
			bdBackground.draw(canvas);
			// 画升级按钮
		} else if (state == DOWNLOAD_PROGRESSBAR_UPDATE) {
			bdBackgroundUpdate.draw(canvas);
		} else if (state == DOWNLOAD_PROGRESSBAR_CONTINUE) {
			bdBackgroundContinue.draw(canvas);
		} else {// 画安装按钮
			bdBackgroundInstall.draw(canvas);
		}
		canvas.restore();
	}

	/**
	 * 初始化参数
	 * 
	 * @param context
	 *            上下文
	 */
	private void init(Context context) {

		// 进度条移动状态
		mBackground = BitmapFactory.decodeResource(getResources(),
				R.drawable.progressbar_bg);
		// 升级状态
		mBackgroundUpdate = BitmapFactory.decodeResource(getResources(),
				R.drawable.download_progressbar_update);
		// 安装状态
		mBackgroundInstall = BitmapFactory.decodeResource(getResources(),
				R.drawable.download_progressbar_install);
		// 继续状态
		mBackgroundContinue = BitmapFactory.decodeResource(getResources(),
				R.drawable.download_progressbar_continue);
		mProgress = BitmapFactory.decodeResource(getResources(),
				R.drawable.progress);
		mProgressSanjiao = BitmapFactory.decodeResource(getResources(),
				R.drawable.progress_sanjiao);
		// 进度条
		bdBackground = new BitmapDrawable(mBackground);
		// 升级
		bdBackgroundUpdate = new BitmapDrawable(mBackgroundUpdate);
		// 安装
		bdBackgroundInstall = new BitmapDrawable(mBackgroundInstall);
		// 继续
		bdBackgroundContinue = new BitmapDrawable(mBackgroundContinue);
		bdProgress = new BitmapDrawable(mProgress);
		bdProgressSanjiao = new BitmapDrawable(mProgressSanjiao);

		// 获取资源文件中图片的宽
		mBackgroundWidth = bdBackground.getIntrinsicWidth();
		// 获取资源文件中图片的高
		mBackgroundHeight = bdBackground.getIntrinsicHeight();

		// 获取资源文件中图片的宽
		mProgressWidth = bdProgress.getIntrinsicWidth();
		// 获取资源文件中图片的高
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
	 * 设置背景颜色
	 * 
	 * @param backgroundColor
	 *            要设置的颜色
	 */
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		invalidate();
	}

	/**
	 * 获得当前进度
	 * 
	 * @return
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * 设置当前进度
	 * 
	 * @param progress
	 *            进度值
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
	 * 获得最大进度
	 * 
	 * @return
	 */
	public int getMaxProgress() {
		return maxProgress;
	}

	/**
	 * 设置最大进度
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
	 * 设置进度改变监听器
	 * 
	 * @param listener
	 *            进度改变监听器
	 */
	public void setOnProgressChangeListener(OnProgressChangeListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置状态和进度
	 * 
	 * @param state
	 *            状态
	 * @param progress
	 *            进度
	 */
	public void setState(int state, int progress) {

		this.state = state;
		if (state == DOWNLOAD_PROGRESSBAR_MOVE) {
			setProgress(progress);
		} else {
			/** 重绘 */
			invalidate();
		}
	}

	public interface OnProgressChangeListener {

		/**
		 * 进度改变
		 * 
		 * @param downloadProgressBar
		 *            被操作的DownloadProgressBar
		 * @param progress
		 *            当前进度
		 */
		public void onProgressChanged(DownloadProgressBar downloadProgressBar,
				int progress);

	}
}
