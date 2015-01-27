package messages;

/**
 * 网络数据包对应类的抽象类
 * 
 */
public abstract class AbstractMessage {
	/**
	 * 包头长度。（协议采用定长包头，长度为8)
	 */
	public static final int MessageHeaderLength = 8;
	
}