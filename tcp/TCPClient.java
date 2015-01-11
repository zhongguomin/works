package com.tcp.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;

public class TCPClient {

	private String hsot = "127.0.0.1";
	private int port = 5023;
	
	class SendMsgThread implements Runnable {
		private Socket socket = null;
		
		public SendMsgThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					PrintWriter pw = new PrintWriter(
							new OutputStreamWriter(socket.getOutputStream()));
					pw.write("this is tcp client ..");
					pw.flush();
					Thread.sleep(3*1000);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class ReceiveThread implements Runnable {
		private Socket socket = null;
		
		public ReceiveThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					Reader reader = new InputStreamReader(socket.getInputStream());
					
					CharBuffer charBuffer = CharBuffer.allocate(8192);
					int charIndex = -1;
					while ((charIndex = reader.read(charBuffer)) != -1) {
						charBuffer.flip();
						System.out.println("client: " + charBuffer.toString());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void start() {
		try {
			Socket socket = new Socket(hsot, port);
			new Thread(new SendMsgThread(socket)).start();
			new Thread(new ReceiveThread(socket)).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new TCPClient().start();
	}
	
}
