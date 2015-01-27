package messages;

import utils.ByteArrayBuilder;


/**
 *��������Ӧ�����
 *
 */
public class ActiveTestResponse extends AbstractMessage implements Message {

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#getBytes()
	 */
	public byte[] getBytes() {
		return new ByteArrayBuilder().write(MessageHeaderLength).write(ActiveTestResponse).toBytes();
	}

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#getMessageLength()
	 */
	public int getMessageLength() {
		return MessageHeaderLength;
	}

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#getMessageType()
	 */
	public int getMessageType() {
		return ActiveTestResponse;
	}

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#parse(byte[])
	 */
	public void parse(byte[] b) throws ParseException {
//		�հ��壬���Կպ����塣
	}

}