package com.michael.thread;

import java.util.concurrent.BlockingQueue;

public class ConsumerThread implements Runnable{

	private BlockingQueue<String> consumeerQueue = null;
	
	public ConsumerThread(BlockingQueue<String> queue) {
		this.consumeerQueue = queue;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(3*1000);
				
				String take = consumeerQueue.take();
				System.out.println("ConsumerThread: " + take);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
