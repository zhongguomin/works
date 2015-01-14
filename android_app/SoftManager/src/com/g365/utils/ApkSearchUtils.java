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
// * ɨ��apk�Ĺ�����
// * 
// * @author Administrator
// * 
// */
//public class ApkSearchUtils {
//	/**
//	 * δ��װapk
//	 */
//	public static final int APK_UNINSTALL = 0;
//	/**
//	 * �Ѱ�װapk
//	 */
//	public static final int APK_INSTALL = 1;
//	/**
//	 * �Ͱ汾apk
//	 */
//	public static final int APK_LOWINSTALL = 2;
//	/**
//	 * �߰汾apk
//	 */
//	public static final int APK_HIGHINSTALL = 3;
//	/**
//	 * �ظ���apk
//	 */
//	public static final int APK_REPEATINSTALL = 4;
//
//	/**
//	 * �ļ�����
//	 */
//	private ArrayList<File> list = new ArrayList<File>();
//	private Context context;
//
//	/**
//	 * ���캯��
//	 */
//	public ApkSearchUtils(Context context) {
//		super();
//		this.context = context;
//	}
//
//	/**
//	 * SD���ϵ��ļ�Ŀ¼ ���õݹ��˼�룬�ݹ�ȥ��ÿ��Ŀ¼�����apk�ļ� �õ�sd������apk����
//	 * 
//	 * @param root
//	 */
//	public void getAllFile(File root) {
//		File files[] = root.listFiles();
//		if (files != null)
//			for (File f : files) {
//				if (f.isDirectory()) {
//					System.out.println("----------ɨ��sd��--------" + f);
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
//			// ���������apk��Ϣ
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
//	 * �õ�һ��apk��Ϣ
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
//				// ��ȡsk����apk��ͼ��
//				appInfo.sourceDir = file.getAbsolutePath();
//				appInfo.publicSourceDir = file.getAbsolutePath();
//				Drawable icon = appInfo.loadIcon(pm);
//				myAppInfo.setIcon(icon);
//				// ���Ӧ�ó�����
//				String appName = appInfo.loadLabel(pm).toString();
//				myAppInfo.setAppName(appName);
//				// ��ð���
//				String packageName = packageInfo.packageName;
//				myAppInfo.setPackageName(packageName);
//				// ���apk�İ汾����
//				String versionName = packageInfo.versionName;
//				myAppInfo.setVersionName(versionName);
//
//				// ���apk�İ汾����
//				int versionCode = packageInfo.versionCode;
//				myAppInfo.setVersionCode(versionCode);
//
//				// ���Ӧ�ó���Ĵ�С
//				String dir = file.getAbsolutePath();
//				String size = MemoryStatus.formatFileSize(new File(dir)
//						.length());
//				// ���path���뵽ʵ����
//				myAppInfo.setFilepath(file.getPath());
//
//				myAppInfo.setAppSize(size);
//
//				try {
//					PackageInfo pInfo = pm.getPackageInfo(appInfo.packageName,
//							0);
//					//ApplicationInfo applicationInfo = pInfo.applicationInfo;
//					//�Ѱ�װ
//					if (versionCode == pInfo.versionCode) {
//						myAppInfo.setVersionflag(APK_INSTALL);
//						// �߰汾
//					} else if (versionCode > pInfo.versionCode) {
//						myAppInfo.setVersionflag(APK_HIGHINSTALL);
//						// �Ͱ汾
//					} else if (versionCode < pInfo.versionCode) {
//						myAppInfo.setVersionflag(APK_LOWINSTALL);
//					}
//
//				} catch (Exception e) {
//					// δ��װ
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
//	 * ��� APK���Ƿ����������
//	 * 
//	 * @param apkPath
//	 *            APK�ļ�·��
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
