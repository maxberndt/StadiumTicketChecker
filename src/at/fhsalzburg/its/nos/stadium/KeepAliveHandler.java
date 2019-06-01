package at.fhsalzburg.its.nos.stadium;

import java.io.*;
import java.util.concurrent.TimeUnit;
import at.fhsalzburg.its.nos.stadium.message.Message;
import at.fhsalzburg.its.nos.stadium.message.MessageType;

public class KeepAliveHandler extends Thread {

	private PrintWriter out;
	private Integer keepalive;

	public KeepAliveHandler(PrintWriter out, Integer keepalive) {
		super();
		this.out = out;
		this.keepalive = keepalive;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (keepalive) {
				System.out.println(keepalive);

				if (keepalive == 1) {
					System.out.println("Keepalive Nachricht wird versendet...");
					Message keepalivem = new Message(MessageType.KEEPALIVE);
					out.println(keepalivem.toJsonString());
					keepalive = 0;
				}

				else if (keepalive == 0) {
					System.out.println("Keepalive ERROR...");
					System.exit(1);
				}

			}

			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
