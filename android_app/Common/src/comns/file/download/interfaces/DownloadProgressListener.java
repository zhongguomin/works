package comns.file.download.interfaces;

/**
 * @����: DownloadProgressListener
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-26 ����02:32:48
 * 
 * @����: ��<code>DownloadProgressListener</code>���ļ��������ݽӿ�</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public interface DownloadProgressListener {

	/**
	 * @param downSize
	 *            �����ش�С
	 * @param fileSize
	 *            �ļ���С
	 * @param speed
	 *            �����ٶ�
	 * @param interrupt
	 *            �Ƿ��ж�
	 */
	public void onDownload(int downSize, int fileSize, int speed,
			boolean interrupt);
}
