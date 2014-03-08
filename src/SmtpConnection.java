import java.net.*;
import java.io.*;

/**
 * The SmtpSendMessge class provides an interface to send messages
 * to a host using the standard SMTP protocol
 * 
 * @author Ken Molcsan
 * @version 1.0a
 * Date: 2008-06-15
 * Email: molcsan@mscd.edu
 *
 */

public class SmtpConnection{
	// Private Data Members
	private int port = 0;
	private Socket client = null;
	private BufferedReader input = null;
	private DataOutputStream output = null;
	private SmtpStateHandler response = null;
	
	/**
	 * Purpose: The SmtpConnection constructor configures the 
	 * 			connection object with the following default 
	 * 			parameters: Port = 25
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	none
	 * 
	 * @return SmtpConnection Object
	 * @see nothing
	 * 
	 */
	public SmtpConnection(){
		port = 25;
	}
	
	/**
	 * Purpose: The SmtpConnection constructor configures the 
	 * 			connection object with the specified port number.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	none
	 * 
	 * @param p Port Number
	 * @return SmtpConnection Object
	 * @see nothing
	 * 
	 */
	public SmtpConnection(int p){
		port = p;
	}
	
	/**
	 * Purpose: The send command will open a connection to the
	 * 			supplied SMTP host, and call the sendMessage
	 * 			method with the supplied parameters to transmit
	 * 			the message.  Upon completion, the connection will
	 * 			be closed.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	none
	 * 
	 * @param h host - the host server name/ip address
	 * @param f from - the address of the message sender
	 * @param t to - the address of the message recipient
	 * @param m message - the message content to be sent
	 * @return nothing
	 * @see nothing
	 * 
	 */
	public void send(String h, String f, String t, String m){
		if(openConnection(h)){
			sendMessage(f,t,m);
			closeConnection();
		}
	}
	
	/**
	 * Purpose: The sendMessage method will send the appropriate
	 * 			SMTP commands to the host to transmit the message.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 *  An SMTP connection must be established prior to calling
	 *  this method.
	 * 
	 * @param f from - the address of the message sender
	 * @param t to - the address of the message recipient
	 * @param m message - the message content to be sent
	 * @return nothing
	 * @see nothing
	 * 
	 */
	private void sendMessage(String f, String t, String m){
		/**
		 * The sendMessage method sends a total of three commands:
		 * MAIL FROM, RCPT TO, and DATA
		 */
		if(sendCmd("MAIL FROM:<" + f + ">") && sendCmd("RCPT TO:<" + t + ">")){
			// If successful initialization, send message text
			if(sendCmd("DATA"))
				sendCmd(dotEscapeMsg(m));
		}
	}
	
	/**
	 * Purpose: The dotEscapeMsg method will properly analyze the
	 * 			message string passed, and will replace all
	 * 			occurrences of <CRLF>. with <CRLF>..
	 * 			to allow the full message to be transmitted.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	none
	 * 
	 * @param m message - the message content to be sent
	 * @return String Escaped message will be returned and will be
	 * 				  appended with the final <CRLF>.<CRLF> to
	 * 				  signal the completion of the message.
	 * @see nothing
	 * 
	 */
	private String dotEscapeMsg(String message){
		return message.replace("\r\n.","\r\n..") + "\r\n.\r\n";
	}
	
	/**
	 * Purpose: The openConnection method will return true if the
	 * 			requested connection was established.  Otherwise
	 * 			the method will return false
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	none
	 * 
	 * @param h host - the host server name/ip address
	 * @return Boolean True = success, False = failure;
	 * @see nothing
	 * 
	 */
	private boolean openConnection(String h){
		try{
			client = new Socket(h,port);
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new DataOutputStream(client.getOutputStream());
			response = new SmtpStateHandler();
			
			System.out.println("Opening Connection ...");
			
			return sendCmd("HELO " + InetAddress.getLocalHost().getHostName());
		}catch(IOException e){
			System.out.println("\nAn error occurred while opening the connection.");
			System.out.println("ERROR: " + e.getMessage());
			System.out.println("The message cannot be sent.");
			return false;
		}
	}
	
	/**
	 * Purpose: The closeConnection method will send the "QUIT"
	 * 			command to the connected server, and will discard
	 * 			any existing connection references which may be
	 * 			reused for future message commands.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	A valid SMTP connection must be in progress before this
	 * 	command can be called.  Otherwise the command will throw
	 *  an exception.
	 * 
	 * @return nothing
	 * @see nothing
	 * 
	 */
	private void closeConnection(){
		System.out.println("Closing Connection ...");
		sendCmd("QUIT");
		input = null;
		output = null;
		client = null;
		response = null;
	}
	
	/**
	 * Purpose: The sendCmd method provides the common interface
	 * 			to the connected SMTP server.  When called, it 
	 * 			will send the command parameter passed, and read
	 * 			the response from the SMTP server.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	A valid SMTP connection must be established to use this
	 * 	method.
	 * 
	 * @param command SMTP command to be sent to the SMTP host.
	 * @return Boolean True = successful; False = failure;
	 * @see nothing
	 * 
	 */
	private boolean sendCmd(String command){
		try{
			// First, write the command to the server
			output.writeBytes(command + "\r\n");
			// Next, we wait for a response,
			return response.chkResponse(input.readLine());
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
}