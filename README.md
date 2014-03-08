Kmailer
==========

Developed as a project in education (MSCD.CS3700), the Kmailer application is designed as a simple SMTP sender application which is capable of sending email messages using SMTP protocol to a specified server/recipient from a specified sender.  This simple application is not currently capable of authenticating to an SMTP server, so it assumes authentication will be granted automatically, or prior to running the application.

Initial Release 4-Jun-2008
----------
- By: Ken Molcsan Jr.
- Metropolitain State University, CS3700
- Course taught by Dr. Steve Beaty

A Lesson in SMTP
==========
Executive Summary
The Simple Mail Transfer Protocol (SMTP) is a common network messaging protocol used to allow the transfer and receipt of communications across a common network connection.  Using this standardized protocol, messages can be sent and received across an existing network which hosts SMTP applications listening for common commands defined by "RFC 821".  The following article details the steps required to connect to an existing SMTP service, through use of a Telnet application, to send a simple email message to a recipient email box address. 


Motivation
==========
In order to better learn and understand the SMTP protocol interface, a test will be performed from a standard terminal application known as Telnet (a tool which is available on most modern operating systems).  The telnet terminal allows a user the ability to connect to a specified server and port combination via the TCP protocol, which then provides an interface to issue commands to a listening SMTP server.  Using this interface, we can follow standardized SMTP syntax to send an email message through an available SMTP server.  This hands-on approach will provide a better understanding of the SMTP protocol and its interface.


Introduction
==========
Since its creation in 1982, the Simple Mail Transfer Protocol (SMTP) has become a common platform for messaging communications between users across the internet.  Originally born as an internal messaging service for the United States government, the published protocol provides a simple mechanism to distribute messages between users who exist on a common network.  The standardized documentation defines a common platform which operates on a specific set of commands and communication syntax.  This common platform design ensures compatibility between numerous applications written to support the SMTP protocol. 

The design scheme behind SMTP allows a single node in a network to act as both a sending and recipient node.  This simple peer-to-peer design is capable of supporting a network of unlimited size, assuming there are an adequate number of nodes running the SMTP client/server application to support the messaging demand.  To deliver a message to another node, the sending node must be aware of the location or existence of the recipient node (or at the very least, have a channel to which it can communicate in order to access another node outside of its local visibility).  Following this simple protocol design, many applications have been developed and adopted for use within most organizations and connectivity providers across the globe, which has contributed to a global communications network, built of applications communicating with the same common language. 


Measurements and Procedures
==========
The following procedure was performed to demonstrate both the simplicity and the power of the SMTP protocol.  Using a common terminal application, known as telnet (available in most modern operating systems), a connection can be made to an existing SMTP server identified by its address and a specific port (port 25 is the standard for most SMTP servers).  Once a connection is established, a communication link must be initialized between the client terminal and the server.  When communication has been established, the message sender and recipient must be identified, and the message data can then be composed.  When message communication has been completed, the connection can then be closed and the session can be ended.


Explanation of Measurements and Procedures
==========
Once a connection has been established by the telnet terminal to the listening SMTP server, the communication link must be initialized by issuing the “HELO” command followed by an address which identifies the requesting client machine.  The listening server will then respond to the client, acknowledging that a session has been initiated, and will then listen for further commands.  

With a communication link now established, the sender of the message must be identified by issuing a MAIL command followed by a “FROM:<email@address.com>” parameter string.  Experimental trials with the Comcast SMTP server (smtp.comcast.net), did not limit the sending address to registered system users.  The next command that must be issued is the RCPT command, followed similarly by a parameter string indicating the recipient address in the format “TO:<recipient@domain.com>”.  Again, the experimental trials did not indicate any attempt by the server to validate the address entered.  While validation did not seem to occur for the message itself, it can be assumed that additional security is most likely put into place to restrict unknown client addresses from manipulating the mail server. 
After the sender and recipient data has been accepted, the remaining message can be composed through use of the “DATA” command.  This command is unique, in that it provides for a message of seemingly endless size to be composed, ending the message only when encountering a '.' character preceded and followed by a <CRLF> (carriage return, line feed) sequence of characters.  Additional information is provided within the message DATA section to provide more detail to the message recipient.  While message size is not restricted by definition, the actual size may be limited by physical hardware restrictions of components which provide the connection between multiple SMTP messaging servers.

When message composure is completed, the connection can be terminated by issuing a “QUIT” command.  Throughout the process of communicating to the SMTP server, the user can easily begin to understand the commonalities which exist in the server return codes.  This is because the codes follow a strict format allowing easy identification of warnings, errors, and temporary failures encountered when communicating with the server.  Further more, the three digit response code returned by the server offers several levels of identification by adhering to different definitions of numeric meaning amongst specific numbers.  For example, a response code of 520 indicates that the server encountered a fatal error in the message transmission and was unable to recover.  This is because the '5' character in the first position represents a fatal or unrecoverable error was encountered, while the '2' character in the second position indicates that the error is related to the transmission of the message.  Following these code definitions, a server response can be analyzed and interpreted by visibly inspecting the three digit return code.  Generally however, additional description data is appended as well for better human readability.
Explanation of Anomalies

Once initial hurdles were cleared in learning the command set requirements, it was fairly simple to begin sending messages to various recipient addresses.  However, the majority of these messages would arrive at their intended destination with blank contents, and most commonly would be caught automatically by the junk email filters and automatically sorted into a trash email folder.  Upon closer inspection however, it was plainly obvious that modern SMTP servers take the protocol to a different level of identification by examining fields defined in the beginning of the DATA section.  These fields, also known as the message header, define descriptive properties of a message such as date, subject, from, to, as well as a variety of additional fields which may be parsed at the recipient server to provide the recipient detailed information about the message.  Once these fields were discovered, I quickly realized that this message header data is used to identify and sort email messages in modern mail software applications. 


Conclusion
==========
The SMTP protocol is a simple, yet powerful, protocol which has become very widely adopted around the world.  Due to the simplicity of the protocol, and the power of the dual functionality client/server design which is supported at each SMTP node, email has become a standard communication tool used in society today.  The documented standardized design allows ease of implementation for new applications, while enforcing compatibility with existing applications.  This ease of use has certainly contributed to the growth and success of the SMTP global network.


Bibliography
==========
Postel, Johnathan B. "RFC 821 SIMPLE MAIL TRANSFER PROTOCOL"
    August 1982. <http://www.ietf.org/rfc/rfc0821.txt>
