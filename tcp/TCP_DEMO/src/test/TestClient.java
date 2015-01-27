package test;


import java.io.IOException;

import client.ConnectionManager;

/**
 * 测试心跳包的简单客户端程序
 * 
 */
public class TestClient {

	public static void main(String[] args) throws IOException {
		
		ConnectionManager.createConnection("localhost", 65432);
	}

}