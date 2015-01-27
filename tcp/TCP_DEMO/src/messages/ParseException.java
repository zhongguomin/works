package messages;

/**
 *	解析数据包产生异常时抛出。
 * 
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ParseException() {
		
	}

	public ParseException(String message) {
		super(message);
		
	}

	public ParseException(Throwable cause) {
		super(cause);
		
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
		
	}

}