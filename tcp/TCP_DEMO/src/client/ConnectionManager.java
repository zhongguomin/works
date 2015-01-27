package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import messages.Message;
import messages.MessageFactory;
import messages.ParseException;


/**
 * 客户端长连接管理类，负责维持所产生的长连接。
 * 
 */
public class ConnectionManager {
	/**
	 * 心跳周期（单位：毫秒）
	 */
	private volatile static long activeCycle = 1000;
	
	/**
	 * 存放产生的长连接
	 */
	private static Set<Connection> pool = Collections.synchronizedSet(new HashSet<Connection>());
	
	/**
	 * 用于定时发送心跳包
	 */
	private static ConnectActiveMonitor monitor = new ConnectActiveMonitor();
	
	static{
		monitor.start();
	}
	
	public static Connection createConnection(String host,int port) throws IOException{
		Connection conn = new Connection(host, port);
		pool.add(conn);
		return conn;
	}
	
	public static Connection createConnection(Socket socket) throws IOException{
		Connection conn = new Connection(socket);
		pool.add(conn);
		return conn;
	}
	
	public static void removeConnection(Connection conn){
		pool.remove(conn);
	}
	
	static class ConnectActiveMonitor extends Thread{
		private volatile boolean running = true;
		
		public void run(){
			while(running){
				long time = System.currentTimeMillis();
				
				for(Connection con : pool){
					try {
						if(con.getLastActTime()+activeCycle < time)
							con.send(MessageFactory.getInstance(Message.ActiveTestRequest));
					} catch (IOException e) {
						removeConnection(con);
					} catch (ParseException e) {
					}
				}
				
				yield();
			}
		}
		
		void setRunning(boolean b){
			running = b;
		}
	}
}