package messages;

import utils.ByteArrayBuilder;


/**
 * ���������������
 * 
 */
public class ActiveTestRequest extends AbstractMessage implements Message {

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#getBytes()
	 */
	public byte[] getBytes() {
		return new ByteArrayBuilder().write(MessageHeaderLength).write(ActiveTestRequest).toBytes();
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
		return ActiveTestRequest;
	}

	/* ���� Javadoc��
	 * @see houlei.net.keepconn.messages.Message#parse(byte[])
	 */
	public void parse(byte[] b) throws ParseException {
		//�հ��壬���Կպ����塣
	}

}