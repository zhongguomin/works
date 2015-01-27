package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import utils.ByteArrayReader;

import messages.AbstractMessage;
import messages.Message;
import messages.MessageFactory;
import messages.ParseException;

/**
 * �����������ķ���˼򵥳���
 * 
 */
public class TestServer {
	
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(65432);
		Socket s = ss.accept();
		new SimpleProcessor(s).start();
	}

	static class SimpleProcessor extends Thread{
		private Socket socket;
		private OutputStream out;
		private InputStream in ;
		private volatile boolean running = true;
		
		public SimpleProcessor(Socket s) throws IOException {
			this.socket = s;
			in = s.getInputStream();
			out = s.getOutputStream();
		}
		
		public void run(){
			while(running){
				try {
					Message m = read();
					System.out.println("�յ���Ϣ");
					if(m.getMessageType()==Message.ActiveTestRequest){
						m = MessageFactory.getInstance(Message.ActiveTestResponse);
					}
					send(m);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			try {
				System.out.println(socket);
				if(socket!=null)socket.close();
			} catch (IOException e) {
			}
		}
		
		private void send(Message m) throws IOException{
			out.write(m.getBytes());
			out.flush();
		}

		private Message read() throws IOException, ParseException{
			byte[] header = new byte[AbstractMessage.MessageHeaderLength];
			
			if(in.read(header) != AbstractMessage.MessageHeaderLength)
				throw new IOException("δ�ܶ�ȡ�����İ�ͷ����");
			
			ByteArrayReader bar = new ByteArrayReader(header);
			int len = bar.readInt();
			
			if(len<0)	throw new ParseException("����İ�������Ϣ");
			
			int type = bar.readInt();
			byte [] cache = new byte [len];
			System.arraycopy(header, 0, cache, 0, header.length);
			
			if(in.read(cache, header.length, len-header.length)!=len-header.length)
				throw new IOException("δ�ܶ�ȡ�����İ��岿��");
			
			Message m = MessageFactory.getInstance(type);
			m.parse(cache);
			return m;
		}
	}
}