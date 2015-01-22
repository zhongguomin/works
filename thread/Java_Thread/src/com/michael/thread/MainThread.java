package com.michael.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainThread {
	
	public static BlockingQueue<String> consumerQueue = new ArrayBlockingQueue<String>(10);
	public static BlockingQueue<String> producerQueue = new ArrayBlockingQueue<String>(10);
	public int m = 0;
	
	public static void main(String[] args) {

		ProducerThread producerThread = new ProducerThread(consumerQueue);
		//ConsumerThread consumerThread = new ConsumerThread(producerQueue);
		
		new Thread(producerThread).start();
		//new Thread(consumerThread).start();
		
		MainThread mainThread = new MainThread();
		//mainThread.testProducer();
		mainThread.testConsumer();
	}
	
	public void testProducer(){
		while(true) {
			try {
				producerQueue.put("MainThread " + m);
				System.out.println("m = " + m);
				m++;
				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testConsumer(){
		while(true) {
			try {
				Thread.sleep(3*1000);
				
				String take = consumerQueue.take();
				System.out.println("MainThread: " + take);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
