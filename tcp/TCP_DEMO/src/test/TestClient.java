package test;


import java.io.IOException;

import client.ConnectionManager;

/**
 * �����������ļ򵥿ͻ��˳���
 * 
 */
public class TestClient {

	public static void main(String[] args) throws IOException {
		
		ConnectionManager.createConnection("localhost", 65432);
	}

}