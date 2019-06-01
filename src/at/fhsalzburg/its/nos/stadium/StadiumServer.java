package at.fhsalzburg.its.nos.stadium;

import java.io.*;
import java.net.*;
import java.util.*;

public class StadiumServer {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		System.out.println("Server starting...");
		// Import CSV - https://www.baeldung.com/java-csv-file-array
		// Create ticket objects

		List<ticket> WhiteList = new ArrayList<ticket>();
		List<ticket> BlackList = new ArrayList<ticket>();

		try (BufferedReader br = new BufferedReader(new FileReader("ticket_data.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split("\\s*,\\s*");
				ticket currentTicket = new ticket(values[0], values[1], values[2], values[3]);
				WhiteList.add(currentTicket);
			}
		}

		System.out.println("Imported Ticket list...");

		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(5000);
			System.out.println("Waiting for Clients...");

			while (true) {
				Socket clientSocket = null;
				clientSocket = serverSocket.accept();
				new Thread(new ServerThread(clientSocket, WhiteList, BlackList)).start();
			}

		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port 5000 or listening for a connection");
			System.out.println(e.getMessage());
		} finally {
			serverSocket.close();
		}

	}

}
