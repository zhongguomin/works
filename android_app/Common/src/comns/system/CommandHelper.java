package comns.system;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** 
 * @类名: CommandHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 *
 * @日期: 2013-2-22 上午11:24:51
 * 
 * @描述: 类<code>CommandHelper</code>是执行系统命令的类</p>
 * 
 *      Copyright 2013。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 *
 */  
public class CommandHelper {

	/**
	 * 申请ROOT执行命令并且输出结果
	 * 
	 * @param cmd
	 *            要执行的命令
	 * @return 命令执行的结果
	 */
	public static String execRootCmd(String cmd) {

		String result = "";
		DataOutputStream dos = null;
		DataInputStream dis = null;

		try {
			Process p = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
			dos = new DataOutputStream(p.getOutputStream());
			dis = new DataInputStream(p.getInputStream());

			dos.writeBytes(cmd + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			String line = null;
			while ((line = dis.readLine()) != null) {
				line += "\n";
				result += line;
			}
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 申请ROOT执行命令但不关注结果输出
	 * 
	 * @param cmd
	 *            要执行的命令
	 * @return 命令的退出值
	 */
	public static int execRootCmdSilent(String cmd) {

		int result = -1;
		DataOutputStream dos = null;

		try {
			Process p = Runtime.getRuntime().exec("su");
			dos = new DataOutputStream(p.getOutputStream());

			dos.writeBytes(cmd + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			p.waitFor();
			result = p.exitValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 执行普通权限命令并返回结果
	 * 
	 * @param cmd
	 *            要执行的命令
	 * @return 执行后的结果
	 */
	public static String execCmd(String cmd) {

		String result = "";
		DataInputStream dis = null;

		try {
			Process p = Runtime.getRuntime().exec(cmd);
			dis = new DataInputStream(p.getInputStream());
			String line = null;
			while ((line = dis.readLine()) != null) {
				line += "\n";
				result += line;
			}
			p.waitFor();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 执行普通命令但不关注结果
	 * 
	 * @param cmd
	 *            要执行的命令
	 * @return 命令进程结束值
	 */
	public static int exeCmdSilence(String cmd) {

		int result = -1;

		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			result = p.exitValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
