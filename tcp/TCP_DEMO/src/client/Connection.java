package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import utils.ByteArrayReader;

import messages.AbstractMessage;
import messages.Message;
import messages.MessageFactory;
import messages.ParseException;

/**
 * 客户端长连接的封装类。
 *
 */
public class Connection {
	private Socket socket;
	private OutputStream out;
	private InputStream in ;
	private long lastActTime = 0;
	
	Connection(String host,int port) throws IOException{
		socket = new Socket();
		socket.connect(new InetSocketAddress(host,port));
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}
	
	Connection(Socket socket) throws IOException{
		this.socket=socket;
		in =socket.getInputStream();
		out = socket.getOutputStream();
	}
	
	private void send0(Message m) throws IOException{
		lastActTime = System.currentTimeMillis();
		out.write(m.getBytes());
		out.flush();
	}
	
	private Message readWithBlock0() throws IOException, ParseException{
		lastActTime = System.currentTimeMillis();
		
		byte [] header = new byte[AbstractMessage.MessageHeaderLength];
		if(in.read(header)!=AbstractMessage.MessageHeaderLength)
			throw new IOException("未能读取完整的包头部分");
		
		ByteArrayReader bar = new ByteArrayReader(header);
		int len = bar.readInt();
		if(len<0)throw new ParseException("错误的包长度信息");
		
		int type = bar.readInt();
		byte [] cache = new byte [len];
		System.arraycopy(header, 0, cache, 0, header.length);
		if(in.read(cache, header.length, len-header.length)!=len-header.length)
			throw new IOException("未能读取完整的包体部分");
		
		Message m = MessageFactory.getInstance(type);
		m.parse(cache);
		return m;
	}
	
	/**
	 * 用于发送数据包，由于包头缺少序列号，所以，交互过程，每一阶段必须等到上一阶段应答包收到才能发起下次的请求包。
	 * @param m 待发送的信息包
	 * @return  对应的应答包
	 * @throws IOException
	 * @throws ParseException
	 */
	public synchronized Message send(Message m) throws IOException, ParseException{
		send0(m);
		return readWithBlock0();
	}
	
	public synchronized void close() throws IOException{
		lastActTime = System.currentTimeMillis();
		ConnectionManager.removeConnection(this);
		if(socket!=null)socket.close();
		if(in!=null)in.close();
		if(out!=null)out.close();
	}
	
	public synchronized long getLastActTime(){
		return lastActTime;
	}
}
