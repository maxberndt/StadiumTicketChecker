package at.fhsalzburg.its.nos.stadium;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import at.fhsalzburg.its.nos.stadium.message.Message;
import at.fhsalzburg.its.nos.stadium.message.MessageType;

public class KeepAliveChecker extends Thread {
	private BufferedReader reader;
	String keepalive;
	public KeepAliveChecker(BufferedReader reader, String keepalive) {
		super();
		this.reader = reader;
		this.keepalive = keepalive;
	}
	
	@Override
	public void run() {
		String readLine;
		try {
			while((readLine = reader.readLine()) != null) {
				Message message = Message.parse(readLine);
				if (message.getType()==MessageType.KEEPALIVE) {
					
					System.out.println("Keepalive Nachricht erhalten!");
					keepalive = "TRUE";
				}
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
