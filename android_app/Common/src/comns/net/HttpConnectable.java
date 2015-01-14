package comns.net;

/**
 * @ClassName: HttpConnectable
 * 
 * @author: chellychi
 * 
 * @version: V1.0
 * 
 * @Date: 2013-7-16 下午5:29:12
 * 
 * @Description: 类<code>HttpConnectable</code>是执行联网处理获取内容任务接口</p>
 * 
 *               Copyright 2013。 All rights reserved.
 * 
 *               You have the permissions to modify.
 * 
 */
public interface HttpConnectable {

	public enum HttpState {
		SUCCESS, ERROR
	}

	/**
	 * 获取联网链接
	 * 
	 * @return
	 */
	public String getConnectUrl();

	/**
	 * 解析联网获取到的内容
	 * 
	 * @param content
	 *            获取到的类容
	 * @return
	 */
	public HttpState parseContent(String content);

	/**
	 * 获取联网尝试次数
	 * 
	 * @return
	 */
	public int getTryConnectionNum();

	/**
	 * 处理出错后逻辑
	 */
	public void handleError();

	/**
	 * 处理完成后逻辑
	 * 
	 * @param httpState
	 *            完成时状态
	 */
	public void hanleFinish(HttpState httpState);
}
