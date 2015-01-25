package com.gpstracker.network;

public class ConnectionManager {
	
	private TCPConnection tcpConnection = null;
	
	public ConnectionManager(final String host, final int port) {
		createTCPConnecton(host, port);
	}

	public void sendMessage(String message) {
		tcpConnection.putSendMsgQueue(message);
	}
	
	public String receiveMessage() {
		
		return null;
	}
	
	public void closeTCPConnection() {
		tcpConnection.closeConnection();
	}
	
	public TCPConnection createTCPConnecton(final String host, final int port) {
		tcpConnection = new TCPConnection(host, port);
		return tcpConnection;
	}

}
