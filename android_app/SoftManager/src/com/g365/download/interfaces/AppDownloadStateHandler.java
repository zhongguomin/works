package com.g365.download.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ��������������״̬�Ľӿ�
 * @author Administrator
 *
 */
public interface AppDownloadStateHandler {

	/**
	 * �Ƿ��µ������ļ�
	 * @param fileUrl  ��������
	 * @return
	 */
	public boolean isNewFile(String fileUrl);
	
	/**
	 * ����µ������ļ�
	 * @param fileUrl  ��������
	 * @param fileSize �ļ��Ĵ�С
	 * @param fileState ����״̬
	 * @return
	 */
	public boolean addNewFile(String fileUrl,int fileSize,int fileState);

    /**
     * ɾ���ļ�������Ϣ
     * @param fileUrl ��������
     * @return
     */
    public boolean delFile(String fileUrl);

    /**
     * �����ļ�����״̬
     * @param fileUrl   ��������
     * @param fileSize  �ļ���С
     * @param fileState �ļ�״̬
     * @return
     */
    public boolean  updateFile(String fileUrl,int fileSize,int fileState);

    /**
     * ��ȡ�ļ���С
     * @param fileUrl  ��������
     * @return
     */
    public int getFileSize(String fileUrl);

    /**
     * ��ȡ�ļ������߳�״̬
     * @param fileUrl   ��������
     * @return
     */
    public ArrayList<HashMap<Integer,Integer>> getThreadState(String fileUrl);

    /**
     * ���һ���µ��߳���������
     * @param fileUrl   ��������
     * @param threadId
     * @param threadData �߳�����
     * @return
     */
    public boolean addNewThreadTask(String fileUrl,int threadId,
    		HashMap<Integer, Integer>  threadData);
    /**
     * ����һ���߳�������Ϣ
     * @param fileUrl ��������
     * @param threadId
     * @param threadData �߳�����
     * @return
     */
    public boolean updateThreadTask(String fileUrl,int threadId,
    		HashMap<Integer, Integer> threadData);
    
    /**
     * ɾ���ļ��߳�������Ϣ
     * @param fileUrl  ��������
     * @return
     */
    public boolean deleteThreadTask(String fileUrl);

}
