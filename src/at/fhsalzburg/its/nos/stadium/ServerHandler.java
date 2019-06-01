package at.fhsalzburg.its.nos.stadium;

import java.io.BufferedReader;
import java.io.IOException;

import at.fhsalzburg.its.nos.stadium.message.Message;
import at.fhsalzburg.its.nos.stadium.message.MessageType;
import at.fhsalzburg.its.nos.stadium.message.TicketCheckResponseMessage;

public class ServerHandler extends Thread {
	private BufferedReader reader;
	private Integer keepalive;

	public ServerHandler(BufferedReader reader, Integer keepalive) {
		super();
		this.reader = reader;
		this.keepalive = keepalive;
	}

	@Override
	public void run() {
		String readLine;
		try {
			while ((readLine = reader.readLine()) != null) {
				Message message = Message.parse(readLine);
				if (message.getType() == MessageType.CHECKRESPONSE) {
					TicketCheckResponseMessage response = (TicketCheckResponseMessage) message;
					System.out.println("TICKET IS " + response.getCheckResult());
				}

				if (message.getType() == MessageType.KEEPALIVE) {
					synchronized (keepalive) {
						System.out.println("KeepAlive Message erhalten!");
						keepalive = 1;
						System.out.println("Auf " + keepalive + " gesetzt.");
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
