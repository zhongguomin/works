package comns.net;

/**
 * @ClassName: HttpConnectable
 * 
 * @author: chellychi
 * 
 * @version: V1.0
 * 
 * @Date: 2013-7-16 ����5:29:12
 * 
 * @Description: ��<code>HttpConnectable</code>��ִ�����������ȡ��������ӿ�</p>
 * 
 *               Copyright 2013�� All rights reserved.
 * 
 *               You have the permissions to modify.
 * 
 */
public interface HttpConnectable {

	public enum HttpState {
		SUCCESS, ERROR
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public String getConnectUrl();

	/**
	 * ����������ȡ��������
	 * 
	 * @param content
	 *            ��ȡ��������
	 * @return
	 */
	public HttpState parseContent(String content);

	/**
	 * ��ȡ�������Դ���
	 * 
	 * @return
	 */
	public int getTryConnectionNum();

	/**
	 * ���������߼�
	 */
	public void handleError();

	/**
	 * ������ɺ��߼�
	 * 
	 * @param httpState
	 *            ���ʱ״̬
	 */
	public void hanleFinish(HttpState httpState);
}
