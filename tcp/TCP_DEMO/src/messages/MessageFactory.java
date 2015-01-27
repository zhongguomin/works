package messages;

/**
 *
 *
 */
public class MessageFactory {
	
	public static Message getInstance(int messageType){
		Message m = null;
		
		switch(messageType){
			case Message.ActiveTestRequest : 
				m = new ActiveTestRequest();
				break;
			case Message.ActiveTestResponse : 
				m = new ActiveTestResponse();
				break;
			default:
				throw new RuntimeException("����������ݰ���δ���ṩ");
		}
		
		return m;
	}
	
}