package com.michael.thread;

import java.util.concurrent.BlockingQueue;

public class ProducerThread implements Runnable{

	private BlockingQueue<String> producerQueue = null;
	
	public ProducerThread(BlockingQueue<String> queue) {
		this.producerQueue = queue;
	}
	
	public void setProducerQueue(BlockingQueue<String> queue) {
		
	}

	@Override
	public void run() {
		int i = 0;
		while(true) {
			try {
				producerQueue.put("ProducerThread " + i);
				System.out.println("i = " + i);
				i++;
				
				Thread.sleep(1*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
