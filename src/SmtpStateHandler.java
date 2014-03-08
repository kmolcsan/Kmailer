/**
 * The SmtpStateHandler class is designed to properly interpret 
 * response messages sent from an SMTP server.  It will properly 
 * parse the status code and text from the response, and determine 
 * whether the status represents an error or successful 
 * communications.
 * 
 * @author Ken Molcsan
 * @version 1.0a
 * Date: 2008-06-15
 * Email: molcsan@mscd.edu
 *
 */

public class SmtpStateHandler{
	// Private Data Members
	private int code = 0;
	private String text = null;
	private boolean error = false;
	
	/**
	 * Purpose: The SmtpStateHandler constructor configures the 
	 * 			state handler object to default "No Error" values.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	none
	 * 
	 * @return SmtpStateHandler Object
	 * @see nothing
	 * 
	 */
	public SmtpStateHandler(){
		code = 0;
		text = "";
		error = false;
	}
	
	/**
	 * Purpose: The chkResponse method takes an SMTP response
	 * 			String and determines whether the status being
	 * 			reported indicates a fatal error, a non-fatal 
	 * 			error, or just a message to the end user.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	none
	 * 
	 * @return Boolean True = Ok; False = Error Encountered;
	 * @see nothing
	 * 
	 */
	public boolean chkResponse(String r){
		if((r.length() != 0) && !error){
			code = Integer.parseInt(r.substring(0,3));
			text = r;
			if(code >= 500){
				// 500 level response - Error Unrecoverable
				System.out.println("\n The server has returned an error:");
				System.out.println("   -> " + text);
				System.out.println(" Your message could not be sent.");
				error = true;
			}else if(code >=400){
				// 400 level response - Error Try Again
				System.out.println("\n The server was unable to complete the request:");
				System.out.println("   -> " + text);
				System.out.println(" Please wait a few moments, then try to send "
						         + "your message again.");
				error = true;
			}else if(code >=300){
				// 300 level response - Awaiting further commands 
				System.out.println(text);
				error = false;
			}else{
				// 200 level response - Status OK
				System.out.println(text);
				error = false;
			}
		}
		return !(error);
	}
}