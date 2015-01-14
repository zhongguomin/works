//package com.g365.utils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import com.g365.entity.AppInfo;
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.os.Environment;
//
///**
// * 扫描apk的工具类
// * 
// * @author Administrator
// * 
// */
//public class ApkSearchUtils {
//	/**
//	 * 未安装apk
//	 */
//	public static final int APK_UNINSTALL = 0;
//	/**
//	 * 已安装apk
//	 */
//	public static final int APK_INSTALL = 1;
//	/**
//	 * 低版本apk
//	 */
//	public static final int APK_LOWINSTALL = 2;
//	/**
//	 * 高版本apk
//	 */
//	public static final int APK_HIGHINSTALL = 3;
//	/**
//	 * 重复的apk
//	 */
//	public static final int APK_REPEATINSTALL = 4;
//
//	/**
//	 * 文件集合
//	 */
//	private ArrayList<File> list = new ArrayList<File>();
//	private Context context;
//
//	/**
//	 * 构造函数
//	 */
//	public ApkSearchUtils(Context context) {
//		super();
//		this.context = context;
//	}
//
//	/**
//	 * SD卡上的文件目录 运用递归的思想，递归去找每个目录下面的apk文件 得到sd卡所有apk方法
//	 * 
//	 * @param root
//	 */
//	public void getAllFile(File root) {
//		File files[] = root.listFiles();
//		if (files != null)
//			for (File f : files) {
//				if (f.isDirectory()) {
//					System.out.println("----------扫描sd卡--------" + f);
//					getAllFile(f);
//				} else {
//					if (f.getName().indexOf(".apk") > 0)
//
//						this.list.add(f);
//				}
//			}
//
//	}
//
//	public List<AppInfo> getApkList() {
//
//		List<AppInfo> saveapk = new ArrayList<AppInfo>();
//
//		getAllFile(new File(Environment.getExternalStorageDirectory()
//				.toString()));
//		for (File f : list) {
//			AppInfo appInfo = getOneApkInfomaton(f);
//			// 过滤破损的apk信息
//			if (!(appInfo.appSize).equals("")) {
//				saveapk.add(appInfo);
//			}
//
//		}
//
//		return saveapk;
//	}
//
//	/**
//	 * 得到一个apk信息
//	 * 
//	 * @param file
//	 * @return
//	 */
//	public AppInfo getOneApkInfomaton(File file) {
//
//		AppInfo myAppInfo = new AppInfo();
//		PackageManager pm = context.getPackageManager();
//		PackageInfo packageInfo = pm.getPackageArchiveInfo(
//				file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
//		if (packageInfo != null) {
//			ApplicationInfo appInfo = packageInfo.applicationInfo;
//			if (appInfo != null) {
//
//				// 获取sk卡上apk的图标
//				appInfo.sourceDir = file.getAbsolutePath();
//				appInfo.publicSourceDir = file.getAbsolutePath();
//				Drawable icon = appInfo.loadIcon(pm);
//				myAppInfo.setIcon(icon);
//				// 获得应用程序名
//				String appName = appInfo.loadLabel(pm).toString();
//				myAppInfo.setAppName(appName);
//				// 获得包名
//				String packageName = packageInfo.packageName;
//				myAppInfo.setPackageName(packageName);
//				// 获得apk的版本名称
//				String versionName = packageInfo.versionName;
//				myAppInfo.setVersionName(versionName);
//
//				// 获得apk的版本号码
//				int versionCode = packageInfo.versionCode;
//				myAppInfo.setVersionCode(versionCode);
//
//				// 获得应用程序的大小
//				String dir = file.getAbsolutePath();
//				String size = MemoryStatus.formatFileSize(new File(dir)
//						.length());
//				// 获得path存入到实体类
//				myAppInfo.setFilepath(file.getPath());
//
//				myAppInfo.setAppSize(size);
//
//				try {
//					PackageInfo pInfo = pm.getPackageInfo(appInfo.packageName,
//							0);
//					//ApplicationInfo applicationInfo = pInfo.applicationInfo;
//					//已安装
//					if (versionCode == pInfo.versionCode) {
//						myAppInfo.setVersionflag(APK_INSTALL);
//						// 高版本
//					} else if (versionCode > pInfo.versionCode) {
//						myAppInfo.setVersionflag(APK_HIGHINSTALL);
//						// 低版本
//					} else if (versionCode < pInfo.versionCode) {
//						myAppInfo.setVersionflag(APK_LOWINSTALL);
//					}
//
//				} catch (Exception e) {
//					// 未安装
//					myAppInfo.setVersionflag(APK_UNINSTALL);
//				}
//			}
//		}
//
//		return myAppInfo;
//
//	}
//
//	/**
//	 * 检测 APK包是否存在且完整
//	 * 
//	 * @param apkPath
//	 *            APK文件路径
//	 * @return
//	 */
//	public static boolean isApKFileOk(Context context, String apkPath) {
//
//		boolean fileIsOk = false;
//
//		File file = new File(apkPath);
//		if (file.exists()) {
//			try {
//				PackageInfo packageInfo = context.getPackageManager()
//						.getPackageArchiveInfo(apkPath,
//								PackageManager.GET_ACTIVITIES);
//				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//				if (applicationInfo != null) {
//					fileIsOk = true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return fileIsOk;
//	}
//
//}
