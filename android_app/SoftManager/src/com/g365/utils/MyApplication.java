package com.g365.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;

/***
 * 
 * @author nova
 * 软件搬家工具类
 *  日期 2013年1月5日16:02:03
 */
public class MyApplication {

	public static final int MODE_SYSTEM = 0;// 系统程序
	public static final int MODE_APP = 1;// 第三方程序

	public static final int SOURCE_SD = 0;// SDcard
	public static final int SOURCE_SYSTEM = 1;// 系统

	public static final int SOFT_CACHE_SIZE = 1024 * 1024;//缓存 1MB

	/**
	 * sdk版本
	 */
	public static final int SDK16 = 4;
	public static final int SDK20 = 5;
	public static final int SDK22 = 8;
	public static final int SDK23 = 9;
	public static final int SDK233 = 10;
	public static final int SDK40 = 15;


	@SuppressWarnings("unused")
	private static final String SCHEME = "package";
	private static final String APP_PKG_ACTION_23 = "android.settings.APPLICATION_DETAILS_SETTINGS";
	/**
	* 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
	*/ 
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	/**
	* 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
	*/ 
	private static final String APP_PKG_NAME_22 = "pkg";
	/**
	* InstalledAppDetails所在包名
	*/ 
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	/**
	* InstalledAppDetails类名
	*/ 
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";

	
	public static final int ApplicationInfo_FLAG_EXTERNAL_STORAGE = 1 << 18;
	public static final int ApplicationInfo_FLAG_FORWARD_LOCK = 1 << 29;

	/**　preferExternal:
	 * 将程序安装在外部存储介质上,但是系统不保证程序一定会被安装到外部存储介质上.
	 * 当外部存储介质不可以或空时,程序将被安装到内存中.程序使用了for　　　　　　
	 * ward-locking机制时也将被安装到内存中,因为外部存储不支持此机制.
	 * 程序安装后,用户可以自由切换程序应该在外部还是内部存储介质上.
	 */
	public static final int PackageInfo_INSTALL_LOCATION_PREFER_EXTERNAL = 2;
	
	/**auto:
	 * 程序可能被安装在外部存储介质上(例如:SD Card),但是默认会被安装到手机内存中.
	 * 当手机内存为空时,程序将被安装到外部存储介质上.
	 * 当程序安装到手机上后,用户可以决定把程序放在外部储介质还是内存中.
	 */
	
	public static final int PackageInfo_INSTALL_LOCATION_AUTO = 0;
	/**
	 * internalOnly: 默认值.当设置为该值时,程序只能被安装在内存中,如果内存为空,则程序将不能成功安装.
	 */
	
	public static final int PackageInfo_INSTALL_LOCATION_UNSPECIFIED = -1;

	public static final int PackageHelper_APP_INSTALL_EXTERNAL = 2;
	
	
	public static int getSDKINT() {
		final int apiLevel = Build.VERSION.SDK_INT;
		return apiLevel;
	}

	
	/**
	 * 判断应用是否能转移数据
	 * 应用程序是否支持搬家
	 */
	public static boolean checkDataSource(Context context, PackageInfo p) {
		boolean canBe = false;
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel < SDK22) {
			return canBe;
		}
		try {
			int installLocation = PackageInfo_INSTALL_LOCATION_AUTO;
			//反射机制
			Field appInfoFld;
			if (apiLevel < SDK23) {
				appInfoFld = PackageInfo.class.getField("installLocation");
				installLocation = appInfoFld.getInt(p);
			} else {
				appInfoFld = ApplicationInfo.class.getField("installLocation");
				installLocation = appInfoFld.getInt(p.applicationInfo);
			}
			//在sdcard上
			if ((p.applicationInfo.flags & ApplicationInfo_FLAG_EXTERNAL_STORAGE) != 0) {
				canBe = true;
			} else {
				if ((p.applicationInfo.flags & ApplicationInfo_FLAG_FORWARD_LOCK) == 0
						&& (p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					if (installLocation == PackageInfo_INSTALL_LOCATION_PREFER_EXTERNAL
							|| installLocation == PackageInfo_INSTALL_LOCATION_AUTO) {
						canBe = true;
					} else if (installLocation == PackageInfo_INSTALL_LOCATION_UNSPECIFIED) {
						// if (mInstallLocation ==
						// PackageHelper_APP_INSTALL_EXTERNAL) {
						// // For apps with no preference and the default value
						// // set
						// // to install on sdcard.
						// canBe = true;
						// }
					}
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		return canBe;
	}

	/**
	 * 获取数据存在未知
	 * 
	 */
	public static int getDataSource(Context context, PackageInfo p) {

		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= SDK22) {
			if (checkDataSource(context, p)) {
				if ((p.applicationInfo.flags & ApplicationInfo_FLAG_EXTERNAL_STORAGE) != 0) {
					return SOURCE_SD;
				} else {
					return SOURCE_SYSTEM;
				}
			}
		}
		return -1;
	}
	
	/**
	 * 程序数据转移到SDcard上
	 * 
	 */
	public static void showInstalledAppDetails(Context context,
			String packageName) {
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= SDK23) {
			// 2.3（ApiLevel 9）以上，使用SDK提供的接口
			intent.setAction(APP_PKG_ACTION_23);
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);
		} else {
			// 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
			// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
		
			String appPkgName = (apiLevel == SDK22 ? APP_PKG_NAME_22
					: APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME,
					APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
		context.startActivity(intent);
	}

	/**
	 * 获取系统SDK的版本号
	 * 
	 */
	public static int getSdkVersion() {

		int sdkVersion = 0;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

	
	/**
	 * 获取未安装包的图标
	 * 
	 */
	public static Drawable showUninstallAPKIcon(PackageManager pm,
			ApplicationInfo appInfo, Context context, String apkPath) {

		int sdkVersion = getSdkVersion();
		if (sdkVersion < SDK22) {
			return pm.getApplicationIcon(appInfo);
		}

		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		try {
			// apk包的文件路径
			// 这是一个Package 解释器, 是隐藏的
			// 构造函数的参数只有一个, apk文件的路径
			// PackageParser packageParser = new PackageParser(apkPath);
			Class pkgParserCls = Class.forName(PATH_PackageParser);
			Class[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			// 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			// PackageParser.Package mPkgInfo = packageParser.parsePackage(new
			// File(apkPath), apkPath,
			// metrics, 0);
			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);
			valueArgs = new Object[4];
			valueArgs[0] = new File(apkPath);
			valueArgs[1] = apkPath;
			valueArgs[2] = metrics;
			valueArgs[3] = 0;
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);
			// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
			// ApplicationInfo info = mPkgInfo.applicationInfo;
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);
			// Resources pRes = getResources();
			// AssetManager assmgr = new AssetManager();
			// assmgr.addAssetPath(apkPath);
			// Resources res = new Resources(assmgr, pRes.getDisplayMetrics(),
			// pRes.getConfiguration());
			Class assetMagCls = Class.forName(PATH_AssetManager);
			Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
			Object assetMag = assetMagCt.newInstance((Object[]) null);
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
			Resources res = context.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor resCt = Resources.class.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = (Resources) resCt.newInstance(valueArgs);
			CharSequence label = null;
			if (info.labelRes != 0) {
				label = res.getText(info.labelRes);
			}
			// if (label == null) {
			// label = (info.nonLocalizedLabel != null) ? info.nonLocalizedLabel
			// : info.packageName;
			// }
			// 这里就是读取一个apk程序的图标
			if (info.icon != 0) {
				Drawable icon = res.getDrawable(info.icon);
				return icon;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	/**
	 * 获取未安装包的程序名称
	 * 
	 */
	public static String showUninstallAPKLabel(PackageManager pm,
			ApplicationInfo appInfo, Context context, String apkPath) {

		int sdkVersion = getSdkVersion();
		if (sdkVersion < SDK22) {
			return pm.getApplicationLabel(appInfo).toString();
		}

		String PATH_PackageParser = "android.content.pm.PackageParser";
		String PATH_AssetManager = "android.content.res.AssetManager";
		try {
			// apk包的文件路径
			// 这是一个Package 解释器, 是隐藏的
			// 构造函数的参数只有一个, apk文件的路径
			// PackageParser packageParser = new PackageParser(apkPath);
			Class pkgParserCls = Class.forName(PATH_PackageParser);
			Class[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			// 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			// PackageParser.Package mPkgInfo = packageParser.parsePackage(new
			// File(apkPath), apkPath,
			// metrics, 0);
			typeArgs = new Class[4];
			typeArgs[0] = File.class;
			typeArgs[1] = String.class;
			typeArgs[2] = DisplayMetrics.class;
			typeArgs[3] = Integer.TYPE;
			Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
					"parsePackage", typeArgs);
			valueArgs = new Object[4];
			valueArgs[0] = new File(apkPath);
			valueArgs[1] = apkPath;
			valueArgs[2] = metrics;
			valueArgs[3] = 0;
			Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
					valueArgs);
			// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
			// ApplicationInfo info = mPkgInfo.applicationInfo;
			Field appInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"applicationInfo");
			ApplicationInfo info = (ApplicationInfo) appInfoFld
					.get(pkgParserPkg);
			// Resources pRes = getResources();
			// AssetManager assmgr = new AssetManager();
			// assmgr.addAssetPath(apkPath);
			// Resources res = new Resources(assmgr, pRes.getDisplayMetrics(),
			// pRes.getConfiguration());
			Class assetMagCls = Class.forName(PATH_AssetManager);
			Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
			Object assetMag = assetMagCt.newInstance((Object[]) null);
			typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod(
					"addAssetPath", typeArgs);
			valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
			Resources res = context.getResources();
			typeArgs = new Class[3];
			typeArgs[0] = assetMag.getClass();
			typeArgs[1] = res.getDisplayMetrics().getClass();
			typeArgs[2] = res.getConfiguration().getClass();
			Constructor resCt = Resources.class.getConstructor(typeArgs);
			valueArgs = new Object[3];
			valueArgs[0] = assetMag;
			valueArgs[1] = res.getDisplayMetrics();
			valueArgs[2] = res.getConfiguration();
			res = (Resources) resCt.newInstance(valueArgs);
			String label = null;
			if (info.labelRes != 0) {
				label = res.getText(info.labelRes).toString();
			}
			if (label == null) {
				label = (info.nonLocalizedLabel != null) ? info.nonLocalizedLabel
						.toString()
						: info.packageName.toString();
			}

			return label;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	/**
	 * 终止程序运行
	 * 
	 */
	public static void KillProcess(Context context, String packageName) {
		try {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);

			int sdkVersion = getSdkVersion();
			if (sdkVersion >= SDK22) {
				try {
					Method method = ActivityManager.class.getMethod(
							"killBackgroundProcesses", String.class);
					method.invoke(activityManager, packageName);
				} catch (Exception e) {
					try {
						Method method = ActivityManager.class.getMethod(
								"restartPackage", String.class);
						method.invoke(activityManager, packageName);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} else {
				activityManager.restartPackage(packageName);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	
	/**
	 * 终止自身程序运行
	 * 
	 */
	public static void KillMyProcess(Context context) {
		try {
			KillProcess(context, context.getPackageName());
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * 获取正在运行的程序
	 * 
	 */
	public static ArrayList<String> GetNewRunningAppProcessInformation(
			Context context) {

		ArrayList<String> list = new ArrayList<String>();

		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> mRunningAppProcess = mActivityManager
				.getRunningAppProcesses();

		// Get Package Information through PackageManager
		PackageManager mPackageManager = context.getPackageManager();

		int size = mRunningAppProcess.size();

		for (int i = 0; i < size; i++) {
			ActivityManager.RunningAppProcessInfo mRunningProcess = mRunningAppProcess
					.get(i);
			// if (mRunningProcess.importance ==
			// RunningAppProcessInfo.IMPORTANCE_SERVICE) {
			// continue;
			// }
			String packageName = "";

			PackageInfo mPackageInformation = null;
			try {
				mPackageInformation = mPackageManager.getPackageInfo(
						mRunningProcess.processName, 0);
				packageName = mPackageInformation.applicationInfo.packageName;
			} catch (Exception e) {
				packageName = mRunningProcess.processName;
			}
			if (!packageName.endsWith(context.getPackageName()))
				list.add(packageName);
			// mApplicationPID.add(mInfo.mPid);
		}

		return list;
	}
	
	public static String[] GetRunningAppProcessInformation(Context context) {

		String[] running = null;
		try {
			ActivityManager mActivityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> mRunningAppProcess = mActivityManager
					.getRunningAppProcesses();
			int size = mRunningAppProcess.size();
			running = new String[size];
			for (int i = 0; i < size; i++) {
				ActivityManager.RunningAppProcessInfo mRunningProcess = mRunningAppProcess
						.get(i);
				// if (mRunningProcess.importance ==
				// RunningAppProcessInfo.IMPORTANCE_SERVICE) {
				// continue;
				// }
				running[i] = mRunningProcess.pid + "："
						+ mRunningProcess.processName;
			}

		} catch (Exception e) {

		}
		return running;

	}
	
	
}
