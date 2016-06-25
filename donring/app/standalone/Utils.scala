package standalone

import java.security.MessageDigest
import sys.process._
import java.net.URL
import java.io.File
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.Files;
import java.io.IOException
import java.nio.file.Paths

//import java.nio.file.{Paths, Files}

object Utils {  
  def generateMD5(s: String): String =
    MessageDigest.getInstance("MD5").digest(s.getBytes).map("%02x".format(_)).mkString
  
  private def downloadFile(webAddress: String, localFileUrl: String): String = {
    new URL(webAddress) #> new File(localFileUrl) !!
  }
  
  def downloadFileCache(webAddress: String, destination: String, olderSecs: Int = 0): String = {
    // If the file is older than olderSecs it will be download and overriden
    
    val localFileUrl = destination + '/' + generateMD5(webAddress)
    val file = new java.io.File(localFileUrl)
    if (file.exists) {
      val fileLastModified = file.lastModified()
      val timestamp = System.currentTimeMillis
            
      //println(timestamp - fileLastModified)
      
      if ((timestamp - fileLastModified) > olderSecs * 1000)
        downloadFile(webAddress, localFileUrl)
    } else {
      downloadFile(webAddress, localFileUrl)
    }
    return localFileUrl
  }
}

import java.util._
import javax.mail._
import javax.mail.internet._
import javax.activation._

object Mail {
  val smtpServerAddress = "localhost:25"
  
  def sendMail(fromAddress: String, toAddress: String, subject: String, contentHtml: Option[String] = None, content: Option[String] = None): Unit = {
    //based on http://www.tutorialspoint.com/java/java_sending_email.htm
      // Get system properties
      val properties = System.getProperties()
      // Setup mail server
      properties.setProperty("mail.smtp.host", smtpServerAddress)
      // Get the default Session object.
      val session = Session.getDefaultInstance(properties)

      try{
         // Create a default MimeMessage object.
         var message = new MimeMessage(session)
         // Set From: header field of the header.
         message.setFrom(new InternetAddress(fromAddress))
         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress))
         // Set Subject: header field
         message.setSubject(subject)
         // Now set the actual message
         contentHtml match {
           case Some(content) => message.setContent( content, "text/html" )
           case None          => println("No HTML content")
         }
         
         content match {
           case Some(content) => message.setText( content )
           case None          => println("No text content")
         }

         // Send message
         Transport.send(message)
         println("Sent message successfully....")
      } catch {
        case mex: MessagingException => mex.printStackTrace() 
      }
  }

        
/*def sendMailHtml(fromAddress, toAddress, subject, contentHtml, contentPlain = ''):
    # Create message container - the correct MIME type is multipart/alternative.
    msg = MIMEMultipart('alternative')
    msg['Subject'] = subject
    msg['From'] = fromAddress
    msg['To'] = toAddress
    
    # Create the body of the message (a plain-text and an HTML version).
    #text = "Hi!\nHow are you?\nHere is the link you wanted:\nhttp://www.python.org"
    #html = """\
    #<html>
    #  <head></head>
    #  <body>
    #    <p>Hi!<br>
    #       How are you?<br>
    #       Here is the <a href="http://www.python.org">link</a> you wanted.
    #    </p>
    #  </body>
    #</html>
    #"""
    
    # Record the MIME types of both parts - text/plain and text/html.
    part1 = MIMEText(contentPlain, 'plain')
    part2 = MIMEText(contentHtml, 'html')
    
    # Attach parts into message container.
    # According to RFC 2046, the last part of a multipart message, in this case
    # the HTML message, is best and preferred.
    msg.attach(part1)
    msg.attach(part2)
    
    # Send the message via local SMTP server.
    s = smtplib.SMTP('localhost')
    # sendmail function takes 3 arguments: sender's address, recipient's address
    # and message to send - here it is sent as one string.
    s.sendmail(fromAddress, toAddress, msg.as_string())
    s.quit()
        

*/