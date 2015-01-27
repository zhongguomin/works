package messages;

import utils.ByteArrayBuilder;


/**
 *心跳包（应答包）
 *
 */
public class ActiveTestResponse extends AbstractMessage implements Message {

	/* （非 Javadoc）
	 * @see houlei.net.keepconn.messages.Message#getBytes()
	 */
	public byte[] getBytes() {
		return new ByteArrayBuilder().write(MessageHeaderLength).write(ActiveTestResponse).toBytes();
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
		return ActiveTestResponse;
	}

	/* （非 Javadoc）
	 * @see houlei.net.keepconn.messages.Message#parse(byte[])
	 */
	public void parse(byte[] b) throws ParseException {
//		空包体，所以空函数体。
	}

}