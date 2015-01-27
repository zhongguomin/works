package messages;

/**
 *	网络数据包对应类的最高抽象<br/>
 *  一般数据包分包头和包体两个部分，包头一般含包长度和包类型等信息，包体是本包所承载的内容。<br/>
 *  本例中，包头就包含长度和类型两个信息，分别占4字节空间。
 * 
 */
public interface Message {
	
	/**
	 * 网络数据包类型：心跳包（请求包）
	 */
	public static final int ActiveTestRequest		= 0x00000001;
	
	/**
	 * 网络数据包类型：心跳包（应答包）
	 */
	public static final int ActiveTestResponse	= 0x80000001;
	
	/**
	 *  网络数据包类型：登陆包（请求包）
	 */
	public static final int LoginRequest		= 0x00000002;
	
	/**
	 * 网络数据包类型：登陆包（应答包）
	 */
	public static final int LoginResponse		= 0x80000002;
	
	/**
	 *  网络数据包类型：登出包（请求包）
	 */
	public static final int LogoutRequest		= 0x00000003;
	
	/**
	 * 网络数据包类型：登出包（应答包）
	 */
	public static final int LogoutResponse	= 0x80000003;
	
	/*
	 * 这里还可以添加其他信息包的类型
	 * */
	
	
	/**
	 * 获取数据包的总长度（包括包头加包体的长度）
	 * @return数据包的总长度
	 */
	public abstract int getMessageLength();
	
	/**
	 * 获取数据包的类型
	 * @return数据包的类型
	 */
	public abstract int getMessageType();
	
	/**
	 * 解析数据包的内容。使字节流转换成类对象。该方法主要完成对包体的解析过程。
	 * @param b 字节数组（字节流）
	 * @throws ParseException 当解析时发生异常时抛出
	 */
	public abstract void parse(byte [] b) throws ParseException;
	
	/**
	 * 将类对象的内容转换成字节数组
	 * @return 类对象对应的字节数组
	 */
	public abstract byte [] getBytes();
}