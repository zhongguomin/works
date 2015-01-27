package messages;

import utils.ByteArrayBuilder;


/**
 * 心跳包（请求包）
 * 
 */
public class ActiveTestRequest extends AbstractMessage implements Message {

	/* （非 Javadoc）
	 * @see houlei.net.keepconn.messages.Message#getBytes()
	 */
	public byte[] getBytes() {
		return new ByteArrayBuilder().write(MessageHeaderLength).write(ActiveTestRequest).toBytes();
	}

	/* （非 Javadoc）
	 * @see houlei.net.keepconn.messages.Message#getMessageLength()
	 */
	public int getMessageLength() {
		return MessageHeaderLength;
	}

	/* （非 Javadoc）
	 * @see houlei.net.keepconn.messages.Message#getMessageType()
	 */
	public int getMessageType() {
		return ActiveTestRequest;
	}

	/* （非 Javadoc）
	 * @see houlei.net.keepconn.messages.Message#parse(byte[])
	 */
	public void parse(byte[] b) throws ParseException {
		//空包体，所以空函数体。
	}

}