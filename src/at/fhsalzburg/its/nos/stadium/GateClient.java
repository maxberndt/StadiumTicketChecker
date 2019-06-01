package at.fhsalzburg.its.nos.stadium;

import java.io.*;
import java.net.*;
//import at.fhsalzburg.its.nos.stadium.message.*;

import at.fhsalzburg.its.nos.stadium.message.*;



public class GateClient {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		String hostName = "localhost";
		int portNumber = 5000;
		Socket echoSocket = null;
		Integer keepalive = 1;
		
		
		try {
			echoSocket = new Socket(hostName, portNumber);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			int GateNumber;
			System.out.println("Bitte Gatenummer eingeben");
			GateNumber = Integer.parseInt(stdIn.readLine());
			RegisterGateMessage registerGate = new RegisterGateMessage(GateNumber);
			out.println(registerGate.toJsonString());

			Thread t = new ServerHandler(in, keepalive);
			Thread ka = new KeepAliveHandler(out, keepalive);
			t.start();
			ka.start();

			String userInput;
			String serverOutput;
			while ((userInput = stdIn.readLine()) != null) {
				
				TicketCheckMessage check = new TicketCheckMessage(userInput);
				serverOutput = check.toJsonString();
				out.println(serverOutput);
			}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		} finally {
			echoSocket.close();
		}
	}

}
