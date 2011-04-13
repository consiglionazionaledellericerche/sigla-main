package it.cnr.contab.util.service;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.util.PropertyPlaceholderHelper;

public class MailService {
	private static Log logger = LogFactory.getLog(MailService.class);	
	@Autowired private String fromAddress;
	@Autowired private JavaMailSender mailSender;
	@Autowired private PropertyPlaceholderHelper propertyPlaceholderHelper;
	@Autowired private Boolean testMode; 
	@Autowired private String mailMessageFooter;
	@Autowired private DateFormat dateShortFormat;
	@Autowired private DateFormat timeShortFormat;
	
	private String defaultSubject;
	private String defaultText;
	private String toTestModeList;
	
	public void setToTestModeList(String toTestModeList) {
		this.toTestModeList = toTestModeList;
	}

	public void setDefaultSubject(String defaultSubject) {
		this.defaultSubject = defaultSubject;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	@SuppressWarnings("unchecked")
	public void send(List<String> to, Object...params){
		send(to, Collections.EMPTY_LIST, defaultSubject, defaultText, params);
	}
	
	@SuppressWarnings("unchecked")
	public void send(List<String> to, String subject, String text, Object...params){
		send(to, Collections.EMPTY_LIST, subject, text, params);
	}
	
	@SuppressWarnings("unchecked")
	public void send(List<String> to, List<String> cc, String subject, String text, Object...params){
		send(to, cc, Collections.EMPTY_LIST, subject, text, params);
	}
	
	public void send(List<String> to, List<String> cc, List<String> bcc, String subject, String text, Object...params){
		subject = resolvePalceHolder(subject, params);
		text = resolvePalceHolder(text, params);
		
		final StringBuffer messageText = new StringBuffer(text).append(resolvePalceHolder(mailMessageFooter, new Properties()));
		if (testMode){
			messageText.append("<HR>");
			messageText.append("<strong>The Application is in TEST MODE</strong>");
			messageText.append("<BR>Mail must be send to: ").append(convertListToAddress(to));
			messageText.append("<BR>Mail must be send to cc: ").append(convertListToAddress(cc));
			messageText.append("<BR>Mail must be send to bcc: ").append(convertListToAddress(bcc));
			cc.clear();
			bcc.clear();
		}
		try{
			MessagePreparator messagePreparator = new MessagePreparator(
					fromAddress,
					testMode?toTestModeList:convertListToAddress(to),
					convertListToAddress(cc),
					convertListToAddress(bcc),
					subject,
					messageText.toString());
			mailSender.send(messagePreparator);
		}catch (MailException e) {
			logger.error("Error sending E-Mail", e);
		}	
	}
	
	@SuppressWarnings("unchecked")
	public String resolvePalceHolder(String text, Object...params) {
		Properties prop = new Properties();
		try{
			if (params != null)
				for (Object object : params) {
					Map<String, Serializable> map = BeanUtils.describe(object);
					for (String key : map.keySet()) {
						Serializable value = map.get(key);
						if (value != null)
							prop.put(key, value);
					}			
				}
			return resolvePalceHolder(text, prop);
		}catch (Exception e) {
			if (logger.isErrorEnabled())
				logger.error("Error resolving placeHolder", e);
		}
		return text;
	}
	
	public String resolvePalceHolder(String text, Properties prop) {
		if (prop == null)
			prop = new Properties();
		prop.putAll(System.getProperties());
		prop.put("date.current.short", dateShortFormat.format(Calendar.getInstance().getTime()));
		prop.put("time.current.short", timeShortFormat.format(Calendar.getInstance().getTime()));
		return propertyPlaceholderHelper.replacePlaceholders(text, prop);
	}

	private String convertListToAddress(List<String> source){
		if (source.isEmpty())
			return null;
		StringBuilder sb = new StringBuilder();
		for (String x : source)
			sb.append(x + ",");
		sb.delete(sb.length()-",".length(), sb.length());
		return sb.toString();
	}
	
	class MessagePreparator implements MimeMessagePreparator{
		private final String from;
		private final String to;
		private final String cc;
		private final String bcc;
		private final String subject;
		private final String text;
		
		public MessagePreparator(String from, String to, String cc, String bcc,
				String subject, String text) {
			super();
			this.from = from;
			this.to = to;
			this.cc = cc;
			this.bcc = bcc;
			this.subject = subject;
			this.text = text;
		}

		public void prepare(MimeMessage mimeMessage) throws Exception {
		     MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		     message.setFrom(from);
		     message.setTo(to);
		     if (cc != null)
		    	 message.setCc(cc);
		     if (bcc != null)
		    	 message.setBcc(bcc);
		     message.setSubject(subject);
		     message.setText(text, true);
		}
		
	}
}
