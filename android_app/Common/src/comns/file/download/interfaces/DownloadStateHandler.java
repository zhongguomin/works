package comns.file.download.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @����: DownloadStateHandler
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-26 ����04:16:56
 * 
 * @����: ��<code>DownloadStateHandler</code>��������������״̬�Ľӿ�</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public interface DownloadStateHandler {

	/**
	 * �Ƿ��µ������ļ�
	 * 
	 * @param fileUrl
	 *            ��������
	 * @return
	 */
	public boolean isNewFile(String fileUrl);

	/**
	 * ����µ������ļ�
	 * 
	 * @param fileUrl
	 *            ��������
	 * @param fileMd5
	 *            �ļ��� md5ֵ
	 * @param fileSize
	 *            �ļ��Ĵ�С
	 * @param fileState
	 *            ����״̬
	 * @return
	 */
	public boolean addNewFile(String fileUrl, String fileMd5, int fileSize,
			int fileState);

	/**
	 * ɾ���ļ�������Ϣ
	 * 
	 * @param fileUrl
	 *            ��������
	 * @return
	 */
	public boolean delFile(String fileUrl);

	/**
	 * �����ļ�����״̬
	 * 
	 * @param fileUrl
	 *            ��������
	 * @param fileMd5
	 *            �ļ��� md5ֵ
	 * @param fileSize
	 *            �ļ���С
	 * @param fileState
	 *            �ļ�״̬
	 * @return
	 */
	public boolean updateFile(String fileUrl, String fileMd5, int fileSize,
			int fileState);

	/**
	 * ��ȡ�ļ���С
	 * 
	 * @param fileUrl
	 *            ��������
	 * @return
	 */
	public int getFileSize(String fileUrl);

	/**
	 * ��ȡ�ļ������߳�״̬
	 * 
	 * @param fileUrl
	 *            ��������
	 * @return
	 */
	public ArrayList<HashMap<Integer, Integer>> getThreadState(String fileUrl);

	/**
	 * ���һ���µ��߳���������
	 * 
	 * @param fileUrl
	 *            ��������
	 * @param threadData
	 *            �߳�����
	 * @return
	 */
	public boolean addNewThreadTask(String fileUrl, int threadId,
			HashMap<Integer, Integer> threadData);

	/**
	 * ����һ���߳�������Ϣ
	 * 
	 * @param fileUrl
	 *            ��������
	 * @param threadData
	 *            �߳�����
	 * @return
	 */
	public boolean updateThreadTask(String fileUrl, int threadId,
			HashMap<Integer, Integer> threadData);

	/**
	 * ɾ���ļ��߳�������Ϣ
	 * 
	 * @param fileUrl
	 *            ��������
	 * @return
	 */
	public boolean deleteThreadTask(String fileUrl);

}
