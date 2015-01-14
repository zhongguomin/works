package comns.system;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/** 
 * @����: CommandHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 *
 * @����: 2013-2-22 ����11:24:51
 * 
 * @����: ��<code>CommandHelper</code>��ִ��ϵͳ�������</p>
 * 
 *      Copyright 2013�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 *
 */  
public class CommandHelper {

	/**
	 * ����ROOTִ�������������
	 * 
	 * @param cmd
	 *            Ҫִ�е�����
	 * @return ����ִ�еĽ��
	 */
	public static String execRootCmd(String cmd) {

		String result = "";
		DataOutputStream dos = null;
		DataInputStream dis = null;

		try {
			Process p = Runtime.getRuntime().exec("su");// ����Root�����androidϵͳ����su����
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
	 * ����ROOTִ���������ע������
	 * 
	 * @param cmd
	 *            Ҫִ�е�����
	 * @return ������˳�ֵ
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
	 * ִ����ͨȨ��������ؽ��
	 * 
	 * @param cmd
	 *            Ҫִ�е�����
	 * @return ִ�к�Ľ��
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
	 * ִ����ͨ�������ע���
	 * 
	 * @param cmd
	 *            Ҫִ�е�����
	 * @return ������̽���ֵ
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
