package com.tcp.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;

public class TCPServer {

	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private int port = 5023;
	
	private void start() {
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				socket = serverSocket.accept();
				new Thread(new SocketThead(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class SocketThead implements Runnable {

		private Socket socket = null;
		private String temp= null;
		
		public SocketThead(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			try {
				Reader reader = new InputStreamReader(socket.getInputStream());
				Writer writer = new OutputStreamWriter(socket.getOutputStream());
				CharBuffer charBuffer = CharBuffer.allocate(1024);
				int readIndex = -1;
				while ((readIndex = reader.read(charBuffer)) != -1) {
					charBuffer.flip();
					temp += charBuffer.toString();
					System.out.println("server: " + temp);
					
					writer.write("this is server");
					writer.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (!socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		TCPServer tcpServer = new TCPServer();
		
		tcpServer.start();
	}
	
}
