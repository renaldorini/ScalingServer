package cs455.scaling.threads;

import java.util.LinkedList;

public class ThreadPool {

	private LinkedList<Worker> threadPool;

	public ThreadPool(int threadPoolSize) {
		threadPool = new LinkedList<Worker>();
		System.out.println("Creatin ThreadPool of size " + threadPoolSize);
		for(int i = 0; i < threadPoolSize; i++) {
			System.out.println("Creating worker thread " + (i+1));
			Worker worker = new Worker(this);
			threadPool.add(worker);
			new Thread(worker).start();
		}
	}

	public void addBackToPool(Worker worker) {
		synchronized(threadPool) {
			if(!threadPool.contains(worker)) {
				threadPool.add(worker);
			}
		}
	}
}
