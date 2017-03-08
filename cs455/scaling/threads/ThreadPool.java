package cs455.scaling.threads;

import java.util.Queue;
import java.util.LinkedList;

public class ThreadPool {

	private Queue<Worker> threadPool;
	private ThreadPoolManager manager;

	public ThreadPool(int threadPoolSize, ThreadPoolManager manager) {
		threadPool = new LinkedList<Worker>();
		System.out.println("Creating ThreadPool of size " + threadPoolSize);
		for(int i = 0; i < threadPoolSize; i++) {
			System.out.println("Creating worker thread " + (i+1));
			Worker worker = new Worker(this);
			threadPool.add(worker);
			new Thread(worker).start();
		}
		this.manager = manager;
	}

	public void runTask(Task task) {
		Worker worker;
		synchronized(threadPool) {
			worker = threadPool.poll();
		}
		if(worker != null) {
			worker.addTask(task);
		}
	}

	public Task addBackToPool(Worker worker) {
		Task task = null;
		if(!manager.checkForMoreTasks()) {
			System.out.println("Adding task to worker");
			task = manager.removeTask();
		} else {
			System.out.println("No more tasks adding back to pool");
			synchronized(threadPool) {
				threadPool.add(worker);
			}
		}
		return task;
	}

	public void giveWorkerTask(Task task) {
		if(!isEmpty()) {
			runTask(task);
		}
	}

	public boolean isEmpty() {
		return threadPool.isEmpty(); 
	}

	public int getSize() {
		return threadPool.size();
	}
}
