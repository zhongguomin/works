package com.gpstracker.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.gpstracker.utils.Common;

public class TCPConnection {

	private Socket socket = null;
	private SendMsgThread sendMsgThread = null;
	private ReceiveMsgThread receiveMsgThread = null;
	private final int sendMsgQueueSize = 10;
	private final int receiveMsgQueueSize = 10;
	private BlockingQueue<String> sendMsgQueue = null;
	private BlockingQueue<String> receiveMsgQueue = null;
	
	public TCPConnection(String host, int port) {
		try {
			Common.printLog("[TCPConnection] TCPConnection new Socket (" + host + ", " + port + ")");
			socket = new Socket();
			socket.connect(new InetSocketAddress(host, port));			
		} catch (IOException e) {
			e.printStackTrace();
		}

		sendMsgQueue = new ArrayBlockingQueue<String>(sendMsgQueueSize);
		receiveMsgQueue = new ArrayBlockingQueue<String>(receiveMsgQueueSize);
		
		sendMsgThread = new SendMsgThread(socket, sendMsgQueue);
		receiveMsgThread = new ReceiveMsgThread(socket, receiveMsgQueue); 
		new Thread(sendMsgThread).start();
		new Thread(receiveMsgThread).start();
	}
	
	public void putSendMsgQueue(String message) {
		try {
			Common.printLog("[TCPConnection] putSendMsgQueue message = " + message);
			sendMsgQueue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getReceiveMessage() {
		
	}
	
	public void closeConnection() {
		if (sendMsgThread != null) {
			sendMsgThread.closeSendThread();
		}
		
		if (receiveMsgThread != null) {
			receiveMsgThread.closeReceiveThread();
		}
		
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public class SendMsgThread implements Runnable {
		private Socket socket = null;
		private BufferedWriter bw = null;
		private boolean isRun = true;
		private BlockingQueue<String> sendQueue = null;
		
		public SendMsgThread(Socket mSocket, BlockingQueue<String> sendQueue) {
			this.socket = mSocket;
			this.sendQueue = sendQueue;
			
			if (socket != null) {
				try {
					bw = new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void sendToSocket(String message) {
			if (bw != null && message != null) {
				try {
					bw.write(message);
					bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void closeSendThread() {
			isRun = false;
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void run() {
			while (isRun) {
				try {
					String sendMsg = sendMsgQueue.take();
					if (sendMsg != null && sendMsg.length() > 0) {
						Common.printLog("[TCPConnection] SendMsgThread sendMsg = " + sendMsg);
						sendToSocket(sendMsg);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public class ReceiveMsgThread implements Runnable {
		private Socket socket = null;
		private BufferedReader br = null;
		private boolean isRun = true;
		private BlockingQueue<String> receiveQueue = null;
		
		public ReceiveMsgThread(Socket socket, BlockingQueue<String> receiveQueue) {
			this.socket = socket;
			this.receiveQueue = receiveQueue;
			
			if (socket != null) {
				try {
					br = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public String onMessageAvailable() {
			
			return null;
		}
		
		public void closeReceiveThread() {
			isRun = false;
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void run() {
			while (isRun) {
				try {
					String recMsg = br.readLine();
					if (recMsg != null && recMsg.length()>0) {
						receiveMsgQueue.put(recMsg);
					}
					Thread.sleep(200);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
