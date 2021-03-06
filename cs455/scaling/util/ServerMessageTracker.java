package cs455.scaling.util;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.channels.SelectionKey;

public class ServerMessageTracker implements Runnable {
	// Will use to message every 5 seconds
	private final int messageRate = 5;
	private int messageThroughput;
	private final Object lock = new Object();
	private final DateTimeFormatter formatter;
	private List<String> activeConnections;

	public ServerMessageTracker() {
		this.messageThroughput = 0;
		this.activeConnections = new ArrayList<String>();
		formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
	}

	public void incrementMessageThroughput() {
		synchronized(lock) {
			messageThroughput++;
		}
	}

	public void incrementActiveConnections(String connection) {
		synchronized(lock) {
			if(!activeConnections.contains(connection)) {
				activeConnections.add(connection);
			}
		}
	}

	public void decrementActiveConnections(String connection) {
		synchronized(lock) {
			if(activeConnections.contains(connection)) {
				activeConnections.remove(connection);
			}
		}
	}

	@Override
	public void run() {
		// Create the time we will wait until
		LocalDateTime messageTime = LocalDateTime.now().plusSeconds(messageRate);
		while(true) {
			// Get current time
			LocalDateTime current = LocalDateTime.now();
			// Check to see if time is after the print time
			if(current.isAfter(messageTime)) {
				// Set next print time
				messageTime = current.plusSeconds(messageRate);
				synchronized(lock) {
					System.out.println("[" + messageTime.format(formatter) + "] Current Server Throughput: " + Math.floor(messageThroughput/5.0) + " message/s, Active Clients: " + activeConnections.size() );
					// Reset tracker
					messageThroughput = 0;
				}
			}
		}
	}
}
