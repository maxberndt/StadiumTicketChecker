package at.fhsalzburg.its.nos.stadium;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ticket {
	protected String ticketID;
	protected int sector;
	protected Date validFrom;
	protected Date validTo;

	
	ticket(String ID, String s, String From, String To){
		ticketID = ID;
		sector = Integer.parseInt(s);
		try {
			//Remove T/Z
			From = charRemoveAt(From, 10);
			From = charRemoveAt(From, 18);
			To = charRemoveAt(To, 10);
			To = charRemoveAt(To, 18);
			validFrom = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse(From);
			validTo = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").parse(To);  
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	ticket(){}
	
	protected void printTicket(){
		System.out.println("Ticket ID: " + ticketID);
		System.out.println("Sector: " + sector);
		System.out.println("Valid from: " + validFrom);
		System.out.println("Valid to: " + validTo);
	}
	
	public String getTicketID() {
		return ticketID;
	}
	
	public int getSector() {
		return sector;
	}
	
	public Date getFrom() {
		return validFrom;
	}
	
	public Date getTo() {
		return validTo;
	}
	
	public static String charRemoveAt(String str, int p) {  
        return str.substring(0, p) + str.substring(p + 1);  
     }  
	
}

