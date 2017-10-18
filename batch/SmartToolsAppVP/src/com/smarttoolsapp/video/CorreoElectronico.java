package com.smarttoolsapp.video;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class CorreoElectronico {

	public void EnviarCorreo(Video video) throws AddressException,
			MessagingException {

		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage generateMailMessage;

		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", String.valueOf(Parametros.getPuertoCorreo(25)));// "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		

		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO,
				new InternetAddress(video.emailUser));
		generateMailMessage.setSubject("Su video ya esta publicado");
		StringBuilder emailBody = new StringBuilder();
		
		emailBody.append(String.format("Hola <b>%1$s %2$s</b><br><br>", video.firstNameUser, video.lastNameUser));
		
		emailBody.append("El video subido por ud ya esta publicado en el consurso.<br><br>");
		
		emailBody.append("Detalle del video:<br>");
		emailBody.append("Nombre: ");
		emailBody.append(video.name);
		emailBody.append("<br>");
		emailBody.append("Mensaje: ");
		emailBody.append(video.message);
		emailBody.append("<br>");
		
		emailBody.append("<br><br> Atentanemte, <br>Administrador del concurso.");
		
				
		generateMailMessage.setContent(emailBody.toString(), "text/html");
		
		if(Parametros.getFromCorreo() != null)
			generateMailMessage.setFrom(new InternetAddress(Parametros.getFromCorreo()));
		
		Transport transport = getMailSession.getTransport("smtp");

		transport.connect(Parametros.getHostCorreo("smtp.gmail.com")
				, Parametros.getUsuarioCorreo("fabricas201717473@gmail.com")
				, Parametros.getClaveCorreo("xiptcfujuygimocs"));//"F4BR1C4S2");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		
		transport.close();
	}
}
