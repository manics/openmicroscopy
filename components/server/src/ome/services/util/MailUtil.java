package ome.services.util;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class MailUtil {
	
	private final static Logger log = LoggerFactory.getLogger(MailUtil.class);
	
	private static final long serialVersionUID = -1L;

	protected final String sender;
	protected final JavaMailSender mailSender;

	public MailUtil(String sender, JavaMailSender mailSender) {
		this.sender = sender;
		this.mailSender = mailSender;
	}
	
	public String getSender() {
		return sender;
	}

	public void sendEmail(final String from, final String to,
			final String topic, final String body, final boolean html,
			final String [] ccrecipients, final String [] bccrecipients) {
		
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setFrom(from);
				message.setSubject(topic);
				message.setTo(to);
				if (null != ccrecipients && ccrecipients.length > 0) message.setCc(ccrecipients);
				if (null != bccrecipients && bccrecipients.length > 0) message.setCc(bccrecipients);
				message.setText(body, html);
			}

		};
		
		this.mailSender.send(preparator);
	}
}
