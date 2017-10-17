package com.smarttoolsapp.video;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import software.amazon.awssdk.SdkBaseException;
import software.amazon.awssdk.SdkClientException;
import software.amazon.awssdk.services.sqs.SQSClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.InvalidIdFormatException;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.OverLimitException;
import software.amazon.awssdk.services.sqs.model.ReceiptHandleIsInvalidException;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SQSException;

public class MensajesSQS {
	
	
	
	String queueUrl = "https://sqs.us-east-2.amazonaws.com/337432867796/smart-tools-app"; 
	SQSClient sqs;
	
	public List<Video> consultarVideosXConvertir()
	{
		List<Video> videos = new ArrayList<Video>();
		
		try {
			if(sqs == null)
				sqs = SQSClient.create();
			
			ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
			        .queueUrl(queueUrl)
			        .maxNumberOfMessages(1)
			        .messageAttributeNames("*")
			        .build();
			
			List<Message> messages= sqs.receiveMessage(receiveMessageRequest).messages();
			if(messages != null){
				for(Message mensaje:messages){
					
					try {
						videos.add(videoFromMessageAtrributes(mensaje.messageAttributes(), mensaje.receiptHandle()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (OverLimitException e) {
			e.printStackTrace();
		} catch (SQSException e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		} catch (SdkBaseException e) {
			e.printStackTrace();
		}
		
		return(videos);
	}
	
	public Video videoFromMessageAtrributes(Map<String, MessageAttributeValue> rs, String receiptHandler)
	{
		Video video = new Video();
		
		video.concursoId = rs.get("concurso_id").stringValue();
		video.videoId = rs.get("video_id").stringValue();
		video.sin_convertir_file_name = rs.get("file_name").stringValue();
		video.emailUser = rs.get("emailUser").stringValue();
		video.receiptHandle = receiptHandler;
		
		return(video);
	}
	
	public  void borrarVideoDePendientes(Video video)
	{
		try {
			if(sqs == null)
				sqs = SQSClient.create();
			
			DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
			        .queueUrl(queueUrl)
			        .receiptHandle(video.receiptHandle)
			        .build();
			sqs.deleteMessage(deleteMessageRequest);
			
		} catch (InvalidIdFormatException e) {
			e.printStackTrace();
		} catch (ReceiptHandleIsInvalidException e) {
			e.printStackTrace();
		} catch (SQSException e) {
			e.printStackTrace();
		} catch (SdkClientException e) {
			e.printStackTrace();
		} catch (SdkBaseException e) {
			e.printStackTrace();
		}

	}

}
