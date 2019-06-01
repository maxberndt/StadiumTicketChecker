package at.fhsalzburg.its.nos.stadium;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import at.fhsalzburg.its.nos.stadium.message.CheckResult;
import at.fhsalzburg.its.nos.stadium.message.Message;
import at.fhsalzburg.its.nos.stadium.message.MessageType;
import at.fhsalzburg.its.nos.stadium.message.RegisterGateMessage;
import at.fhsalzburg.its.nos.stadium.message.TicketCheckMessage;
import at.fhsalzburg.its.nos.stadium.message.TicketCheckResponseMessage;
import java.util.Date;

public class ServerThread implements Runnable {
	private final Socket clientSocket; // initialize in const'r
	private int gateNumber = 0;
	private List<ticket> WhiteList = new ArrayList<ticket>();
	private List<ticket> BlackList = new ArrayList<ticket>();

	public ServerThread(Socket clientSocket, List<ticket> WhiteList, List<ticket> BlackList) {
		super();
		this.clientSocket = clientSocket;
		this.WhiteList = WhiteList;
		this.BlackList = BlackList;
	}

	public void run() {
		PrintWriter out;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputLine;
			inputLine = in.readLine();
			RegisterGateMessage rm = (RegisterGateMessage) Message.parse(inputLine);
			gateNumber = rm.getSectorId();
			System.out.println("Gate " + gateNumber + " logged in!");
			String outputLine = "";

			while ((inputLine = in.readLine()) != null) {
				int check = 0;
				Message mt = Message.parse(inputLine);
				if (mt.getType() == MessageType.KEEPALIVE) {
					// Return Keepalive Message
					Message keepalive = new Message(MessageType.KEEPALIVE);
					out.println(keepalive.toJsonString());
				}

				// TicketCheckResponseMessage response = new
				// TicketCheckResponseMessage(CheckResult.VALID);
				// outputLine = response.toJsonString();

				else if (mt.getType() == MessageType.TICKETCHECK) {
					TicketCheckMessage ticketnumber = (TicketCheckMessage) Message.parse(inputLine);
					String ticketID = ticketnumber.getTicketId();
					ticket current = new ticket();
					for (int i = 0; i < WhiteList.size() + 1; i++) {

						if (i != WhiteList.size()) {
							current = WhiteList.get(i);
						}

						if ((current.getTicketID()).equals(ticketID)) {
							System.out.println("Ticket found!...");

							if (current.getSector() == gateNumber) {
								System.out.println("Correct Sector!");
								Date today = new Date();

								if (current.getFrom().before(today) && current.getTo().after(today)) {
									System.out.println("Correct Date!");

									ticket blacklistTicket = new ticket();
									for (int o = 0; o < BlackList.size() + 1; o++) {

										if (o != BlackList.size()) {
											blacklistTicket = BlackList.get(o);
										}

										if ((current.getTicketID()).equals(blacklistTicket.getTicketID())) {
											System.out.println("Ticket on Blacklist!");
											TicketCheckResponseMessage response = new TicketCheckResponseMessage(
													CheckResult.USED);
											outputLine = response.toJsonString();
											out.println(outputLine);
											break;
										}

										else if (o == BlackList.size()) {
											System.out.println("Ticket valid!");
											BlackList.add(current);
											TicketCheckResponseMessage response = new TicketCheckResponseMessage(
													CheckResult.VALID);
											outputLine = response.toJsonString();
											out.println(outputLine);
											break;
										}

									}

								}

								else {
									System.out.println("Out of Date!");
									TicketCheckResponseMessage response = new TicketCheckResponseMessage(
											CheckResult.OUTOFDATE);
									outputLine = response.toJsonString();
									out.println(outputLine);
								}

							}

							else {
								System.out.println("Wrong Sector!");
								TicketCheckResponseMessage response = new TicketCheckResponseMessage(
										CheckResult.WRONGSECTOR);
								outputLine = response.toJsonString();
								out.println(outputLine);
							}

							break;
						}

						else if (i == WhiteList.size() && check == 0) {
							System.out.println("Ticket not found!");
							TicketCheckResponseMessage response = new TicketCheckResponseMessage(CheckResult.INVALID);
							outputLine = response.toJsonString();
							out.println(outputLine);
						}

					}

				}

				else {
					System.out.println("Message Type not recognized");
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}