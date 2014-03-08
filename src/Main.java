import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Statement of purpose:
 * 
 * 	This program will send an email message using the provided
 * 	input parameters (host, from, to) and a stream of message
 * 	text from the command line.
 * 
 * 
 * @author Ken Molcsan
 * @version 1.0a
 * Date: 2008-06-15
 * Email: molcsan@mscd.edu
 * 
 * 
 * Description of input and output:
 * 
 * 	input:  three parameters are expected to be passed in the 
 * 			following order: host to connect to, address of the
 * 			person the message is from, address of the person the
 * 			message is to.  The contents of System.in will also be
 * 			read until a null line is encountered (EOF).
 * 	output: Status messages will be written to the standard
 * 			System.out console indicating the success or failure
 * 			of the message communications.
 * 
 * 
 * How to use:
 * 
 * 	Save the files Main.java, SmtpConnection.java, and 
 * 	SmtpStateHandler.java.  Compile each file using the javac.exe
 * 	application.  Run the program from a command line by typing:
 * 	'java Main "mailServer" "fromAddress" "toAddress"'.
 * 
 * 
 * Assumptions on expected data:
 * 
 * 	The message read from the standard System.in input stream are
 * 	expected to be written in valid SMTP protocol format (RFC 822)
 * 
 * 
 * Test Platform:
 * 
 * 	Windows Vista Home Edition
 *  javac 1.6.0_03
 *  
 *  
 * System test design:
 * NOTE: All tests were performed on the smtp.comcast.net mail
 *       server system.
 * Step		Description						Expected Outcome
 * ----		-----------						----------------
 *  01		from: g.bush@whitehouse.gov		Message Successful
 *  		to: molcsan@mscd.edu
 *  		msg0.txt
 *  		This message contains several period characters which
 *  		must be escaped before sending.
 *  
 *  02		from: tyson@knockyouout.com		Message Successful
 *  		to: molcsan@mscd.edu
 *  		msg1.txt
 *  		this message contains a series of <crlf>.<crlf> chars
 *  		which should be escaped before sending the message.
 *  
 *  03		from: s.clause@northpole.org	Message Successful
 *  		to: molcsan@mscd.edu
 *  		msg2.txt
 *  		this message contains formatted HTML content and so
 *  		should display content appropriately in HTML enabled
 *  		mail applications.
 *  
 *  04		from: 							Error, invalid FROM
 *  		to: molcsan@mscd.edu
 *  		<any message>
 *  
 *  05		from: g.bush@whitehouse.gov		Error, invalid RCPT
 *  		to: 
 *  		<any message>
 *  
 *  06		invalid mail server				Error, unable to
 *  										communicate with the
 *  										mail server.  Unable
 *  										to send message
 *  
 *  
 *  Bibliography:
 *  	@see http://www.ietf.org/rfc/rfc0821.txt
 *  	@see http://www.ietf.org/rfc/rfc0822.txt
 */

public class Main{
	/**
	 * Purpose: send an SMTP formatted message to a listening
	 * 			SMTP server.
	 * 
	 * @author Ken Molcsan
	 * @version 1.0a
	 * 
	 * Preconditions:
	 * 	readable system input stream must pass valid SMTP message
	 * 	content followed by a null line to represent EOF.
	 * 
	 * @param args 3 or 4 parameters are expected in the following
	 * 				order: host, port(optional), fromAddress, toAddress
	 * @return nothing
	 * @see nothing
	 * 
	 */
	public static void main(String[] args){
		try{
			String host, from, to;
			int port;
			
			if(args.length == 4){
				host = args[0];
				port = Integer.parseInt(args[1]);
				from = args[2];
				to = args[3];
			}else{
				host = args[0];
				port = 25; // default SMTP port = 25
				from = args[1];
				to = args[2];
			}
			String message = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			System.out.println(host + '\n' + from + '\n' + to);
			
			SmtpConnection c = new SmtpConnection(port);
			
			// Read the system in until an EOF is encountered
			while(true){ 
				String line = in.readLine();
				if(line == null) break; //EOF reached
				message += line + "\r\n"; // Insert missing CRLF
			}			
			// Send the message via SMTP
			c.send(host,from,to,message);
		}catch(Exception e){
			System.out.println("There was an error encountered" 
							 + "reading the parameters:\n");
			System.out.println(e);
			System.out.println("\nThe expected command format is"
							 + "the following:\n>"
					         + "java Main [host] [port(optional)] [from] [to]"
					         + "\n\nPlease Try again!");
		}
	}
}

/*
c:\> java -jar Kmailer_v1.0a.jar smtp.comcast.net 
     g.bush@whitehouse.gov kennymolc@gmail.com
From: "President George W. Bush" <his.holiness@whitehouse.gov>
To: "Ken Molcsan" <kennymolc@gmail.com>
Subject: Presidential Pardon

Dear Ken,

I passed gas, and request a presidential pardon.

:P

.....
....
...
..
.

Opening Connection ...
220 OMTA11.westchester.pa.mail.comcast.net comcast ESMTP server ready
250 OMTA11.westchester.pa.mail.comcast.net hello [24.9.254.120], 
    pleased to meet you
250 2.1.0 <george.bush@whitehouse.gov> sender ok
250 2.1.5 <kennymolc@gmail.com> recipient ok
354 enter mail, end with "." on a line by itself
Closing Connection ...
250 2.0.0 ju9e1Z0062cdl6Y3Xu9emD mail accepted for delivery

-----

c:\> java -jar Kmailer_v1.0a.jar smtp.comcast.net 
     g.bush@whitehouse.gov kennymolc@gmail.com
.

Opening Connection ...
220 OMTA06.westchester.pa.mail.comcast.net comcast ESMTP server ready
250 OMTA06.westchester.pa.mail.comcast.net hello [24.9.254.120], 
    pleased to meet you
250 2.1.0 <george.bush@whitehouse.gov> sender ok
250 2.1.5 <kennymolc@gmail.com> recipient ok
354 enter mail, end with "." on a line by itself
Closing Connection ...
250 2.0.0 juCn1Z0012cdl6Y3SuCnWv mail accepted for delivery

-----

c:\> java -jar Kmailer_v1.0a.jar smtp.comcast.net 
     g.bush@whitehouse.gov kennymolc@gmail.com
Hello World

Opening Connection ...
220 OMTA08.westchester.pa.mail.comcast.net comcast ESMTP server ready
250 OMTA08.westchester.pa.mail.comcast.net hello [24.9.254.120], 
    pleased to meet you
250 2.1.0 <george.bush@whitehouse.gov> sender ok
250 2.1.5 <kennymolc@gmail.com> recipient ok
354 enter mail, end with "." on a line by itself
Closing Connection ...
250 2.0.0 juEB1Z0092cdl6Y3UuEBmU mail accepted for delivery

-----

c:\> java -jar Kmailer_v1.0a.jar smtp.comcast.net 
     g.bush@whitehouse.gov @gmail.com
Hello World

Opening Connection ...
220 OMTA03.westchester.pa.mail.comcast.net comcast ESMTP server ready
250 OMTA03.westchester.pa.mail.comcast.net hello [24.9.254.120], 
    pleased to meet you
250 2.1.0 <george.bush@whitehouse.gov> sender ok
 The server has returned an error:
   -> 550 5.5.0 <@gmail.com> no local part
 Your message could not be sent.
Closing Connection ...

-----

*/