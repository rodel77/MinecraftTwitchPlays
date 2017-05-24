package mx.com.rodel.twitchplays.utils;

public class Scheduler {
	public long start;
	public long delay;
	public Runnable runnable;

	public Scheduler(long start, long delay, Runnable runnable) {
		this.start = start;
		this.delay = delay;
		this.runnable = runnable;
	}
	
	public long elapsedTime(){
		return System.currentTimeMillis() - start;
	}
	
	public long remainingTime(){
		return delay - elapsedTime();
	}
	
	public boolean canExecute(){
		return System.currentTimeMillis() - start >= delay;
	}
}
